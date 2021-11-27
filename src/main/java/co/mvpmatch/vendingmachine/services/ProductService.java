package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import org.jvnet.hk2.annotations.Service;

import java.util.Collection;
import java.util.Collections;

@SuppressWarnings("unused")
@Service
public class ProductService implements IProductService {

  @Override
  public Collection<Product> getBySellerUsername(String username) {
    //TODO: persistence
    return Collections.singleton(new IProductService.Product.Builder().build());
  }

  @Override
  public Product createProduct(Product product) {
    //TODO: persistence
    return product;
  }
}
