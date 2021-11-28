package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.user.User;
import org.jvnet.hk2.annotations.Service;

@Service
public class UserAdapter {

  public User fromContext(IUserService.UserContext userContext) {
    User user = new User();
    user.setUsername(userContext.getUsername());
    user.setPassword(userContext.getPassword());
    user.setDeposit(userContext.getDeposit());
    user.setRole(userContext.getRole());
    return user;
  }

  public IUserService.User fromDb(User user) {
    return new IUserService.User.Builder()
        .setUsername(user.getUsername())
        .setDeposit(user.getDeposit())
        .setRole(user.getRole())
        .build();
  }
}
