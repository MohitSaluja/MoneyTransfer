package com.assignment.moneytransfer.db;

import com.assignment.moneytransfer.core.Transaction;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class TransactionDAO extends AbstractDAO<Transaction> {
    public TransactionDAO(SessionFactory factory) {
        super(factory);
    }

    public Optional<Transaction> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public Transaction create(Transaction transaction) {
        return persist(transaction);
    }

    public List<Transaction> findAll() {
        return list((Query<Transaction>) namedQuery("com.assignment.moneytransfer.core.Transaction.findAll"));
    }

    public List<Transaction> findByReferenceId(String referenceId) {
        return list((Query<Transaction>) namedQuery(
                "com.assignment.moneytransfer.core.Transaction.findByReferenceId")
                .setParameter("referenceId", referenceId));
    }

}
