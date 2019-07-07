package com.assignment.moneytransfer.service;


import com.assignment.moneytransfer.api.Transaction;

import java.util.List;

public interface ITransactionService {
    List<Transaction> getTransactionsByReferenceId(String referenceId);
}
