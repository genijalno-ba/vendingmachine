package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.ProductServiceClient;
import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IProductService;
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

import static co.mvpmatch.vendingmachine.accesscontrol.IAuthenticationTokenIssuer.ONE_DAY;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductServiceTest extends AbstractTest {

  private static IProductService productService;
  private static IUserService userService;
  private static ITokenSessionService tokenSessionService;
  private static IProductService.Product currentProduct;

  @BeforeClass
  public static void setUp() {
    AbstractTest.startServer();
    // create the client
    productService = new ProductServiceClient();
    userService = new UserServiceClient();
    tokenSessionService = new TokenSessionClient();
  }

  private void setAuthToken(String authToken) {
    ((UserServiceClient) userService).setAuthToken(authToken);
    ((ProductServiceClient) productService).setAuthToken(authToken);
    ((TokenSessionClient) tokenSessionService).setAuthToken(authToken);
  }

  @AfterClass
  public static void tearDown() {
    AbstractTest.shutdownServer();
  }

  @Test
  public void test01_CreateUser() {
    IUserService.UserContext user = new IUserService.UserContext();
    user.setUsername("e2e-producttest-username");
    user.setPassword("testuser");
    user.setDeposit(BigDecimal.TEN);
    user.setRole(IUserService.Role.SELLER);
    IUserService.User newUser = userService.createUser(user);
    assertEquals("e2e-producttest-username", newUser.getUsername());
    assertEquals(BigDecimal.TEN, newUser.getDeposit());
    assertEquals(IUserService.Role.SELLER, newUser.getRole());
  }

  @Test
  public void test02_createTokenSession() {
    final Instant NOW = Instant.now();
    ITokenSessionService.TokenSessionContext tokenSessionContext = ITokenSessionService.TokenSessionContext
        .create("e2e-producttest-username", "testuser");
    ITokenSessionService.TokenSession tokenSession = tokenSessionService.createTokenSession(tokenSessionContext);
    assertEquals("e2e-producttest-username", tokenSession.getUsername());
    assertNotNull(tokenSession.getToken());
    assertTrue(tokenSession.getValidUntil().toInstant()
        .isAfter(NOW.plus(ONE_DAY)));
    assertNotNull(tokenSession);
    setAuthToken(tokenSession.getToken());
  }

  @Test
  public void test03_CreateProduct() {
    IProductService.ProductContext product = IProductService.ProductContext.create(
        3,
        BigDecimal.TEN,
        "Snickers",
        "e2e-producttest-username"
    );
    currentProduct = productService.createProduct(product);
    assertEquals("Snickers", currentProduct.getProductName());
    assertEquals(3, currentProduct.getAmountAvailable());
    assertEquals(BigDecimal.TEN, currentProduct.getCost());
    assertEquals("e2e-producttest-username", currentProduct.getSellerId());
  }

  @Test
  public void test04_DeleteProduct() {
    assertNotNull(currentProduct);
    productService.deleteProduct(currentProduct.getProductId());
    Throwable t = null;
    try {
      productService.getByProductId(currentProduct.getProductId());
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
  }

  @Test
  public void test05_DeleteUser() {
    IUserService.User user = userService.deleteUser("e2e-producttest-username");
    assertNotNull(user);
    Throwable t = null;
    try {
      userService.getUserByUsername("e2e-producttest-username");
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof NotFoundException);
  }
}
