package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.NotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;
import java.time.Instant;

import static co.mvpmatch.vendingmachine.contracts.ITokenSessionService.ONE_DAY;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TokenSessionServiceTest extends AbstractTest {

  private static ITokenSessionService tokenSessionService;
  private static IUserService userService;
  private static ITokenSessionService.TokenSession currentTokenSession;
  private static IUserService.User currentUser;

  @BeforeClass
  public static void setUp() {
    AbstractTest.startServer();
    // create the client
    tokenSessionService = new TokenSessionClient();
    userService = new UserServiceClient();
    currentUser = createUser();
    currentTokenSession = createTokenSession();
  }

  @AfterClass
  public static void tearDown() {
    AbstractTest.shutdownServer();
  }

  public static IUserService.User createUser() {
    IUserService.UserContext user = new IUserService.UserContext();
    user.setUsername("e2e-tokensessiontest-username");
    user.setPassword("postpassword");
    user.setDeposit(BigDecimal.TEN);
    user.setRole(IUserService.Role.BUYER);
    return userService.createUser(user);
  }

  public static ITokenSessionService.TokenSession createTokenSession() {
    final Instant NOW = Instant.now();
    ITokenSessionService.TokenSessionContext tokenSessionContext = ITokenSessionService.TokenSessionContext
        .create("e2e-tokensessiontest-username");
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.createTokenSession(tokenSessionContext);
    assertEquals("e2e-tokensessiontest-username", tokenSession.getUsername());
    assertNotNull(tokenSession.getToken());
    assertTrue(tokenSession.getValidUntil().toInstant()
        .isAfter(NOW.plus(ONE_DAY)));
    assertNotNull(tokenSession);
    return tokenSession;
  }

  @Test
  public void test03_GetByToken() {
    assertNotNull(currentTokenSession);
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.readTokenSession(currentTokenSession.getToken());
    assertEquals("e2e-tokensessiontest-username", tokenSession.getUsername());
    assertEquals(currentTokenSession.getToken(), tokenSession.getToken());
    assertEquals(currentTokenSession.getValidUntil(), tokenSession.getValidUntil());
  }

  @Test
  public void test04_DeleteByToken() {
    assertNotNull(currentTokenSession);
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.deleteTokenSession(currentTokenSession.getToken());
    assertEquals("e2e-tokensessiontest-username", tokenSession.getUsername());
    assertEquals(currentTokenSession.getToken(), tokenSession.getToken());
    assertTrue(tokenSession.getValidUntil().toInstant().isBefore(Instant.now()));
    Throwable t = null;
    try {
      tokenSessionService.readTokenSession(currentTokenSession.getToken());
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
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
