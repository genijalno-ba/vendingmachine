package co.mvpmatch.vendingmachine.contracts;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;

@Contract
public interface IDepositService {

  IUserService.User deposit(DepositContext depositContext);

  IUserService.User reset();

  BigDecimal COIN_5 = BigDecimal.valueOf(5);
  BigDecimal COIN_10 = BigDecimal.valueOf(10);
  BigDecimal COIN_20 = BigDecimal.valueOf(20);
  BigDecimal COIN_50 = BigDecimal.valueOf(50);
  BigDecimal COIN_100 = BigDecimal.valueOf(100);

  Collection<BigDecimal> ALLOWED_DEPOSIT_AMOUNT = new HashSet<>() {{
    add(COIN_5);
    add(COIN_10);
    add(COIN_20);
    add(COIN_50);
    add(COIN_100);
  }};

  class DepositContext {

    private final BigDecimal amount;

    @JsonbCreator
    public static DepositContext create(@JsonbProperty("amount") BigDecimal amount) {
      return new DepositContext(amount);
    }

    private DepositContext(BigDecimal amount) {
      this.amount = amount;
    }

    public BigDecimal getAmount() {
      return amount;
    }
  }

  class VendingMachineDepositException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineDepositException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineResetDepositException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineResetDepositException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineInvalidDepositException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineInvalidDepositException(String message) {
      super(message);
    }
  }
}
