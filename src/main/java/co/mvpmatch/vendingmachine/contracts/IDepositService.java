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

  Collection<BigDecimal> ALLOWED_DEPOSIT_AMOUNT = new HashSet<>() {{
    add(BigDecimal.valueOf(5));
    add(BigDecimal.valueOf(10));
    add(BigDecimal.valueOf(20));
    add(BigDecimal.valueOf(50));
    add(BigDecimal.valueOf(100));
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
