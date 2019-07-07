package com.assignment.moneytransfer.resources;

import com.assignment.moneytransfer.api.Transaction;
import com.assignment.moneytransfer.api.TransactionResponse;
import com.assignment.moneytransfer.service.impl.TransactionService;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link TransactionResource}.
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class TransactionResourceTest {
    private static final TransactionService SERVICE = mock(TransactionService.class);

    public static final ResourceExtension RULE = ResourceExtension.builder()
            .addResource(new TransactionResource(SERVICE))
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .build();
    private List<Transaction> transactions;

    @BeforeEach
    public void setup() {
        Transaction transaction = new Transaction();
        transaction.setAccountId(1L);
        transactions = new ArrayList<>();
        transactions.add(transaction);
    }

    @AfterEach
    public void tearDown() {
        reset(SERVICE);
    }

    @Test
    public void getTransactionSuccess() {

        String referenceId = UUID.randomUUID().toString();

        when(SERVICE.getTransactionsByReferenceId(referenceId)).thenReturn(transactions);

        TransactionResponse found = RULE.target("/transaction/referenceId/" + referenceId).request().get(TransactionResponse.class);

        assertThat(found.getTransactions()).isEqualTo(transactions);
        assertThat(found.getTransactions().get(0)).isEqualTo(transactions.get(0));
        assertThat(found.getTransactions().get(0).getAccountId()).isEqualTo(transactions.get(0).getAccountId());
        verify(SERVICE).getTransactionsByReferenceId(referenceId);
    }

}

