package co.mvpmatch.vendingmachine.rest.tokensession;

import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static co.mvpmatch.vendingmachine.VendingMachineApi.API_NAME;
import static co.mvpmatch.vendingmachine.VendingMachineApi.API_V1;

@SuppressWarnings("unused")
@Path(API_NAME + API_V1 + "token-session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenSessionController {

  @Inject
  private ITokenSessionService tokenSessionService;

  @GET
  @Path("/{token}")
  public ITokenSessionService.TokenSession getTokenSession(@PathParam("token") String token) {
    return tokenSessionService.readTokenSession(token);
  }

  @POST
  public ITokenSessionService.TokenSession createTokenSession(ITokenSessionService.TokenSessionContext tokenSessionContext) {
    return tokenSessionService.createTokenSession(tokenSessionContext);
  }

  @DELETE
  @Path("/{token}")
  public ITokenSessionService.TokenSession deleteTokenSession(@PathParam("token") String token) {
    return tokenSessionService.deleteTokenSession(token);
  }
}
