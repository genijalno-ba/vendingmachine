package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.IProductService;

import java.util.Collection;

public class ProductServiceClient extends AbstractClient<IProductService.Product> implements IProductService {

  @Override
  public Collection<Product> getBySellerUsername(String username) {
    return getCollection("product/" + username);
  }

  @Override
  public Product createProduct(Product product) {
    return post("product", product, Product.class);
  }
}
