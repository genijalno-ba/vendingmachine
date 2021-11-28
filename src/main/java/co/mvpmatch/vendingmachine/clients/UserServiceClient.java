package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.IUserService;

public class UserServiceClient extends AbstractClient<IUserService.User> implements IUserService {

  @Override
  public User createUser(UserContext user) {
    return post("/user", user, User.class);
  }

  @Override
  public User getUserByUsername(String username) {
    return get("/user/" + username, User.class);
  }

  @Override
  public User deleteUser(String username) {
    return delete("/user/" + username, User.class);
  }
}
