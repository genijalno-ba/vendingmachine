package co.mvpmatch.vendingmachine.contracts;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

@SuppressWarnings("unused")
@Contract
public interface IProductService {

  Product createProduct(ProductContext productContext);

  Collection<Product> getAll();

  Product getByProductId(BigInteger productId);

  Product updateProduct(Product product);

  Product deleteProduct(BigInteger productId);

  class ProductContext {

    private final int amountAvailable;
    private final BigDecimal cost;
    private final String productName;

    @JsonbCreator
    public static IProductService.ProductContext create(
        @JsonbProperty("amountAvailable") int amountAvailable,
        @JsonbProperty("cost") BigDecimal cost,
        @JsonbProperty("productName") String productName
    ) {
      return new IProductService.ProductContext(amountAvailable, cost, productName);
    }

    private ProductContext(int amountAvailable, BigDecimal cost, String productName) {
      this.amountAvailable = amountAvailable;
      this.cost = cost;
      this.productName = productName;
    }

    public int getAmountAvailable() {
      return amountAvailable;
    }

    public BigDecimal getCost() {
      return cost;
    }

    public String getProductName() {
      return productName;
    }
  }

  class Product {

    private final BigInteger productId;
    private final int amountAvailable;
    private final BigDecimal cost;
    private final String productName;
    private final String sellerId;

    @JsonbCreator
    public static Product create(
        @JsonbProperty("productId") BigInteger productId,
        @JsonbProperty("amountAvailable") int amountAvailable,
        @JsonbProperty("cost") BigDecimal cost,
        @JsonbProperty("productName") String productName,
        @JsonbProperty("sellerId") String sellerId
    ) {
      return new Product(productId, amountAvailable, cost, productName, sellerId);
    }

    private Product(BigInteger productId, int amountAvailable, BigDecimal cost, String productName, String sellerId) {
      this.productId = productId;
      this.amountAvailable = amountAvailable;
      this.cost = cost;
      this.productName = productName;
      this.sellerId = sellerId;
    }

    public BigInteger getProductId() {
      return productId;
    }

    public int getAmountAvailable() {
      return amountAvailable;
    }

    public BigDecimal getCost() {
      return cost;
    }

    public String getProductName() {
      return productName;
    }

    public String getSellerId() {
      return sellerId;
    }

    public static class Builder {

      private BigInteger productId;
      private int amountAvailable;
      private BigDecimal cost;
      private String productName;
      private String sellerId;

      public Builder setProductId(BigInteger productId) {
        this.productId = productId;
        return this;
      }

      public Builder setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
        return this;
      }

      public Builder setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
      }

      public Builder setProductName(String productName) {
        this.productName = productName;
        return this;
      }

      public Builder setSellerId(String sellerId) {
        this.sellerId = sellerId;
        return this;
      }

      public Product build() {
        return new Product(productId, amountAvailable, cost, productName, sellerId);
      }
    }
  }

  class VendingMachineCreateProductException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineCreateProductException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineReadProductException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineReadProductException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineUpdateProductException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineUpdateProductException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineProductNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineProductNotFoundException(String message) {
      super(message);
    }
  }

  class VendingMachineDeleteProductException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineDeleteProductException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineUpdateProductForbiddenException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineUpdateProductForbiddenException(String message) {
      super(message);
    }
  }
}
