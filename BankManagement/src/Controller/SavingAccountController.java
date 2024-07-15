package Controller;

import Model.DatabaseConnection;
import Model.SavingAccount;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SavingAccountController {

    // Method to open a new savings account
    public static void openSavingAccount(long accountID, BigDecimal amount) {
        try {
            // Validate account
            if (!isValidAccount(accountID)) {
                System.out.println("Invalid account number.");
                return;
            }

            // Check sufficient funds in the user's account
            if (!checkSufficientFunds(accountID, amount)) {
                System.out.println("Insufficient funds.");
                return;
            }

            // Create new savings account
            SavingAccount savingAccount = createSavingAccount(accountID, amount);

            // Update user's main account balance
            updateMainAccountBalance(accountID, amount);

            // Notify successful opening of savings account
            System.out.println("Savings account opened successfully. Details: " + savingAccount);
        } catch (SQLException | ClassNotFoundException | IOException e) {
            System.out.println("An error has occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Validate account
    private static boolean isValidAccount(long accountID) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT 1 FROM Accounts WHERE AccountID = ?")) {
            pstm.setLong(1, accountID);
            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Check sufficient funds in the account
    private static boolean checkSufficientFunds(long accountID, BigDecimal amount) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("SELECT Balance FROM Accounts WHERE AccountID = ?")) {
            pstm.setLong(1, accountID);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    BigDecimal balance = rs.getBigDecimal("Balance");
                    return balance.compareTo(amount) >= 0;
                }
                return false;
            }
        }
    }
 // Get the next transaction ID
    private static int getNextSavingAccountId() throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = DatabaseConnection.getConnection()) {
        	//Prepare an SQL statement to get the highest transaction ID from the Transactions table
            try (PreparedStatement pstm = connection.prepareStatement("SELECT MAX(SavingAccountID) FROM SavingAccounts")) {
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        int maxId = rs.getInt(1);
                        return maxId + 1; //Return the next ID, incremented by 1 from the current highest ID
                    }
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException e) {
            e.printStackTrace();
            
        }
        return 1; //  Return the first ID if there are no transactions in the database
    }

    // Create a new savings account
    private static SavingAccount createSavingAccount(long accountID, BigDecimal amount) throws SQLException, ClassNotFoundException, IOException {
        String query = "INSERT INTO SavingAccounts (SavingAccountID, AccountID, Balance, InterestRate, StartDate) VALUES (?, ?, ?, ?, getDate())";
        int savingaccountid = getNextSavingAccountId();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstm.setInt(1, savingaccountid);
        	pstm.setLong(2, accountID);
            pstm.setBigDecimal(3, amount);
            pstm.setBigDecimal(4, new BigDecimal("0.04")); // 4% interest rate
            pstm.executeUpdate();

            // Retrieve the generated keys (including SavingAccountID)
            try (ResultSet generatedKeys = pstm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int savingAccountID = generatedKeys.getInt(1);
                    BigDecimal interestRate = new BigDecimal("0.04"); // hardcoded for simplicity
                    LocalDateTime startDate = LocalDateTime.now(); // use the actual StartDate retrieved from DB if needed
                    SavingAccount newSavingAccount = new SavingAccount(savingAccountID, accountID, amount, interestRate);
                    return newSavingAccount;
                } else {
                    throw new SQLException("Creating saving account failed, no ID obtained.");
                }
            }
        }
    }

    // Update main account balance after opening a savings account
    private static void updateMainAccountBalance(long accountID, BigDecimal amount) throws SQLException, ClassNotFoundException, IOException {
        String query = "UPDATE Accounts SET Balance = Balance - ? WHERE AccountID = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(query)) {
            pstm.setBigDecimal(1, amount);
            pstm.setLong(2, accountID);
            pstm.executeUpdate();
        }
    }

}