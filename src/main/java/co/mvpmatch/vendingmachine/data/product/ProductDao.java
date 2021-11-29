package co.mvpmatch.vendingmachine.data.product;

import co.mvpmatch.vendingmachine.data.AbstractDao;
import org.jvnet.hk2.annotations.Service;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@SuppressWarnings("unused")
@Service
public class ProductDao extends AbstractDao<Product> implements IProductDao {

  @Override
  public int create(Product entity) throws SQLException {
    return executeUpdate("INSERT INTO product_table (product_name, seller_id, amount_available, cost) VALUES (?, ?, ?, ?)",
        entity.getProductName(),
        entity.getSellerId(),
        entity.getAmountAvailable(),
        entity.getCost());
  }

  @Override
  public Product read(Product entity) throws SQLException {
    return executeQuery("SELECT * FROM product_table WHERE product_id = ?",
        entity.getProductId());
  }

  @Override
  public Product readByProductNameAndSellerId(String productName, String sellerId) throws SQLException {
    return executeQuery("SELECT * FROM product_table WHERE product_name = ? AND seller_id = ?",
        productName, sellerId);
  }

  @Override
  public int update(Product entity) throws SQLException {
    return executeUpdate("UPDATE product_table SET product_name = ?, amount_available = ?, cost = ? WHERE product_id = ?",
        entity.getProductName(),
        entity.getAmountAvailable(),
        entity.getCost(),
        entity.getProductId());
  }

  @Override
  public int delete(Product entity) throws SQLException {
    return executeUpdate("DELETE FROM product_table WHERE product_id = ?",
        entity.getProductId());
  }

  @Override
  public Collection<Product> getAll() throws SQLException {
    return executeQueryCollection("SELECT * FROM product_table");
  }

  protected Product fromResultSet(ResultSet resultSet) throws SQLException {
    Product product = new Product();
    product.setProductId(resultSet.getObject("product_id", BigInteger.class));
    product.setProductName(resultSet.getString("product_name"));
    product.setSellerId(resultSet.getString("seller_id"));
    product.setAmountAvailable(resultSet.getInt("amount_available"));
    product.setCost(resultSet.getBigDecimal("cost"));
    return product;
  }
}
