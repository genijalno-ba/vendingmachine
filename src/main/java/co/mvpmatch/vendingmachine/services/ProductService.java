package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.data.product.IProductRepository;
import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import co.mvpmatch.vendingmachine.data.user.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jvnet.hk2.annotations.Service;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
public class ProductService extends AbstractService implements IProductService {

  @Inject
  private IProductRepository productRepository;

  @Inject
  private ProductAdapter productAdapter;

  @Context
  SecurityContext securityContext;

  @Inject
  private IUserRepository userRepository;

  @Override
  public Product createProduct(ProductContext productContext) {
    try {
      User authUser = getLoggedUser(securityContext, userRepository);
      String sellerId = authUser.getUsername();
      boolean success = 1 == productRepository.createProduct(productContext, sellerId);
      if (!success) {
        throw new VendingMachineCreateProductException("Could not create product", null);
      }
      String productName = productContext.getProductName();
      co.mvpmatch.vendingmachine.data.product.Product productEntity =
          productRepository.getByProductNameAndSellerId(productName, sellerId);
      return productAdapter.fromEntity(productEntity);
    } catch (SQLException e) {
      throw new VendingMachineCreateProductException("Could not create product", e);
    }
  }

  @Override
  public Collection<Product> getAll() {
    try {
      return productRepository.getAll().stream().map(p -> productAdapter.fromEntity(p))
          .collect(Collectors.toCollection(ArrayList::new));
    } catch (SQLException e) {
      throw new VendingMachineReadProductException("Could not read products", e);
    }
  }

  @Override
  public Product getByProductId(BigInteger productId) {
    try {
      co.mvpmatch.vendingmachine.data.product.Product productEntity =
          productRepository.getByProductId(productId);
      if (null == productEntity) {
        throw new VendingMachineProductNotFoundException("Product not found");
      }
      return productAdapter.fromEntity(productEntity);
    } catch (SQLException e) {
      throw new VendingMachineReadProductException("Could not read product " + productId, e);
    }
  }

  @Override
  public Product updateProduct(Product product) {
    try {
      co.mvpmatch.vendingmachine.data.product.Product productEntity =
          productAdapter.fromContext(product);
      checkUpdateProductPermission(productEntity);
      boolean success = 1 == productRepository.updateProduct(productEntity);
      if (!success) {
        throw new VendingMachineUpdateProductException("Could not update product", null);
      }
      return productAdapter.fromEntity(
          productRepository.getByProductId(product.getProductId()));
    } catch (SQLException e) {
      throw new VendingMachineUpdateProductException("Could not update product", e);
    }
  }

  @Override
  public Product deleteProduct(BigInteger productId) {
    try {
      co.mvpmatch.vendingmachine.data.product.Product productEntity =
          productRepository.getByProductId(productId);
      if (null == productEntity) {
        throw new VendingMachineProductNotFoundException("Product not found");
      }
      checkUpdateProductPermission(productEntity);
      boolean success = 1 == productRepository.deleteProduct(productId);
      if (!success) {
        throw new VendingMachineDeleteProductException("Could not delete product", null);
      }
      return productAdapter.fromEntity(productEntity);
    } catch (SQLException e) {
      throw new VendingMachineDeleteProductException("Could not delete product", e);
    }
  }

  private void checkUpdateProductPermission(co.mvpmatch.vendingmachine.data.product.Product product) throws SQLException {
    User loggedUser = getLoggedUser(securityContext, userRepository);
    if (!loggedUser.getUsername().equals(product.getSellerId())) {
      throw new VendingMachineUpdateProductForbiddenException("You cannot update Products from other sellers");
    }
  }
}
