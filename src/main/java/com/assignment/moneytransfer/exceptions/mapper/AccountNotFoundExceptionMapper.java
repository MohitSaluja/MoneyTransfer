package com.assignment.moneytransfer.exceptions.mapper;

import com.assignment.moneytransfer.exceptions.AccountNotFoundException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {
    public Response toResponse(AccountNotFoundException exception) {
        return Response.status(500)
                .entity(new ErrorMessage(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
