package co.mvpmatch.vendingmachine.data.product;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Collection;

@SuppressWarnings("unused")
@Service
public class ProductRepository implements IProductRepository {

  @Inject
  private IProductDao productDao;

  @Override
  public int createProduct(IProductService.ProductContext productContext, String sellerId) throws SQLException {
    Product product = new Product();
    product.setCost(productContext.getCost());
    product.setProductName(productContext.getProductName());
    product.setAmountAvailable(productContext.getAmountAvailable());
    product.setSellerId(sellerId);
    return productDao.create(product);
  }

  @Override
  public Collection<Product> getAll() throws SQLException {
    return productDao.getAll();
  }

  @Override
  public Product getByProductId(BigInteger productId) throws SQLException {
    Product product = new Product();
    product.setProductId(productId);
    return productDao.read(product);
  }

  @Override
  public Product getByProductNameAndSellerId(String productName, String sellerId) throws SQLException {
    return productDao.readByProductNameAndSellerId(productName, sellerId);
  }

  @Override
  public int updateProduct(Product product) throws SQLException {
    return productDao.update(product);
  }

  @Override
  public int deleteProduct(BigInteger productId) throws SQLException {
    Product product = getByProductId(productId);
    return productDao.delete(product);
  }

}
