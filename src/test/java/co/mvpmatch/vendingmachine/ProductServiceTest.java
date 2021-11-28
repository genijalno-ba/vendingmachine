package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.ProductServiceClient;
import co.mvpmatch.vendingmachine.contracts.IProductService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest extends AbstractTest {

  private static IProductService productService;

  @BeforeClass
  public static void setUp() {
    AbstractTest.startServer();
    // create the client
    productService = new ProductServiceClient();
  }

  @AfterClass
  public static void tearDown() {
    AbstractTest.shutdownServer();
  }

  @Test
  public void testGetBySellerUsername() {
    Collection<IProductService.Product> products = productService.getBySellerUsername("myusername");
    assertEquals(1, products.size());
  }

  @Test
  public void testCreateProduct() {
    IProductService.Product product = new IProductService.Product.Builder()
        .setProductName("Snickers")
        .setAmountAvailable(3)
        .setCost(BigDecimal.ONE)
        .setSellerId("myusername")
        .build();
    IProductService.Product newProduct = productService.createProduct(product);
    assertEquals("Snickers", newProduct.getProductName());
    assertEquals(3, newProduct.getAmountAvailable());
    assertEquals(BigDecimal.ONE, newProduct.getCost());
    assertEquals("myusername", newProduct.getSellerId());
  }
}
