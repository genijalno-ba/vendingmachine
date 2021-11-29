package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.time.Instant;

import static co.mvpmatch.vendingmachine.accesscontrol.IAuthenticationTokenIssuer.ONE_DAY;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenSessionServiceTest extends AbstractTest {

  private static ITokenSessionService tokenSessionService;
  private static IUserService userService;
  private static IUserService.User currentUser;

  @BeforeClass
  public static void setUp() {
    AbstractTest.startServer();
    // create the client
    tokenSessionService = new TokenSessionClient();
    userService = new UserServiceClient();
  }

  private void setAuthToken(String authToken) {
    ((UserServiceClient) userService).setAuthToken(authToken);
    ((TokenSessionClient) tokenSessionService).setAuthToken(authToken);
  }

  @AfterClass
  public static void tearDown() {
    AbstractTest.shutdownServer();
  }

  @Test
  public void test01_createUser() {
    IUserService.UserContext user = new IUserService.UserContext();
    user.setUsername("e2e-tokensessiontest-username");
    user.setPassword("testuser");
    user.setDeposit(BigDecimal.TEN);
    user.setRole(IUserService.Role.BUYER);
    currentUser = userService.createUser(user);
  }

  @Test
  public void test02_createTokenSession() {
    final Instant NOW = Instant.now();
    ITokenSessionService.TokenSessionContext tokenSessionContext = ITokenSessionService.TokenSessionContext
        .create("e2e-tokensessiontest-username", "testuser");
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.createTokenSession(tokenSessionContext);
    assertEquals("e2e-tokensessiontest-username", tokenSession.getUsername());
    assertNotNull(tokenSession.getToken());
    assertTrue(tokenSession.getValidUntil().toInstant()
        .isAfter(NOW.plus(ONE_DAY)));
    assertNotNull(tokenSession);
    setAuthToken(tokenSession.getToken());
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
    IUserService.User user = userService.deleteUser(currentUser.getUsername());
    assertNotNull(user);
    Throwable t = null;
    try {
      userService.getUserByUsername(user.getUsername());
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
  }
}
