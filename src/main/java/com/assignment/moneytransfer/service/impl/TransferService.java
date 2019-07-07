package com.assignment.moneytransfer.service.impl;

import com.assignment.moneytransfer.api.TransferRequest;
import com.assignment.moneytransfer.api.TransferResponse;
import com.assignment.moneytransfer.core.Account;
import com.assignment.moneytransfer.core.AccountState;
import com.assignment.moneytransfer.core.Transaction;
import com.assignment.moneytransfer.core.TransactionType;
import com.assignment.moneytransfer.db.AccountDAO;
import com.assignment.moneytransfer.db.TransactionDAO;
import com.assignment.moneytransfer.exceptions.AccountNotFoundException;
import com.assignment.moneytransfer.exceptions.InsufficientBalanceException;
import com.assignment.moneytransfer.exceptions.InvalidAccountStatusException;
import com.assignment.moneytransfer.service.ITransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.function.Supplier;

public class TransferService implements ITransferService {

    private final AccountDAO accountDAO;
    private final TransactionDAO transactionDAO;

    public TransferService(AccountDAO peopleDAO, TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
        this.accountDAO = peopleDAO;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferService.class);

    @Override
    public TransferResponse transferAmount(TransferRequest transferRequest) {

        Account sourceAccount = accountDAO.findById(transferRequest.getFromAccount())
                .orElseThrow(getAccountNotFoundExceptionSupplier(transferRequest.getFromAccount()));

        validateAccountState(sourceAccount);

        validateAccountBalance(transferRequest.getAmount(), sourceAccount);

        Account targetAccount = accountDAO.findById(transferRequest.getToAccount())
                .orElseThrow(getAccountNotFoundExceptionSupplier(transferRequest.getToAccount()));

        validateAccountState(targetAccount);

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(transferRequest.getAmount()));
        targetAccount.setBalance(targetAccount.getBalance().add(transferRequest.getAmount()));

        String referenceId = UUID.randomUUID().toString();

        Transaction debitTransaction = createTransactionObject(referenceId, sourceAccount,
                transferRequest.getAmount(), TransactionType.DEBIT);

        Transaction creditTransaction = createTransactionObject(referenceId, targetAccount,
                transferRequest.getAmount(), TransactionType.CREDIT);

        accountDAO.update(sourceAccount);
        accountDAO.update(targetAccount);

        transactionDAO.create(debitTransaction);
        transactionDAO.create(creditTransaction);

        return new TransferResponse(referenceId);
    }

    private Supplier<AccountNotFoundException> getAccountNotFoundExceptionSupplier(Long accountId) {
        return () -> new AccountNotFoundException(
                MessageFormat.format("Account {0} not found.", accountId));
    }

    private void validateAccountBalance(BigDecimal amount, Account account) {
        if (account.getBalance().compareTo(amount) < 0) {
            LOGGER.info("Account ID {} has insufficient balance.", account.getId());
            throw new InsufficientBalanceException(
                    MessageFormat.format("Account ID {0} has insufficient balance.",
                            account.getId()));
        }
    }

    private void validateAccountState(Account account) {
        if (!account.getAccountState().equals(AccountState.ACTIVE)) {
            LOGGER.info("Account ID {} is in {} state.", account.getId(),
                    account.getAccountState());
            throw new InvalidAccountStatusException(
                    MessageFormat.format("Account ID {0} is in {1} state.", account.getId(),
                            account.getAccountState()));
        }
    }

    private Transaction createTransactionObject(String referenceId, Account account,
                                                BigDecimal amount, TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setReferenceId(referenceId);
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        return transaction;
    }

}
