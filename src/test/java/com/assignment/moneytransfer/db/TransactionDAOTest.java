package com.assignment.moneytransfer.db;


import com.assignment.moneytransfer.core.Account;
import com.assignment.moneytransfer.core.AccountState;
import com.assignment.moneytransfer.core.AccountType;
import com.assignment.moneytransfer.core.Transaction;
import com.assignment.moneytransfer.core.TransactionType;
import com.assignment.moneytransfer.core.User;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class TransactionDAOTest {

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(User.class)
            .addEntityClass(Account.class)
            .addEntityClass(Transaction.class)
            .build();

    private UserDAO userDAO;
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;

    @BeforeEach
    public void setUp() throws Exception {
        userDAO = new UserDAO(daoTestRule.getSessionFactory());
        accountDAO = new AccountDAO(daoTestRule.getSessionFactory());
        transactionDAO = new TransactionDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void createTransaction() {
        final User user = daoTestRule.inTransaction(() -> userDAO.create(new User("Mohit", "Saluja")));
        final Account account = daoTestRule.inTransaction(() -> accountDAO.create(createAccountObject(user)));

        String referenceId = UUID.randomUUID().toString();
        final Transaction transaction = daoTestRule.inTransaction(() -> {
            return transactionDAO.create(
                    createTransactionObject(referenceId, account, BigDecimal.valueOf(5.5), TransactionType.CREDIT));
        });
        assertThat(transaction.getAmount()).isEqualTo(BigDecimal.valueOf(5.5));
        assertThat(transaction.getAccount()).isEqualTo(account);
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.CREDIT);
        assertThat(transaction.getReferenceId()).isEqualTo(referenceId);

    }

    @Test
    public void findTransactionsByReferenceId() {
        final User user = daoTestRule.inTransaction(() -> userDAO.create(new User("Mohit", "Saluja")));
        final Account account = daoTestRule.inTransaction(() -> accountDAO.create(createAccountObject(user)));

        String referenceId = UUID.randomUUID().toString();
        final Transaction transaction1 = daoTestRule.inTransaction(() -> {
            return transactionDAO.create(
                    createTransactionObject(referenceId, account, BigDecimal.valueOf(5.5), TransactionType.CREDIT));
        });

        final Transaction transaction2 = daoTestRule.inTransaction(() -> {
            return transactionDAO.create(
                    createTransactionObject(referenceId, account, BigDecimal.valueOf(2.5), TransactionType.DEBIT));
        });


        final List<Transaction> transactions = daoTestRule.inTransaction(() -> {
            return transactionDAO.findByReferenceId(referenceId);
        });

        assertThat(transactions).contains(transaction1);
        assertThat(transactions).contains(transaction2);
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

    private Account createAccountObject(User userMohit) {
        Account account = new Account();
        account.setUser(userMohit);
        account.setBalance(BigDecimal.valueOf(10.0));
        account.setAccountState(AccountState.ACTIVE);
        account.setAccountType(AccountType.SAVINGS);
        return account;
    }

}
