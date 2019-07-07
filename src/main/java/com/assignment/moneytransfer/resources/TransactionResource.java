package com.assignment.moneytransfer.resources;

import com.assignment.moneytransfer.api.Transaction;
import com.assignment.moneytransfer.api.TransactionResponse;
import com.assignment.moneytransfer.service.ITransactionService;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/transaction")
@Produces(MediaType.APPLICATION_JSON)
public class TransactionResource {

    private final ITransactionService transactionService;

    public TransactionResource(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionResource.class);

    @GET
    @Path("/referenceId/{referenceId}")
    @UnitOfWork
    public TransactionResponse getTransactionsByReferenceId(
            @PathParam("referenceId") String referenceId) {

        LOGGER.info("getTransactionsByReferenceId for referenceId:: {}",  referenceId);
        List<Transaction> transactions = transactionService.getTransactionsByReferenceId(referenceId);
        TransactionResponse response = new TransactionResponse();
        response.setTransactions(transactions);
        return response;
    }

}

