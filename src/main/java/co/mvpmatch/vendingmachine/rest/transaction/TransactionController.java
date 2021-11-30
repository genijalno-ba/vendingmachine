package co.mvpmatch.vendingmachine.rest.transaction;

import co.mvpmatch.vendingmachine.accesscontrol.Secured;
import co.mvpmatch.vendingmachine.contracts.ITransactionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import static co.mvpmatch.vendingmachine.VendingMachineApi.API_NAME;
import static co.mvpmatch.vendingmachine.VendingMachineApi.API_V1;

@SuppressWarnings("unused")
@Path(API_NAME + API_V1)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TransactionController {

  @Inject
  private ITransactionService transactionService;

  @POST
  @Path("buy")
  @Secured()
  public ITransactionService.Transaction buy(ITransactionService.TransactionContext transactionContext) {
    return transactionService.buy(transactionContext);
  }
}
