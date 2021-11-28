package co.mvpmatch.vendingmachine.rest.user;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static co.mvpmatch.vendingmachine.Main.API_NAME;
import static co.mvpmatch.vendingmachine.Main.API_V1;

@Path(API_NAME + API_V1 + "user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

  @Inject
  private IUserService userService;

  @GET
  @Path("/{username}")
  public IUserService.User getUserByUsername(@PathParam("username") String username) {
    return userService.getUserByUsername(username);
  }

  @POST
  public IUserService.User createUser(IUserService.UserContext userContext) {
    return userService.createUser(userContext);
  }

  @DELETE
  @Path("/{username}")
  public IUserService.User deleteUser(@PathParam("username") String username) {
    return userService.deleteUser(username);
  }
}
