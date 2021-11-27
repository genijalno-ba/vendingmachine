package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import org.jvnet.hk2.annotations.Service;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Service
public class UserService implements IUserService {

  @Override
  public User createUser(UserContext userContext) {
    //TODO: persistence
    return new User.Builder()
        .setUsername(userContext.getUsername())
        .setDeposit(userContext.getDeposit())
        .setRole(userContext.getRole())
        .build();
  }

  @Override
  public User getUserByUsername(String username) {
    //TODO: persistence
    return new User.Builder()
        .setUsername(username)
        .setDeposit(BigDecimal.TEN)
        .setRole(Role.SELLER)
        .build();
  }
}
