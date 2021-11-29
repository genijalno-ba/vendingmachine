package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceTest extends VendingMachineApiTest {

  private static IUserService userService;
  private static ITokenSessionService tokenSessionService;

  @BeforeClass
  public static void setUp() {
    VendingMachineApiTest.startServer();
    // create the client
    userService = new UserServiceClient();
    tokenSessionService = new TokenSessionClient();
  }

  protected void setAuthToken(String authToken) {
    ((UserServiceClient) userService).setAuthToken(authToken);
    ((TokenSessionClient) tokenSessionService).setAuthToken(authToken);
  }

  @Override
  protected void resetAuthToken() {
    ((UserServiceClient) userService).setAuthToken(null);
    ((TokenSessionClient) tokenSessionService).setAuthToken(null);
  }

  @AfterClass
  public static void tearDown() {
    VendingMachineApiTest.shutdownServer();
  }

  @Test
  public void test01_CRUDUser() {
    createUser(userService, "e2e-usertest-username", "testuser", IUserService.Role.SELLER);
    loginUser(tokenSessionService, "e2e-usertest-username", "testuser");
    IUserService.User user = userService.getUserByUsername("e2e-usertest-username");
    assertEquals("e2e-usertest-username", user.getUsername());
    deleteUser(userService, tokenSessionService, "e2e-usertest-username");
  }
}
