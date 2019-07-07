package com.assignment.moneytransfer.health;

import com.codahale.metrics.health.HealthCheck;
import io.dropwizard.hibernate.SessionFactoryHealthCheck;
import org.hibernate.SessionFactory;

public class MoneyTransferApplicationHealthCheck extends HealthCheck {

    private final SessionFactory sessionFactory;

    public MoneyTransferApplicationHealthCheck(SessionFactory factory) {
        this.sessionFactory = factory;
    }

    @Override
    public Result check() throws Exception {
        return new SessionFactoryHealthCheck(sessionFactory, "SELECT 1").execute();
    }
}
