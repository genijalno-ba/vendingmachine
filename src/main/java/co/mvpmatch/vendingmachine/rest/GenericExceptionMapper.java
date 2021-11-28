package co.mvpmatch.vendingmachine.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
  @Override
  public Response toResponse(Throwable e) {
    Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, e);
    return Response.status(500).entity(ErrorMessage.create("Something went wrong")).type(MediaType.APPLICATION_JSON).build();
  }
}
