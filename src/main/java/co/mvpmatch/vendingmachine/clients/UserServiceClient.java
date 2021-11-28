package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.IUserService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UserServiceClient extends AbstractClient<IUserService.User> implements IUserService {

  @Override
  public User createUser(UserContext user) {
    return post("/user", user, User.class);
  }

  @Override
  public User getUserByUsername(String username) {
    return get("/user/" + URLEncoder.encode(username, StandardCharsets.UTF_8), User.class);
  }

  @Override
  public User deleteUser(String username) {
    return delete("/user/" + URLEncoder.encode(username, StandardCharsets.UTF_8), User.class);
  }
}
