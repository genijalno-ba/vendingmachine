package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.NotFoundException;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest {

  private HttpServer server;
  private IUserService userService;

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
  public void test01_CreateUser() {
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

  @Test
  public void test_02_GetByUsername() {
    IUserService.User user = userService.getUserByUsername("postusername");
    assertEquals("postusername", user.getUsername());
  }

  @Test
  public void test_03_DeleteByUsername() {
    IUserService.User user = userService.deleteUser("postusername");
    assertEquals("postusername", user.getUsername());
    Throwable t = null;
    try {
      IUserService.User deletedUser = userService.getUserByUsername("postusername");
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
  }
}
