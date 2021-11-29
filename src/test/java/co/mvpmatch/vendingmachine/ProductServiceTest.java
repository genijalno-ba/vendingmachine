package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.ProductServiceClient;
import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.ForbiddenException;
import jakarta.ws.rs.NotFoundException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Ignore
public class ProductServiceTest extends VendingMachineApiTest {

  private static IProductService productService;
  private static IUserService userService;
  private static ITokenSessionService tokenSessionService;
  private static IProductService.Product currentProduct;

  @BeforeClass
  public static void setUp() {
    VendingMachineApiTest.startServer();
    // create the client
    productService = new ProductServiceClient();
    userService = new UserServiceClient();
    tokenSessionService = new TokenSessionClient();
  }

  protected void setAuthToken(String authToken) {
    ((UserServiceClient) userService).setAuthToken(authToken);
    ((ProductServiceClient) productService).setAuthToken(authToken);
    ((TokenSessionClient) tokenSessionService).setAuthToken(authToken);
  }

  protected void resetAuthToken() {
    ((UserServiceClient) userService).setAuthToken(null);
    ((ProductServiceClient) productService).setAuthToken(null);
    ((TokenSessionClient) tokenSessionService).setAuthToken(null);
  }

  @AfterClass
  public static void tearDown() {
    VendingMachineApiTest.shutdownServer();
  }

  @Test
  public void test00_SetupUsers() {
    createUser(userService, "e2e-producttest-buyer-username", "testuser", IUserService.Role.BUYER);
    createUser(userService, "e2e-producttest-seller-username", "testuser", IUserService.Role.SELLER);
    createUser(userService, "e2e-producttest-wrong-seller-username", "testuser", IUserService.Role.SELLER);
  }

  @Test
  public void test01_BuyerCannotCreateProduct() {
    loginUser(tokenSessionService, "e2e-producttest-buyer-username", "testuser");
    IProductService.ProductContext product = IProductService.ProductContext.create(
        3,
        BigDecimal.TEN,
        "Snickers"
    );
    Throwable t = null;
    try {
      productService.createProduct(product);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof ForbiddenException);
  }

  @Test
  public void test02_SellerCanCreateProduct() {
    loginUser(tokenSessionService, "e2e-producttest-seller-username", "testuser");
    IProductService.ProductContext product = IProductService.ProductContext.create(
        3,
        BigDecimal.TEN,
        "Snickers"
    );
    currentProduct = productService.createProduct(product);
    assertEquals("Snickers", currentProduct.getProductName());
    assertEquals(3, currentProduct.getAmountAvailable());
    assertEquals(BigDecimal.TEN, currentProduct.getCost());
    assertEquals("e2e-producttest-seller-username", currentProduct.getSellerId());
  }

  @Test
  public void test03_GetProductByAll() {
    assertNotNull(currentProduct);
    // get product anonymous user
    logoutUser();
    IProductService.Product product = productService.getByProductId(
        currentProduct.getProductId());
    assertNotNull(product);
    assertEquals("Snickers", product.getProductName());
    assertEquals("e2e-producttest-seller-username", product.getSellerId());
    // get product buyer user
    loginUser(tokenSessionService, "e2e-producttest-buyer-username", "testuser");
    product = productService.getByProductId(
        currentProduct.getProductId());
    assertNotNull(product);
    assertEquals("Snickers", product.getProductName());
    assertEquals("e2e-producttest-seller-username", product.getSellerId());
    // get product buyer wrong seller
    loginUser(tokenSessionService, "e2e-producttest-wrong-seller-username", "testuser");
    product = productService.getByProductId(
        currentProduct.getProductId());
    assertNotNull(product);
    assertEquals("Snickers", product.getProductName());
    assertEquals("e2e-producttest-seller-username", product.getSellerId());
  }

  @Test
  public void test04_DeleteProductWrongSeller() {
    assertNotNull(currentProduct);
    loginUser(tokenSessionService, "e2e-producttest-wrong-seller-username", "testuser");
    Throwable t = null;
    try {
      productService.deleteProduct(currentProduct.getProductId());
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof ForbiddenException);
  }

  @Test
  public void test05_DeleteProduct() {
    assertNotNull(currentProduct);
    loginUser(tokenSessionService, "e2e-producttest-seller-username", "testuser");
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
  public void test06_DeleteUsers() {
    deleteUser(userService, tokenSessionService, "e2e-producttest-buyer-username");
    deleteUser(userService, tokenSessionService, "e2e-producttest-seller-username");
    deleteUser(userService, tokenSessionService, "e2e-producttest-wrong-seller-username");
  }
}
