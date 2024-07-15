package Model;

import java.math.BigDecimal;
import java.util.Date;


public class SavingAccount {
    private int savingAccountID;
    private long accountID;
    private BigDecimal balance;
    private BigDecimal interestRate;
    private Date startDate;

    public SavingAccount(int savingAccountID, long accountID, BigDecimal balance, BigDecimal interestRate) {
        this.savingAccountID = savingAccountID;
        this.accountID = accountID;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    public int getSavingAccountID() {
        return savingAccountID;
    }

    public long getAccountID() {
        return accountID;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public Date getStartDate() {
        return startDate;
    }
}