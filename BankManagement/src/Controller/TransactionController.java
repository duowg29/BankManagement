package Controller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import Model.DatabaseConnection;
import Model.Transaction;
import Model.User;

public class TransactionController {
    private static char[] userOtp;
    public static void performTransaction(long senderAccountId, long receiverAccountId, BigDecimal amount, long loggedInAccountId) {
        try {
            if (!isValidAccount(senderAccountId) || !isValidAccount(receiverAccountId)) {
                System.out.println("Invalid sender or receiver account.");
                return;
            }
            if (!checkSufficientFunds(senderAccountId, amount)) {
                System.out.println("Insufficient funds in sender's account.");
                return;
            }
            User sender = getUserInfo(senderAccountId);
            if (sender == null) {
                System.out.println("Sender account does not exist.");
                return;
            }
            Transaction transaction = new Transaction(0, senderAccountId, receiverAccountId, amount, "", "","");
            updateAccountBalances(senderAccountId, receiverAccountId, amount);

            String receiverName = getAccountName(receiverAccountId);

            String senderName = getAccountName(senderAccountId);

            writeTransaction(transaction, senderAccountId, receiverAccountId, senderName, receiverName, loggedInAccountId);

            System.out.println("Transaction successful.");
        } catch (SQLException | ClassNotFoundException | IOException e) {

            System.out.println("An error has occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }


    
 // Retrieve user account information from the database based on accountID
    public static User getUserInfo(long accountID) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT a.Username, a.Password FROM Accounts a WHERE a.AccountID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setLong(1, accountID);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    String username = rs.getString("Username");
                    String password = rs.getString("Password");

                    return new User(accountID, username, password);
                } else {
                    return null; 
                }
            }
        }
    }


    // Validate account
    private static boolean isValidAccount(long account) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT 1 FROM Accounts WHERE AccountID = ?")) { // kiểm tra sự tồn tại của một tài khoản
            pstm.setLong(1, account);
            
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Check if there are sufficient funds in the account
    private static boolean checkSufficientFunds(long account, BigDecimal amount) throws SQLException, ClassNotFoundException, IOException {        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT Balance FROM Accounts WHERE AccountID = ?")) {
            pstm.setLong(1, account);
            
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    BigDecimal balance = rs.getBigDecimal("Balance");
                    return balance.compareTo(amount) >= 0;
                }
                return false;
            }
        }
    }

 // Update sender and receiver account balances
    public static void updateAccountBalances(long senderAccount, long receiverAccount, BigDecimal amount) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Turn off auto-commit mode to manually manage transactions, performing multiple operations in one transaction.
            connection.setAutoCommit(false);
            try {
                // Update sender's balance
                try (PreparedStatement pstm = connection.prepareStatement("UPDATE Accounts SET Balance = Balance - ? WHERE AccountID = ?")) {
                    pstm.setBigDecimal(1, amount); 
                    pstm.setLong(2, senderAccount); 
                    pstm.executeUpdate(); 
                }

                // Update receiver's balance
                try (PreparedStatement pstm = connection.prepareStatement("UPDATE Accounts SET Balance = Balance + ? WHERE AccountID = ?")) {
                    pstm.setBigDecimal(1, amount); 
                    pstm.setLong(2, receiverAccount); 
                    pstm.executeUpdate(); 
                }

                // Commit transaction to confirm changes
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e; 
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public static BigDecimal getSenderBalance(long senderAccount) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT Balance FROM Accounts WHERE AccountID = ?")) {
            pstm.setLong(1, senderAccount);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getBigDecimal("Balance");
                } else {
                    throw new SQLException("Sender account not found.");
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new SQLException("Database connection error.");
        }
    }



    



    // Get the name of the sender's account
    public static String getSenderName(long senderAccount) throws ClassNotFoundException, SQLException, IOException {
        return getAccountName(senderAccount);
    }
    // Get the name of the receiver's account
    public static String getReceiverName(long receiverAccount) throws ClassNotFoundException, SQLException, IOException {
        return getAccountName(receiverAccount);
    }

    public static String getAccountName(long accountId) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT a.FullName FROM Users a JOIN Accounts b ON a.CitizenID = b.CitizenID WHERE b.AccountID = ?")) {
            pstm.setLong(1, accountId);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("FullName");
                }
            }
        }
        return null;
    }


    public static void writeTransaction(Transaction transaction, long senderAccountId, long receiverAccountId, String senderName, String receiverName, long loggedInAccountId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String formattedDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

            String senderRemarks = String.format("Transfer to %s (account %d)", receiverName, receiverAccountId);
            
            String receiverRemarks = String.format("Received from %s (account %d)", senderName, senderAccountId);

            System.out.println("Transaction Details: Sender: " + senderAccountId + ", Receiver: " + receiverAccountId + ", Remarks (Sender): " + senderRemarks + ", Remarks (Receiver): " + receiverRemarks);

            writeTransactionRecord(transaction, senderAccountId, receiverAccountId, formattedDate, senderRemarks, "SENT");

            writeTransactionRecord(transaction, receiverAccountId, senderAccountId, formattedDate, receiverRemarks, "RECEIVED");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeTransactionRecord(Transaction transaction, long senderAccountId, long receiverAccountId, String formattedDate, String remarks, String transactionType) throws SQLException, ClassNotFoundException, IOException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Transactions (TransactionId, SenderAccount, ReceiverAccount, Amount, TransactionTime, Remarks, TransactionRole) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            int nextTransactionId = getNextTransactionId(); // Phương thức này để lấy TransactionId mới
            
            stmt.setLong(1, nextTransactionId);
            stmt.setLong(2, senderAccountId);
            stmt.setLong(3, receiverAccountId);
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setString(5, formattedDate);
            stmt.setString(6, remarks);
            stmt.setString(7, transactionType);

            stmt.executeUpdate();
        }
    }







    // Get the next transaction ID
    private static int getNextTransactionId() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            // Prepare SQL statement to retrieve the highest transaction ID from Transactions table
            try (PreparedStatement pstm = connection.prepareStatement("SELECT MAX(TransactionId) FROM Transactions")) {
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        int maxId = rs.getInt(1);
                        return maxId + 1; // Return the next ID, incremented by 1 from the current highest ID
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace(); 
        }
        return 1; 
        }

    // Search transactions of an account within a specific time range
    public static List<Transaction> searchTransactionsByTimeAndAccount(String startTime, String endTime, long accountID) {
        List<Transaction> transactions = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Prepare SQL statement to retrieve transactions of accountID within startTime and endTime
            String sql = "SELECT * FROM Transactions WHERE (SenderAccount = ? OR ReceiverAccount = ?) AND TransactionTime BETWEEN ? AND ?";

            try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                // Convert startTime and endTime to yyyy-MM-dd format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedStartDate = dateFormat.parse(startTime);
                java.util.Date parsedEndDate = dateFormat.parse(endTime);

                // Convert to java.sql.Date format for use in SQL statement
                java.sql.Date startDate = new java.sql.Date(parsedStartDate.getTime());
                java.sql.Date endDate = new java.sql.Date(parsedEndDate.getTime());

                // Set parameters in the SQL statement
                pstm.setLong(1, accountID);
                pstm.setLong(2, accountID);
                pstm.setDate(3, startDate);
                pstm.setDate(4, endDate);

                // Execute SQL query and retrieve results from ResultSet
                try (ResultSet rs = pstm.executeQuery()) {
                    while (rs.next()) {
                        Transaction transaction = new Transaction(
                            rs.getInt("TransactionId"),
                            rs.getLong("SenderAccount"), 
                            rs.getLong("ReceiverAccount"), 
                            rs.getBigDecimal("Amount"),
                            rs.getString("TransactionTime"),
                            rs.getString("Remarks"),
                            rs.getString("TransactionRole")
                        );
                        transactions.add(transaction); // Add transaction to the list
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException | ParseException e) {
            e.printStackTrace();
        }

        return transactions; 
    }



    public static List<Transaction> getAllTransactionsByAccount(long accountId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE SenderAccount = ? OR ReceiverAccount = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, accountId);
            pstm.setLong(2, accountId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    long senderAccount = rs.getLong("SenderAccount");
                    long receiverAccount = rs.getLong("ReceiverAccount");
                    
                    // Check if accountId is the sender or receiver
                    if (senderAccount == accountId || receiverAccount == accountId) {
                        Transaction transaction = new Transaction(
                            rs.getInt("TransactionId"),
                            senderAccount,
                            receiverAccount,
                            rs.getBigDecimal("Amount"),
                            rs.getString("TransactionTime"),
                            rs.getString("Remarks"),
                            rs.getString("TransactionRole")
                        );
                        transactions.add(transaction);
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    // Retrieve transactions sent by an account

    public static List<Transaction> getSentTransactionsByAccount(long accountId) {
        List<Transaction> sentTransactions = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE SenderAccount = ? AND TransactionRole = 'SENT'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, accountId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                        rs.getInt("TransactionId"),
                        accountId,
                        rs.getLong("ReceiverAccount"),
                        rs.getBigDecimal("Amount"),
                        rs.getString("TransactionTime"),
                        rs.getString("Remarks"),
                        rs.getString("TransactionRole")
                    );
                    sentTransactions.add(transaction);
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return sentTransactions;
    }
    // Retrieve transactions received by an account

    public static List<Transaction> getReceivedTransactionsByAccount(long accountId) {
        List<Transaction> receivedTransactions = new ArrayList<>();
        String sql = "SELECT * FROM Transactions WHERE ReceiverAccount = ? AND TransactionRole = 'RECEIVED'";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(sql)) {
            pstm.setLong(1, accountId);
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    Transaction transaction = new Transaction(
                        rs.getInt("TransactionId"),
                        rs.getLong("SenderAccount"),
                        accountId,
                        rs.getBigDecimal("Amount"),
                        rs.getString("TransactionTime"),
                        rs.getString("Remarks"),
                        rs.getString("TransactionRole")
                    );
                    receivedTransactions.add(transaction);
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return receivedTransactions;
    }




       // Set the OTP for the user
       public static void setUserOtp(char[] otp) {
           userOtp = otp;
       }

       // Get the OTP for the user
       public static char[] getUserOtp() {
           return userOtp;
       }

       public static void saveOtpToDatabase(char[] otp, long accountId) {
    	    try (Connection connection = DatabaseConnection.getConnection()) {
                // Check if OTP already exists in the database
    	        if (isOtpExistsInDatabase(otp, accountId)) {
    	            JOptionPane.showMessageDialog(null, "OTP already exists in the database.", "Error", JOptionPane.ERROR_MESSAGE);
    	            return; 
    	        }

                // Delete old OTP before saving new OTP
    	        deleteOldOtpFromDatabase(accountId);

                // Convert OTP from char array to String
    	        String otpString = new String(otp);

                // Save new OTP to the database
    	        String sql = "INSERT INTO OtpTable (Otp, AccountID) VALUES (?, ?)";
    	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    	            pstmt.setString(1, otpString); 
    	            pstmt.setLong(2, accountId); 
    	            pstmt.executeUpdate();
    	        }
    	        JOptionPane.showMessageDialog(null, "OTP has been successfully saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
    	    } catch (SQLException | ClassNotFoundException | IOException e) {
    	        e.printStackTrace();
    	    }
    	}


       public static boolean isOtpExistsInDatabase(char[] otp, long accountId) throws SQLException, ClassNotFoundException, IOException {
    	    try (Connection connection = DatabaseConnection.getConnection()) {
    	        String otpString = new String(otp);
    	        String sql = "SELECT * FROM OtpTable WHERE Otp = ? AND AccountID = ?";
    	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    	            pstmt.setString(1, otpString);
    	            pstmt.setLong(2, accountId);
    	            try (ResultSet rs = pstmt.executeQuery()) {
    	                return rs.next();
    	            }
    	        }
    	    }
    	}

    	public static void deleteOldOtpFromDatabase(long accountId) throws SQLException, ClassNotFoundException, IOException {
    	    try (Connection connection = DatabaseConnection.getConnection()) {
    	        String sql = "DELETE FROM OtpTable WHERE AccountID = ?";
    	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    	            pstmt.setLong(1, accountId);
    	            pstmt.executeUpdate();
    	        }
    	    }
    	}
    	// Retrieve user OTP from the database
    	public static char[] retrieveUserOtpFromDatabase(long accountID) {
    	    char[] otpChars = null; 

    	    try (Connection connection = DatabaseConnection.getConnection()) {
                // Prepare SQL statement to retrieve OTP data from OtpTable
    	        String sql = "SELECT Otp FROM OtpTable WHERE AccountID = ?";
    	        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
    	            pstmt.setLong(1, accountID);

    	            try (ResultSet rs = pstmt.executeQuery()) {
    	                if (rs.next()) {
    	                    String otpString = rs.getString("Otp");
    	                    otpChars = otpString.toCharArray(); // Chuyển chuỗi OTP thành mảng char
    	                }
    	            }
    	        }
    	    } catch (SQLException | ClassNotFoundException | IOException e) {
    	        e.printStackTrace();
    	    }

    	    return otpChars; 
    	}

    
    	// Validate OTP provided by the user
       public static boolean validateOtp(char[] otp, long accountId) {
    	    try (Connection connection = DatabaseConnection.getConnection()) {
                // Convert OTP from char array to String for use in SQL query
    	        String otpString = new String(otp);

                // SQL statement to check if OTP exists in OtpTable for current AccountID
    	        String sql = "SELECT * FROM OtpTable WHERE Otp = ? AND AccountID = ?";
    	        
    	        try (PreparedStatement pstm = connection.prepareStatement(sql)) {
    	            pstm.setString(1, otpString); 
    	            pstm.setLong(2, accountId); 

    	            try (ResultSet rs = pstm.executeQuery()) {
                        return rs.next(); // Return true if valid OTP exists in the database, false otherwise
    	            }
    	        }
    	    } catch (SQLException | ClassNotFoundException | IOException e) {
    	        e.printStackTrace(); 
    	        return false; 
    	    } finally {
                java.util.Arrays.fill(otp, '0'); // Clear OTP array content from memory for security
    	    }
    	}


       // Check if the provided OTP exists in the database
       public static boolean checkOtp(char[] otp, long accountId) {
           try (Connection connection = DatabaseConnection.getConnection()) {
               // Convert OTP from char array to String for use in SQL query
               String otpString = new String(otp);

               // SQL statement to check if OTP exists in OtpTable
               String sql = "SELECT * FROM OtpTable WHERE Otp = ? AND AccountID = ?";
               
               try (PreparedStatement pstm = connection.prepareStatement(sql)) {
                   pstm.setString(1, otpString);
                   pstm.setLong(2, accountId); 
                   
                   try (ResultSet rs = pstm.executeQuery()) {
                       return rs.next(); // Return true if OTP exists in the database, false otherwise
                   }
               }
           } catch (SQLException | ClassNotFoundException | IOException e) {
               e.printStackTrace(); 
               return false; 
           } finally {
               java.util.Arrays.fill(otp, '0'); // Clear OTP array content from memory for security
           }
       }

       
       
       
       public static void saveTransactionsToExcel(DefaultTableModel tableModel, long accountID, String filePath) throws IOException {
   	    Workbook workbook = null;
   	    FileInputStream fis = null;
   	    FileOutputStream fos = null;

   	    try {
   	        File file = new File(filePath);

            // Check if the Excel file exists
   	        if (file.exists()) {
                // If the file exists, open Workbook from the file
   	            fis = new FileInputStream(file);
   	            workbook = new XSSFWorkbook(fis);
   	        } else {
                // If the file doesn't exist, create a new Workbook
   	            workbook = new XSSFWorkbook();
   	        }

            // Check if the sheet exists, if not create a new one
   	        Sheet sheet = workbook.getSheet(String.valueOf(accountID));
   	        if (sheet == null) {
   	            sheet = workbook.createSheet(String.valueOf(accountID));
                // Create header row
   	            Row headerRow = sheet.createRow(0);
   	            int colCount = tableModel.getColumnCount();
   	            CellStyle centerAlignmentStyle = createCenterAlignmentStyle(workbook);
   	            for (int col = 0; col < colCount; col++) {
   	                Cell cell = headerRow.createCell(col);
   	                cell.setCellValue(tableModel.getColumnName(col));
   	                cell.setCellStyle(centerAlignmentStyle);
   	            }
   	        }

            // Number of rows and columns in tableModel
   	        int rowCount = tableModel.getRowCount();
   	        int colCount = tableModel.getColumnCount();

            // Write data from tableModel to rows in Sheet
            for (int row = 0; row < rowCount; row++) {
                Row excelRow = sheet.createRow(sheet.getLastRowNum() + 1);
                for (int col = 0; col < colCount; col++) {
                    Object value = tableModel.getValueAt(row, col);
                    Cell cell = excelRow.createCell(col);
                    if (value != null) {
                        if (value instanceof Number) {
                            cell.setCellValue(((Number) value).doubleValue());
                        } else {
                            cell.setCellValue(value.toString());
                        }
                    }
                    cell.setCellStyle(createCenterAlignmentStyle(workbook));
                }
            }

            // Automatically adjust column size to fit content
            for (int col = 0; col < colCount; col++) {
                sheet.autoSizeColumn(col);
            }

            // Write Workbook to file
            fos = new FileOutputStream(filePath);
            workbook.write(fos);
            JOptionPane.showMessageDialog(null, "Transactions have been successfully saved to: " + filePath, "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to save transactions to Excel: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } finally {
            if (workbook != null) {
                workbook.close();
            }
            if (fis != null) {
                fis.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    // Create a CellStyle with center alignment
    private static CellStyle createCenterAlignmentStyle(Workbook workbook) {
        CellStyle centerAlignmentStyle = workbook.createCellStyle(); // Create a new CellStyle object from Workbook
        centerAlignmentStyle.setAlignment(HorizontalAlignment.CENTER); // Set center alignment for this CellStyle
        return centerAlignmentStyle; // Return the configured CellStyle object
    }
}

