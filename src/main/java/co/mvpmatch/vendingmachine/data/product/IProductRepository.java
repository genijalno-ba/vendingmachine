package co.mvpmatch.vendingmachine.data.product;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.Collection;

@Contract
public interface IProductRepository {

  int createProduct(IProductService.ProductContext productContext) throws SQLException;

  Collection<Product> getAll() throws SQLException;

  Product getByProductId(BigInteger productId) throws SQLException;

  int updateProduct(Product product) throws SQLException;

  int deleteProduct(BigInteger productId) throws SQLException;

  Product getByProductNameAndSellerId(String productName, String sellerId) throws SQLException;
}
