package com.assignment.moneytransfer.exceptions.mapper;

import io.dropwizard.jersey.errors.ErrorMessage;

import javax.persistence.OptimisticLockException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OptimisticLockExceptionMapper implements ExceptionMapper<OptimisticLockException> {
    public Response toResponse(OptimisticLockException exception) {
        return Response.status(500)
                .entity(new ErrorMessage("Account has been updated by some other transaction, please re-try."))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
