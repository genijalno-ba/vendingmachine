package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.NotFoundException;
import org.glassfish.grizzly.http.server.HttpServer;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public abstract class VendingMachineApiTest {

  protected static HttpServer server;

  protected static void startServer() {
    server = VendingMachineApi.startServer();
  }

  protected static void shutdownServer() {
    server.shutdownNow();
  }

  protected abstract void setAuthToken(String authToken);

  protected abstract void resetAuthToken();

  protected void createUser(IUserService userService, String username, String password, IUserService.Role role) {
    IUserService.UserContext user = new IUserService.UserContext();
    user.setUsername(username);
    user.setPassword(password);
    user.setDeposit(BigDecimal.TEN);
    user.setRole(role);
    IUserService.User newUser = userService.createUser(user);
    assertEquals(username, newUser.getUsername());
    assertEquals(BigDecimal.TEN, newUser.getDeposit());
    assertEquals(role, newUser.getRole());
  }

  protected void loginUser(ITokenSessionService tokenSessionService, String username, String password) {
    ITokenSessionService.TokenSessionContext tokenSessionContext = ITokenSessionService.TokenSessionContext
        .create(username, password);
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.createTokenSession(tokenSessionContext);
    assertNotNull(tokenSession);
    assertNotNull(tokenSession.getToken());
    assertNotEquals("", tokenSession.getToken());
    assertEquals(username, tokenSession.getUsername());
    setAuthToken(tokenSession.getToken());
  }

  protected void logoutUser() {
    resetAuthToken();
  }

  protected void deleteUser(IUserService userService, ITokenSessionService tokenSessionService, String username) {
    IUserService.User user = userService.deleteUser(username);
    assertEquals(username, user.getUsername());
    Throwable t = null;
    try {
      userService.getUserByUsername(username);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotAuthorizedException);
    try {
      // make sure token was removed with user
      tokenSessionService.readTokenSession(((TokenSessionClient) tokenSessionService).getAuthToken());
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
  }

}
