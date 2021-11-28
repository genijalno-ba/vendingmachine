package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.clients.ProductServiceClient;
import co.mvpmatch.vendingmachine.contracts.IProductService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ProductServiceTest {

  private HttpServer server;
  private IProductService productService;

  @Before
  public void setUp() {
    // start the server
    server = Main.startServer();
    // create the client
    productService = new ProductServiceClient();
  }

  @After
  public void tearDown() {
    server.shutdownNow();
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
