package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.ProductServiceClient;
import co.mvpmatch.vendingmachine.clients.TokenSessionClient;
import co.mvpmatch.vendingmachine.clients.TransactionServiceClient;
import co.mvpmatch.vendingmachine.clients.UserServiceClient;
import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.contracts.ITransactionService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import jakarta.ws.rs.BadRequestException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static co.mvpmatch.vendingmachine.contracts.IDepositService.COIN_10;
import static co.mvpmatch.vendingmachine.contracts.IDepositService.COIN_5;
import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TransactionServiceTest extends VendingMachineApiTest {

  private static IProductService productService;
  private static IUserService userService;
  private static ITokenSessionService tokenSessionService;
  private static ITransactionService transactionService;
  private static IProductService.Product currentProduct;

  @BeforeClass
  public static void setUp() {
    VendingMachineApiTest.startServer();
    // create the client
    productService = new ProductServiceClient();
    userService = new UserServiceClient();
    tokenSessionService = new TokenSessionClient();
    transactionService = new TransactionServiceClient();
  }

  protected void setAuthToken(String authToken) {
    ((UserServiceClient) userService).setAuthToken(authToken);
    ((ProductServiceClient) productService).setAuthToken(authToken);
    ((TokenSessionClient) tokenSessionService).setAuthToken(authToken);
    ((TransactionServiceClient) transactionService).setAuthToken(authToken);
  }

  protected void resetAuthToken() {
    ((UserServiceClient) userService).setAuthToken(null);
    ((ProductServiceClient) productService).setAuthToken(null);
    ((TokenSessionClient) tokenSessionService).setAuthToken(null);
    ((TransactionServiceClient) transactionService).setAuthToken(null);
  }

  @AfterClass
  public static void tearDown() {
    VendingMachineApiTest.shutdownServer();
  }

  @Test
  public void test00_CreateUsersAndProducts() {
    createUser(userService, "e2e-transactiontest-buyer-username", "testuser", IUserService.Role.BUYER);
    createUser(userService, "e2e-transactiontest-seller-username", "testuser", IUserService.Role.SELLER);
    loginUser(tokenSessionService, "e2e-transactiontest-seller-username", "testuser");
    IProductService.ProductContext product = IProductService.ProductContext.create(
        30,
        COIN_5,
        "Snickers"
    );
    currentProduct = productService.createProduct(product);
    assertEquals("Snickers", currentProduct.getProductName());
    assertEquals(30, currentProduct.getAmountAvailable());
    assertEquals(COIN_5.intValue(), currentProduct.getCost().intValue());
    assertEquals("e2e-transactiontest-seller-username", currentProduct.getSellerId());
  }

  @Test
  public void test01_BuyTooExpensive() {
    assertNotNull(currentProduct);
    loginUser(tokenSessionService, "e2e-transactiontest-buyer-username", "testuser");
    ITransactionService.TransactionContext transactionContext = ITransactionService.TransactionContext
        .create(currentProduct.getProductId(), 4);
    Throwable t = null;
    try {
      transactionService.buy(transactionContext);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof BadRequestException);
  }

  @Test
  public void test02_BuyTooMany() {
    assertNotNull(currentProduct);
    loginUser(tokenSessionService, "e2e-transactiontest-buyer-username", "testuser");
    ITransactionService.TransactionContext transactionContext = ITransactionService.TransactionContext
        .create(currentProduct.getProductId(), 31);
    Throwable t = null;
    try {
      transactionService.buy(transactionContext);
    } catch (Exception e) {
      t = e;
    }
    assertNotNull(t);
    assertTrue(t instanceof BadRequestException);
  }

  @Test
  public void test03_BuyOne() {
    assertNotNull(currentProduct);
    loginUser(tokenSessionService, "e2e-transactiontest-buyer-username", "testuser");
    ITransactionService.TransactionContext transactionContext = ITransactionService.TransactionContext
        .create(currentProduct.getProductId(), 1);
    ITransactionService.Transaction transaction = transactionService.buy(transactionContext);
    assertNotNull(transaction);
    assertEquals(COIN_5, transaction.getTotalSpent());
    assertEquals(1, transaction.getChange().size());
    transaction.getChange().stream().findFirst()
        .ifPresent(coin -> assertEquals(COIN_5, coin));
    assertEquals(1, transaction.getItemsPurchased().size());
    IUserService.User buyer = userService.getUserByUsername("e2e-transactiontest-buyer-username");
    assertEquals(COIN_5.intValue(), buyer.getDeposit().intValue());
    loginUser(tokenSessionService, "e2e-transactiontest-seller-username", "testuser");
    IUserService.User seller = userService.getUserByUsername("e2e-transactiontest-seller-username");
    assertEquals(COIN_10.add(COIN_5).intValue(), seller.getDeposit().intValue());
  }

  @Test
  public void test04_DeleteProductAndUsers() {
    loginUser(tokenSessionService, "e2e-transactiontest-buyer-username", "testuser");
    deleteUser(userService, tokenSessionService, "e2e-transactiontest-buyer-username");
    loginUser(tokenSessionService, "e2e-transactiontest-seller-username", "testuser");
    productService.deleteProduct(currentProduct.getProductId());
    deleteUser(userService, tokenSessionService, "e2e-transactiontest-seller-username");
  }

}
