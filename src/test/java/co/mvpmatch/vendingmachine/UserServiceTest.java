package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {

  private HttpServer server;
  private UserServiceClient userService;

  @Before
  public void setUp() {
    // start the server
    server = Main.startServer();
    // create the client
    userService = new UserServiceClient();
  }

  @After
  public void tearDown() {
    server.shutdownNow();
  }

  @Test
  public void testGetByUsername() {
    IUserService.User user = userService.getUserByUsername("myusername");
    assertEquals("myusername", user.getUsername());
  }

  @Test
  public void testCreateUser() {
    IUserService.UserContext user = new IUserService.UserContext();
    user.setUsername("postusername");
    user.setPassword("postpassword");
    user.setDeposit(BigDecimal.TEN);
    user.setRole(IUserService.Role.SELLER);
    IUserService.User newUser = userService.createUser(user);
    assertEquals("postusername", newUser.getUsername());
    assertEquals(BigDecimal.TEN, newUser.getDeposit());
    assertEquals(IUserService.Role.SELLER, newUser.getRole());
  }
}
