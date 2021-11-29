package co.mvpmatch.vendingmachine.data.product;

import co.mvpmatch.vendingmachine.data.IDao;
import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;
import java.util.Collection;

@Contract
public interface IProductDao extends IDao<Product> {

  Collection<Product> getAll() throws SQLException;

  Product readByProductNameAndSellerId(String productName, String sellerId) throws SQLException;
}
