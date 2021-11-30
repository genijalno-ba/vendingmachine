package co.mvpmatch.vendingmachine.contracts;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

@Contract
public interface ITransactionService {

  Transaction buy(TransactionContext transactionContext);

  class TransactionContext {

    private final BigInteger productId;
    private final int amount;

    @JsonbCreator
    public static TransactionContext create(
        @JsonbProperty("productId") BigInteger productId,
        @JsonbProperty("amount") int amount
    ) {
      return new TransactionContext(productId, amount);
    }

    private TransactionContext(BigInteger productId, int amount) {
      this.productId = productId;
      this.amount = amount;
    }

    public BigInteger getProductId() {
      return productId;
    }

    public int getAmount() {
      return amount;
    }
  }

  class Transaction {

    private final BigDecimal totalSpent;
    private final Collection<BigDecimal> change;
    private final Collection<IProductService.Product> itemsPurchased;

    @JsonbCreator
    public static Transaction create(
        @JsonbProperty("totalSpent") BigDecimal totalSpent,
        @JsonbProperty("change") Collection<BigDecimal> change,
        @JsonbProperty("itemsPurchased") Collection<IProductService.Product> itemsPurchased
    ) {
      return new Transaction(totalSpent, change, itemsPurchased);
    }

    private Transaction(BigDecimal totalSpent, Collection<BigDecimal> change, Collection<IProductService.Product> itemsPurchased) {
      this.totalSpent = totalSpent;
      this.change = change;
      this.itemsPurchased = itemsPurchased;
    }

    public BigDecimal getTotalSpent() {
      return totalSpent;
    }

    public Collection<BigDecimal> getChange() {
      return change;
    }

    public Collection<IProductService.Product> getItemsPurchased() {
      return itemsPurchased;
    }
  }

  class VendingMachineTransactionException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineTransactionException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineDepositTooLowException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineDepositTooLowException(String message) {
      super(message);
    }
  }

  class VendingMachineProductCountTooLowException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineProductCountTooLowException(String message) {
      super(message);
    }
  }

}
