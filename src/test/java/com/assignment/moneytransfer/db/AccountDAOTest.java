package com.assignment.moneytransfer.db;


import com.assignment.moneytransfer.core.Account;
import com.assignment.moneytransfer.core.AccountState;
import com.assignment.moneytransfer.core.AccountType;
import com.assignment.moneytransfer.core.User;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AccountDAOTest {

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(User.class)
            .addEntityClass(Account.class)
            .build();

    private UserDAO userDAO;
    private AccountDAO accountDAO;

    @BeforeEach
    public void setUp() throws Exception {
        userDAO = new UserDAO(daoTestRule.getSessionFactory());
        accountDAO = new AccountDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void createAccount() {
        final User user = daoTestRule.inTransaction(() -> userDAO.create(new User("Mohit", "Saluja")));
        Account accountObject = createAccountObject(user);
        final Account account = daoTestRule.inTransaction(() -> {
            return accountDAO.create(accountObject);
        });

        assertThat(account.getId()).isGreaterThan(0);
        assertThat(account.getVersionId()).isEqualTo(0);
        assertThat(account.getBalance()).isEqualTo(accountObject.getBalance());
        assertThat(account.getAccountState()).isEqualTo(accountObject.getAccountState());
        assertThat(account.getAccountType()).isEqualTo(accountObject.getAccountType());
        assertThat(account.getUser()).isEqualTo(accountObject.getUser());

        assertThat(accountDAO.findById(account.getId())).isEqualTo(Optional.of(account));

        account.setBalance(BigDecimal.valueOf(5.5));
        Long versionId = account.getVersionId();
        final Account updatedAccount = daoTestRule.inTransaction(() -> accountDAO.update(account));
        assertThat(updatedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(5.5));
        assertThat(updatedAccount.getVersionId()).isEqualTo(versionId + 1);

    }

    @Test
    public void getAccount() {
        final User user = daoTestRule.inTransaction(() -> userDAO.create(new User("Mohit", "Saluja")));
        Account accountObject = createAccountObject(user);
        final Account account = daoTestRule.inTransaction(() -> {
            return accountDAO.create(accountObject);
        });

        Optional<Account> accountFromDB = accountDAO.findById(account.getId());
        assertThat(accountFromDB).isEqualTo(Optional.of(account));
        assertThat(accountFromDB.get().getBalance()).isEqualTo(account.getBalance());
        assertThat(accountFromDB.get().getUser()).isEqualTo(account.getUser());
        assertThat(accountFromDB.get().getAccountType()).isEqualTo(account.getAccountType());
        assertThat(accountFromDB.get().getAccountState()).isEqualTo(account.getAccountState());

    }

    @Test
    public void updateAccount() {
        final User user = daoTestRule.inTransaction(() -> userDAO.create(new User("Mohit", "Saluja")));
        final Account account = daoTestRule.inTransaction(() -> accountDAO.create(createAccountObject(user)));

        account.setBalance(BigDecimal.valueOf(5.5));
        Long versionId = account.getVersionId();
        final Account updatedAccount = daoTestRule.inTransaction(() -> accountDAO.update(account));

        assertThat(updatedAccount.getBalance()).isEqualTo(BigDecimal.valueOf(5.5));
        assertThat(updatedAccount.getVersionId()).isEqualTo(versionId + 1);

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
