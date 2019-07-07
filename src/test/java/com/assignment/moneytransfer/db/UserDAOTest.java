package com.assignment.moneytransfer.db;

import com.assignment.moneytransfer.core.User;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(DropwizardExtensionsSupport.class)
public class UserDAOTest {

    public DAOTestExtension daoTestRule = DAOTestExtension.newBuilder()
            .addEntityClass(User.class)
            .build();

    private UserDAO userDAO;

    @BeforeEach
    public void setUp() throws Exception {
        userDAO = new UserDAO(daoTestRule.getSessionFactory());
    }

    @Test
    public void createUser() {
        final User user = daoTestRule.inTransaction(() -> userDAO.create(new User("Mohit", "Saluja")));
        assertThat(user.getId()).isGreaterThan(0);
        assertThat(user.getFirstName()).isEqualTo("Mohit");
        assertThat(userDAO.findById(user.getId())).isEqualTo(Optional.of(user));
    }

    @Test
    public void findAll() {
        daoTestRule.inTransaction(() -> {
            userDAO.create(new User("Mohit", "Saluja"));
            userDAO.create(new User("Sneha", "Arora"));
            userDAO.create(new User("Ankit", "Saluja"));
        });

        final List<User> users = userDAO.findAll();
        assertThat(users).extracting("firstName").containsOnly("Mohit", "Sneha", "Ankit");
    }

    @Test
    public void handlesNullFirstName() {
        assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() ->
                daoTestRule.inTransaction(() -> userDAO.create(new User(null, "The null"))));
    }
}
