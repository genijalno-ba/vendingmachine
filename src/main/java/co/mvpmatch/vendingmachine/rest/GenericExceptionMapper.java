package co.mvpmatch.vendingmachine.rest;

import co.mvpmatch.vendingmachine.contracts.IUserService;
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
    if (e instanceof IUserService.VendingMachineUserNotFoundException) {
      return Response.status(404).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    if (e instanceof IUserService.VendingMachineUserInternalErrorException) {
      return Response.status(500).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
    }
    return Response.status(500).entity(ErrorMessage.create("Something went wrong")).type(MediaType.APPLICATION_JSON).build();
  }
}
