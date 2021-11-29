package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.ForbiddenException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenSessionServiceTest extends VendingMachineApiTest {

  private static ITokenSessionService tokenSessionService;
  private static IUserService userService;

  @BeforeClass
  public static void setUp() {
    VendingMachineApiTest.startServer();
    // create the client
    tokenSessionService = new TokenSessionClient();
    userService = new UserServiceClient();
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
  public void test01_createUser() {
    createUser(userService, "e2e-tokensessiontest-username", "testuser", IUserService.Role.BUYER);
  }

  @Test
  public void test02_createTokenSession() {
    loginUser(tokenSessionService, "e2e-tokensessiontest-username", "testuser");
  }

  @Test
  public void test03_createTokenSessionWrongPassword() {
    ITokenSessionService.TokenSessionContext tokenSessionContext = ITokenSessionService.TokenSessionContext
        .create("e2e-tokensessiontest-username", "wrongpassword");
    Throwable t = null;
    try {
      tokenSessionService.createTokenSession(tokenSessionContext);
    } catch (Exception e) {
      e.printStackTrace();
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof ForbiddenException);
  }

  @Test
  public void test04_GetByToken() {
    String authToken = ((UserServiceClient) userService).getAuthToken();
    assertNotNull(authToken);
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.readTokenSession(authToken);
    assertEquals("e2e-tokensessiontest-username", tokenSession.getUsername());
    assertEquals(authToken, tokenSession.getToken());
  }

  @Test
  public void test05_DeleteUser() {
    deleteUser(userService, tokenSessionService, "e2e-tokensessiontest-username");
  }
}
