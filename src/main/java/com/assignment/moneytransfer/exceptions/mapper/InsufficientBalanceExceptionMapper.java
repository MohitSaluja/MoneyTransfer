package com.assignment.moneytransfer.exceptions.mapper;

import com.assignment.moneytransfer.exceptions.InsufficientBalanceException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InsufficientBalanceExceptionMapper implements ExceptionMapper<InsufficientBalanceException> {
    public Response toResponse(InsufficientBalanceException exception) {
        return Response.status(500)
                .entity(new ErrorMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
