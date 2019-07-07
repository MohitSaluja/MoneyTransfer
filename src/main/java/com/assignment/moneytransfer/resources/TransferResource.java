package com.assignment.moneytransfer.resources;

import com.assignment.moneytransfer.api.TransferRequest;
import com.assignment.moneytransfer.api.TransferResponse;
import com.assignment.moneytransfer.service.ITransferService;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/transfer")
@Produces(MediaType.APPLICATION_JSON)
public class TransferResource {

    private final ITransferService transferService;

    public TransferResource(ITransferService transferService) {
        this.transferService = transferService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferResource.class);

    @POST
    @UnitOfWork
    public TransferResponse transferAmount(@Valid TransferRequest transferRequest) {
        LOGGER.info("transferAmount request:: fromAccount: {}, toAccount: {}, amount: {}",
                transferRequest.getFromAccount(), transferRequest.getToAccount(), transferRequest.getAmount());

        TransferResponse transferResponse = transferService.transferAmount(transferRequest);

        LOGGER.info("transferAmount request succeeded:: fromAccount: {}, toAccount: {}, amount: {} with referenceId: {}",
                transferRequest.getFromAccount(), transferRequest.getToAccount(),
                transferRequest.getAmount(),transferResponse.getReferenceId());
        return transferResponse;
    }

}

