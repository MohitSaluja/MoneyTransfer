package com.assignment.moneytransfer;

import com.assignment.moneytransfer.core.Account;
import com.assignment.moneytransfer.core.Transaction;
import com.assignment.moneytransfer.core.User;
import com.assignment.moneytransfer.db.AccountDAO;
import com.assignment.moneytransfer.db.TransactionDAO;
import com.assignment.moneytransfer.exceptions.mapper.AccountNotFoundExceptionMapper;
import com.assignment.moneytransfer.exceptions.mapper.InsufficientBalanceExceptionMapper;
import com.assignment.moneytransfer.exceptions.mapper.InvalidAccountStatusExceptionMapper;
import com.assignment.moneytransfer.exceptions.mapper.OptimisticLockExceptionMapper;
import com.assignment.moneytransfer.exceptions.mapper.ReferenceIdNotFoundExceptionMapper;
import com.assignment.moneytransfer.health.MoneyTransferApplicationHealthCheck;
import com.assignment.moneytransfer.resources.TransactionResource;
import com.assignment.moneytransfer.resources.TransferResource;
import com.assignment.moneytransfer.service.ITransactionService;
import com.assignment.moneytransfer.service.ITransferService;
import com.assignment.moneytransfer.service.impl.TransactionService;
import com.assignment.moneytransfer.service.impl.TransferService;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class MoneyTransferApplication extends Application<MoneyTransferConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MoneyTransferApplication().run(args);
    }

    @Override
    public String getName() {
        return "MoneyTransfer";
    }

    @Override
    public void initialize(final Bootstrap<MoneyTransferConfiguration> bootstrap) {
        bootstrap.addBundle(new MigrationsBundle<MoneyTransferConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(MoneyTransferConfiguration moneyTransferConfiguration) {
                return moneyTransferConfiguration.getDataSourceFactory();
            }
        });
        bootstrap.addBundle(hibernateBundle);
    }

    @Override
    public void run(final MoneyTransferConfiguration configuration,
                    final Environment environment) {
        final AccountDAO accountDAO = new AccountDAO(hibernateBundle.getSessionFactory());
        final TransactionDAO transactionDAO = new TransactionDAO(hibernateBundle.getSessionFactory());

        environment.healthChecks().register(this.getName(),
                new MoneyTransferApplicationHealthCheck(hibernateBundle.getSessionFactory()));

        ITransferService transferService = new TransferService(accountDAO, transactionDAO);
        ITransactionService transactionService = new TransactionService(transactionDAO);

        environment.jersey().register(new TransferResource(transferService));
        environment.jersey().register(new TransactionResource(transactionService));

        environment.jersey().register(new AccountNotFoundExceptionMapper());
        environment.jersey().register(new InvalidAccountStatusExceptionMapper());
        environment.jersey().register(new InsufficientBalanceExceptionMapper());
        environment.jersey().register(new ReferenceIdNotFoundExceptionMapper());
        environment.jersey().register(new OptimisticLockExceptionMapper());
    }

    private final HibernateBundle<MoneyTransferConfiguration> hibernateBundle =
            new HibernateBundle<MoneyTransferConfiguration>(Account.class, User.class, Transaction.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(MoneyTransferConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

}
