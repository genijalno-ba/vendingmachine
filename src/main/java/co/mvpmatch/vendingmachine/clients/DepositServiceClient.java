package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.IDepositService;
import co.mvpmatch.vendingmachine.contracts.IUserService;

public class DepositServiceClient extends AbstractClient<IUserService.User> implements IDepositService {

  @Override
  public IUserService.User deposit(DepositContext depositContext) {
    return post("deposit", depositContext, IUserService.User.class);
  }

  @Override
  public IUserService.User reset() {
    return delete("reset", IUserService.User.class);
  }
}
