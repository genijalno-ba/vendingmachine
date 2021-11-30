package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.DepositServiceClient;
import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IDepositService;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotAuthorizedException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;

import static co.mvpmatch.vendingmachine.contracts.IDepositService.COIN_10;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DepositServiceTest extends VendingMachineApiTest {

  private static IDepositService depositService;
  private static IUserService userService;
  private static ITokenSessionService tokenSessionService;
  private static final BigDecimal depositAmount = COIN_10;
  private static final IDepositService.DepositContext depositContext = IDepositService.DepositContext
      .create(depositAmount);

  @BeforeClass
  public static void setUp() {
    VendingMachineApiTest.startServer();
    // create the client
    depositService = new DepositServiceClient();
    userService = new UserServiceClient();
    tokenSessionService = new TokenSessionClient();
  }

  protected void setAuthToken(String authToken) {
    ((DepositServiceClient) depositService).setAuthToken(authToken);
    ((UserServiceClient) userService).setAuthToken(authToken);
    ((TokenSessionClient) tokenSessionService).setAuthToken(authToken);
  }

  protected void resetAuthToken() {
    ((DepositServiceClient) depositService).setAuthToken(null);
    ((UserServiceClient) userService).setAuthToken(null);
    ((TokenSessionClient) tokenSessionService).setAuthToken(null);
  }

  @AfterClass
  public static void tearDown() {
    VendingMachineApiTest.shutdownServer();
  }

  @Test
  public void test00_SetupUsers() {
    createUser(userService, "e2e-deposittest-buyer-username", "testuser", IUserService.Role.BUYER);
    createUser(userService, "e2e-deposittest-seller-username", "testuser", IUserService.Role.SELLER);
  }

  @Test
  public void test01_DepositNotAllowedForAnonymous() {
    IDepositService.DepositContext depositContext = IDepositService.DepositContext
        .create(depositAmount);
    String authToken = ((UserServiceClient) userService).getAuthToken();
    assertNull(authToken);
    Throwable t = null;
    try {
      depositService.deposit(depositContext);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotAuthorizedException);
  }

  @Test
  public void test02_DepositNotAllowedForSeller() {
    loginUser(tokenSessionService, "e2e-deposittest-seller-username", "testuser");
    String authToken = ((UserServiceClient) userService).getAuthToken();
    assertNotNull(authToken);
    Throwable t = null;
    try {
      depositService.deposit(depositContext);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof ForbiddenException);
  }

  @Test
  public void test03_DepositAllowedForBuyer() {
    loginUser(tokenSessionService, "e2e-deposittest-buyer-username", "testuser");
    String authToken = ((UserServiceClient) userService).getAuthToken();
    assertNotNull(authToken);
    IUserService.User buyer = userService.getUserByUsername("e2e-deposittest-buyer-username");
    IUserService.User deposit = depositService.deposit(depositContext);
    assertEquals(buyer.getUsername(), deposit.getUsername());
    assertEquals(buyer.getDeposit().add(depositAmount), deposit.getDeposit());
  }

  @Test
  public void test04_IllegalDepositNotAllowed() {
    // check for BUYER illegal deposit
    IDepositService.DepositContext illegalDepositContext = IDepositService.DepositContext
        .create(BigDecimal.valueOf(123));
    Throwable t = null;
    try {
      depositService.deposit(illegalDepositContext);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof BadRequestException);
  }

  @Test
  public void test05_DeleteUsers() {
    loginUser(tokenSessionService, "e2e-deposittest-buyer-username", "testuser");
    deleteUser(userService, tokenSessionService, "e2e-deposittest-buyer-username");
    loginUser(tokenSessionService, "e2e-deposittest-seller-username", "testuser");
    deleteUser(userService, tokenSessionService, "e2e-deposittest-seller-username");
  }
}
