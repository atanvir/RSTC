package com.example.rstc.model.response;

public class CardModel {
    private String amount;
    private String guid;
    private String TransactionId;
    private String TransactionAmt;
    private String TransactionDate;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getTransactionId() {
        return TransactionId;
    }

    public void setTransactionId(String transactionId) {
        TransactionId = transactionId;
    }

    public String getTransactionAmt() {
        return TransactionAmt;
    }

    public void setTransactionAmt(String transactionAmt) {
        TransactionAmt = transactionAmt;
    }

    public String getTransactionDate() {
        return TransactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        TransactionDate = transactionDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
