package Controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.DatabaseConnection;
import Model.User;

import java.sql.ResultSet;

public class CRUD {

    // Register account info by INSERT INTO
    public boolean signUpAccount(long accountID, String username, String password, String roles, String accountStatus, String citizenID) throws ClassNotFoundException, IOException {
        String sql = "INSERT INTO Accounts (AccountID, Username, Password, Roles, AccountStatus, CitizenID) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountID);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, roles);
            stmt.setString(5, accountStatus);
            stmt.setString(6, citizenID);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Register user info by INSERT INTO
    public boolean signUpUser(String citizenID, String fullName, String gender, int day, int month, int year, String province, String phoneNumbers, String email) throws ClassNotFoundException, IOException {
        String sql = "INSERT INTO Users (CitizenID, FullName, Gender, DateOfBirth, Province, PhoneNumbers, Email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, citizenID);
            stmt.setString(2, fullName);
            stmt.setString(3, gender);
            stmt.setString(4, day + "/" + month + "/" + year);
            stmt.setString(5, province);
            stmt.setString(6, phoneNumbers);
            stmt.setString(7, email);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login by validate username, password, role, accountStatus in Database
    public static String signInAccount(String username, String password) throws ClassNotFoundException, IOException {
        String sql = "SELECT AccountStatus, Roles, CitizenID FROM Accounts WHERE Username = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String accountStatus = rs.getString("AccountStatus");
                    String roles = rs.getString("Roles");
                    String citizenID = rs.getString("CitizenID");

                    if ("Closed".equals(accountStatus)) {
                        return "Tài khoản đã bị đóng";
                    } else if ("Opened".equals(accountStatus)) {
                        if ("Admin".equals(roles)) {
                            // AdminMenu();
                            return "Chuyển sang menu Admin";
                        } else if ("User".equals(roles)) {
                            // UserMenu();
                            return "Chuyển sang menu User";
                        } else {
                            return "Roles không hợp lệ";
                        }
                    } else {
                        return "Trạng thái tài khoản không hợp lệ";
                    }
                } else {
                    return "Tài khoản không tồn tại";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Lỗi khi truy cập cơ sở dữ liệu";
        }
    }
    
    public List<Object[]> getAccountStatusInfo() throws ClassNotFoundException, IOException {
        String sql = "SELECT AccountID, CitizenID, AccountStatus FROM Accounts";
        List<Object[]> accountStatusInfo = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long accountID = rs.getLong("AccountID");
                String citizenID = rs.getString("CitizenID");
                String accountStatus = rs.getString("AccountStatus");
                accountStatusInfo.add(new Object[] { accountID, citizenID, accountStatus });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountStatusInfo;
    }

    // Delete (or close) account by UPDATE accountStatus
    public boolean setAccountStatus(String citizenID, String accountStatus) throws ClassNotFoundException, IOException {
        String sql = "UPDATE Accounts SET AccountStatus = ? WHERE CitizenID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountStatus); // Fix the parameter index
            stmt.setString(2, citizenID);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean toggleAccountStatus(long accountID, String newStatus) throws ClassNotFoundException, IOException {
        String sql = "UPDATE Accounts SET AccountStatus = ? WHERE AccountID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setLong(2, accountID);
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteUser(String citizenID) throws ClassNotFoundException, IOException {
        String sql = "DELETE FROM Users WHERE CitizenID = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, citizenID);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method to retrieve detailed account information
    public List<Object[]> getAccountInfo() throws ClassNotFoundException, IOException {
        String sql = "SELECT AccountID, CitizenID, Username, AccountStatus FROM Accounts";
        List<Object[]> accountInfo = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long accountID = rs.getLong("AccountID");
                String citizenID = rs.getString("CitizenID");
                String username = rs.getString("Username");
                String accountStatus = rs.getString("AccountStatus");
                accountInfo.add(new Object[] { accountID, citizenID, username, accountStatus });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountInfo;
    }
    public static User getUserInfoMenu() throws ClassNotFoundException, IOException {
        String sql = "SELECT u.*, a.AccountID, a.UserName, a.Roles, a.Balance FROM Users u JOIN Accounts a ON u.CitizenID = a.CitizenID ";
        User user = null;
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String fullName = rs.getString("FullName");
                    String gender = rs.getString("Gender");
                    String dateOfBirth = rs.getString("DateOfBirth");
                    String province = rs.getString("Province");
                    String phoneNumbers = rs.getString("PhoneNumbers");
                    String email = rs.getString("Email");
                    String citizenID = rs.getString("CitizenID");
                    Long accountID = rs.getLong("AccountID");
                    String username = rs.getString("Username");
                    String roles = rs.getString("Roles");
                    BigDecimal balance = rs.getBigDecimal("Balance");
                    
                    user = new User(accountID, fullName, gender, dateOfBirth, province, phoneNumbers, email, citizenID, username, roles, balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public static User getUserInfoByAccountID(Long accountID) throws ClassNotFoundException, IOException {
        String sql = "SELECT u.*, a.AccountID, a.UserName, a.Roles, a.Balance FROM Users u JOIN Accounts a ON u.CitizenID = a.CitizenID WHERE a.AccountID = ?";
        User user = null;
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, accountID);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String citizenID = rs.getString("CitizenID");
                    String fullName = rs.getString("FullName");
                    String gender = rs.getString("Gender");
                    String dateOfBirth = rs.getString("DateOfBirth");
                    String province = rs.getString("Province");
                    String phoneNumbers = rs.getString("PhoneNumbers");
                    String email = rs.getString("Email");
                    String username = rs.getString("Username");
                    String roles = rs.getString("Roles");
                    BigDecimal balance = rs.getBigDecimal("Balance");
                    
                    user = new User(accountID, fullName, gender, dateOfBirth, province, phoneNumbers, email, citizenID, username, roles, balance);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }



    
    public List<Object[]> getTransactionInfo() throws ClassNotFoundException, IOException {
        String sql = "SELECT * FROM Transactions";
        List<Object[]> transactions = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                long transactionID = rs.getLong("TransactionID");
                long senderAccount = rs.getLong("SenderAccount");
                long receiverAccount = rs.getLong("ReceiverAccount");
                double amount = rs.getDouble("Amount");
                String transactionTime = rs.getString("TransactionTime");
                String remarks = rs.getString("Remarks");

                Object[] transaction = {transactionID, senderAccount, receiverAccount, amount, transactionTime, remarks};
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }
    
    public static void updateUserBalance(User user) throws SQLException, ClassNotFoundException, IOException {
        String sql = "UPDATE Accounts SET Balance = ? WHERE AccountID = ?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBigDecimal(1, user.getBalance());
            stmt.setLong(2, user.getAccountID());
            stmt.executeUpdate();
        }
    }
    
    // Method to retrieve detailed user information
    public List<Object[]> getUserInfo() throws ClassNotFoundException, IOException {
        String sql = "SELECT CitizenID, FullName, Gender, DateOfBirth, Province, PhoneNumbers, Email FROM Users";
        List<Object[]> userInfo = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String citizenID = rs.getString("CitizenID");
                String fullName = rs.getString("FullName");
                String gender = rs.getString("Gender");
                String dateOfBirth = rs.getString("DateOfBirth");
                String province = rs.getString("Province");
                String phoneNumbers = rs.getString("PhoneNumbers");
                String email = rs.getString("Email");
                userInfo.add(new Object[]{citizenID, fullName, gender, dateOfBirth, province, phoneNumbers, email});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }


    
}
