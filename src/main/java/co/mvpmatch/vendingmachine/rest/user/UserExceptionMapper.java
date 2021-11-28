package co.mvpmatch.vendingmachine.rest.user;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.rest.ErrorMessage;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class UserExceptionMapper implements ExceptionMapper<IUserService.VendingMachineUserNotFoundException> {
  @Override
  public Response toResponse(IUserService.VendingMachineUserNotFoundException e) {
    Logger.getLogger(UserExceptionMapper.class.getName()).log(Level.SEVERE, null, e);
    return Response.status(500).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build();
  }
}
