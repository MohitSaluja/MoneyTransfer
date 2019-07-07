package com.assignment.moneytransfer.service.impl;

import com.assignment.moneytransfer.core.Transaction;
import com.assignment.moneytransfer.db.TransactionDAO;
import com.assignment.moneytransfer.exceptions.ReferenceIdNotFoundException;
import com.assignment.moneytransfer.service.ITransactionService;

import java.util.ArrayList;
import java.util.List;

public class TransactionService implements ITransactionService {

    private final TransactionDAO transactionDAO;

    public TransactionService(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    public List<com.assignment.moneytransfer.api.Transaction> getTransactionsByReferenceId(String referenceId) {
        List<Transaction> transactions = transactionDAO.findByReferenceId(referenceId);

        if(transactions.isEmpty()){
            throw new ReferenceIdNotFoundException("Reference ID not found.");
        }

        List<com.assignment.moneytransfer.api.Transaction> response = new ArrayList<>();

        transactions.forEach(each -> {
            com.assignment.moneytransfer.api.Transaction transaction = new com.assignment.moneytransfer.api.Transaction();
            transaction.setReferenceId(each.getReferenceId());
            transaction.setAccountId(each.getAccount().getId());
            transaction.setTransactionType(each.getTransactionType());
            transaction.setAmount(each.getAmount());
            transaction.setAccountState(each.getAccount().getAccountState());
            transaction.setAccountType(each.getAccount().getAccountType());
            response.add(transaction);
        });
        return response;
    }

}
