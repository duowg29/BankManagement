package Model;

import java.math.BigDecimal;
import java.sql.Date;

public class User extends Human {
    private long accountID;
    private String username;
    private String password;
    private BigDecimal balance;
    private String roles;
    
    public User(long accountID, String username, String password) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
    }
   public User() {
	   
   }
    public User(long accountID,String fullName, String gender, String dateOfBirth, String address, String phoneNumbers, String email, String citizenID,  String username,String roles, BigDecimal balance) {
        super(fullName, gender, dateOfBirth, address, phoneNumbers, email, citizenID);
        this.accountID = accountID;
        this.username = username;
        this.roles = roles;
        this.balance = balance;
    }
    public  long getAccountID() {
        return accountID;
    }

    public void setAccountID(long accountID) {
        this.accountID = accountID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getRoles() {
        return roles;
    }

    
    @Override
    public String toString() {
        return "Account Information" +
                "\naccountID: " + accountID  +
                "\nbalance: " + balance + 
                "\nfullName: " + getFullName() +
                "\ngender: " + getGender() +
                "\ndateOfBirth: " + getDateOfBirth() +
                "\naddress: " + getAddress() +
                "\nphoneNumbers: " + getPhoneNumbers() +
                "\nemail: " + getEmail() +
                "\ncitizenID: " + getCitizenID() ;
    }

}