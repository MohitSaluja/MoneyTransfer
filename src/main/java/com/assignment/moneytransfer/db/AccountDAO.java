package com.assignment.moneytransfer.db;

import com.assignment.moneytransfer.core.Account;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class AccountDAO extends AbstractDAO<Account> {
    public AccountDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Account create(Account account) {
        return persist(account);
    }

    public Account update(Account account) {
        return persist(account);
    }

    public List<Account> findAll() {
        return list((Query<Account>) namedQuery("com.assignment.moneytransfer.core.Account.findAll"));
    }
}
