package com.assignment.moneytransfer.exceptions.mapper;

import com.assignment.moneytransfer.exceptions.ReferenceIdNotFoundException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ReferenceIdNotFoundExceptionMapper implements ExceptionMapper<ReferenceIdNotFoundException> {
    public Response toResponse(ReferenceIdNotFoundException exception) {
        return Response.status(404)
                .entity(new ErrorMessage(404, exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
