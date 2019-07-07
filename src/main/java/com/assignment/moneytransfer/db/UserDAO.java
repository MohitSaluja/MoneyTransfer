package com.assignment.moneytransfer.db;

import com.assignment.moneytransfer.core.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDAO extends AbstractDAO<User> {
    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public User create(User user) {
        return persist(user);
    }

    public User update(User user) {
        return persist(user);
    }

    public List<User> findAll() {
        return list((Query<User>) namedQuery("com.assignment.moneytransfer.core.User.findAll"));
    }
}
