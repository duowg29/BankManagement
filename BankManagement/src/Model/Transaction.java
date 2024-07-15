package Model;

import java.math.BigDecimal;

public class Transaction {
    private int transactionId;
    private long senderAccount; 
    private long receiverAccount;
    private BigDecimal amount;
    private String transactionTime;
    private String remarks;
    private String TransactionRole;

    public Transaction(int transactionId, long senderAccount, long receiverAccount, BigDecimal amount, String transactionTime, String remarks, String TransactionType) {
        this.transactionId = transactionId;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.amount = amount;
        this.transactionTime = transactionTime;
        this.remarks = remarks;
        this.TransactionRole = TransactionType;
    }

    // Getter methods
    public int getTransactionId() {
        return transactionId;
    }

    public long getSenderAccount() {
        return senderAccount;
    }

    
    public long getReceiverAccount() {
        return receiverAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getRemarks() {
        return remarks;
    }

	public String getTransactionType() {
		return TransactionRole;
	}

	
}
