package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.NotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class UserServiceTest extends AbstractTest {

  private static IUserService userService;

  @BeforeClass
  public static void setUp() {
    AbstractTest.startServer();
    // create the client
    userService = new UserServiceClient();
  }

  @AfterClass
  public static void tearDown() {
    AbstractTest.shutdownServer();
  }

  @Test
  public void test01_CreateUser() {
    IUserService.UserContext user = new IUserService.UserContext();
    user.setUsername("e2e-usertest-username");
    user.setPassword("postpassword");
    user.setDeposit(BigDecimal.TEN);
    user.setRole(IUserService.Role.SELLER);
    IUserService.User newUser = userService.createUser(user);
    assertEquals("e2e-usertest-username", newUser.getUsername());
    assertEquals(BigDecimal.TEN, newUser.getDeposit());
    assertEquals(IUserService.Role.SELLER, newUser.getRole());
  }

  @Test
  public void test02_GetByUsername() {
    IUserService.User user = userService.getUserByUsername("e2e-usertest-username");
    assertEquals("e2e-usertest-username", user.getUsername());
  }

  @Test
  public void test03_DeleteByUsername() {
    IUserService.User user = userService.deleteUser("e2e-usertest-username");
    assertEquals("e2e-usertest-username", user.getUsername());
    Throwable t = null;
    try {
      userService.getUserByUsername("e2e-usertest-username");
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
  }
}
