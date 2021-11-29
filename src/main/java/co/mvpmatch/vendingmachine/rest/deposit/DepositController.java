package co.mvpmatch.vendingmachine.rest.deposit;

import co.mvpmatch.vendingmachine.accesscontrol.Secured;
import co.mvpmatch.vendingmachine.contracts.IDepositService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static co.mvpmatch.vendingmachine.VendingMachineApi.API_NAME;
import static co.mvpmatch.vendingmachine.VendingMachineApi.API_V1;

@SuppressWarnings("unused")
@Path(API_NAME + API_V1 + "deposit")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DepositController {

  @Inject
  private IDepositService depositService;

  @POST
  @Secured({IUserService.Role.SELLER})
  public IUserService.User deposit(IDepositService.DepositContext depositContext) {
    return depositService.deposit(depositContext);
  }
}
