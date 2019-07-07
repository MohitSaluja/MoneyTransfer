package com.assignment.moneytransfer.api;

import com.assignment.moneytransfer.core.AccountState;
import com.assignment.moneytransfer.core.AccountType;
import com.assignment.moneytransfer.core.TransactionType;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {

    private String referenceId;
    private Long accountId;
    private AccountType accountType;
    private AccountState accountState;
    private TransactionType transactionType;
    private BigDecimal amount;

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(referenceId, that.referenceId) &&
                Objects.equals(accountId, that.accountId) &&
                accountType == that.accountType &&
                accountState == that.accountState &&
                transactionType == that.transactionType &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(referenceId, accountId, accountType, accountState, transactionType, amount);
    }
}
