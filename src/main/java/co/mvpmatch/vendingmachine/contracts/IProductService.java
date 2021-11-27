package co.mvpmatch.vendingmachine.contracts;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigDecimal;
import java.util.Collection;

@SuppressWarnings("unused")
@Contract
public interface IProductService {

  Collection<Product> getBySellerUsername(String username);

  Product createProduct(Product product);

  class Product {

    private final int amountAvailable;
    private final BigDecimal cost;
    private final String productName;
    private final String sellerId;

    @JsonbCreator
    public static Product create(
        @JsonbProperty("amountAvailable") int amountAvailable,
        @JsonbProperty("cost") BigDecimal cost,
        @JsonbProperty("productName") String productName,
        @JsonbProperty("sellerId") String sellerId
    ) {
      return new Product(amountAvailable, cost, productName, sellerId);
    }

    private Product(int amountAvailable, BigDecimal cost, String productName, String sellerId) {
      this.amountAvailable = amountAvailable;
      this.cost = cost;
      this.productName = productName;
      this.sellerId = sellerId;
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

      private int amountAvailable;
      private BigDecimal cost;
      private String productName;
      private String sellerId;

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
        return new Product(amountAvailable, cost, productName, sellerId);
      }
    }
  }

}
