package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.IProductService;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class ProductServiceClient extends AbstractClient<IProductService.Product> implements IProductService {

  @Override
  public Product createProduct(ProductContext productContext) {
    return post("product", productContext, Product.class);
  }

  @Override
  public Collection<Product> getAll() {
    return getCollection("product");
  }

  @Override
  public Product getByProductId(BigInteger productId) {
    return get("product/" + URLEncoder.encode(productId.toString(), StandardCharsets.UTF_8), Product.class);
  }

  @Override
  public Product updateProduct(Product product) {
    return put("product", product, Product.class);
  }

  @Override
  public Product deleteProduct(BigInteger productId) {
    return delete("product/" + URLEncoder.encode(productId.toString(), StandardCharsets.UTF_8), Product.class);
  }
}
