package co.mvpmatch.vendingmachine.data.product;

import java.math.BigDecimal;
import java.math.BigInteger;

@SuppressWarnings("unused")
public class Product {

  private BigInteger productId;
  private int amountAvailable;
  private BigDecimal cost;
  private String productName;
  private String sellerId;

  public BigInteger getProductId() {
    return productId;
  }

  public void setProductId(BigInteger productId) {
    this.productId = productId;
  }

  public int getAmountAvailable() {
    return amountAvailable;
  }

  public void setAmountAvailable(int amountAvailable) {
    this.amountAvailable = amountAvailable;
  }

  public BigDecimal getCost() {
    return cost;
  }

  public void setCost(BigDecimal cost) {
    this.cost = cost;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public String getSellerId() {
    return sellerId;
  }

  public void setSellerId(String sellerId) {
    this.sellerId = sellerId;
  }
}
