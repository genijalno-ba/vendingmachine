package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.data.product.Product;
import org.jvnet.hk2.annotations.Service;

@Service
public class ProductAdapter {

  public IProductService.Product fromEntity(Product entity) {
    return IProductService.Product.create(
        entity.getProductId(),
        entity.getAmountAvailable(),
        entity.getCost(),
        entity.getProductName(),
        entity.getSellerId()
    );
  }

  public Product fromContext(IProductService.Product product) {
    Product entity = new Product();
    entity.setProductId(product.getProductId());
    entity.setProductName(product.getProductName());
    entity.setCost(product.getCost());
    entity.setAmountAvailable(product.getAmountAvailable());
    entity.setSellerId(product.getSellerId());
    return entity;
  }
}
