package co.mvpmatch.vendingmachine.contracts;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.jvnet.hk2.annotations.Contract;

import java.math.BigDecimal;

@SuppressWarnings("unused")
@Contract
public interface IUserService {

  User createUser(UserContext user);

  User getUserByUsername(String username);

  User deleteUser(String username);

  class VendingMachineCreateUserException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineCreateUserException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineCreateUserAlreadyExistsException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineCreateUserAlreadyExistsException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineUserNotFoundException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineUserNotFoundException(String message) {
      super(message);
    }

    public VendingMachineUserNotFoundException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineUserInternalErrorException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineUserInternalErrorException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineDeleteUserException extends RuntimeException {
    public static final long serialVersionUID = 1L;

    public VendingMachineDeleteUserException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class UserContext {

    private String username;
    private String password;
    private BigDecimal deposit;
    private Role role;

    public String getUsername() {
      return username;
    }

    public void setUsername(String username) {
      this.username = username;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public BigDecimal getDeposit() {
      return deposit;
    }

    public void setDeposit(BigDecimal deposit) {
      this.deposit = deposit;
    }

    public Role getRole() {
      return role;
    }

    public void setRole(Role role) {
      this.role = role;
    }
  }


  class User {

    private final String username;
    private final BigDecimal deposit;
    private final Role role;

    @JsonbCreator
    public static User create(
        @JsonbProperty("username") String username,
        @JsonbProperty("deposit") BigDecimal deposit,
        @JsonbProperty("role") Role role
    ) {
      return new User(username, deposit, role);
    }

    private User(String username, BigDecimal deposit, Role role) {
      this.username = username;
      this.deposit = deposit;
      this.role = role;
    }

    public String getUsername() {
      return username;
    }

    public BigDecimal getDeposit() {
      return deposit;
    }

    public Role getRole() {
      return role;
    }

    public static class Builder {

      private String username;
      private BigDecimal deposit;
      private Role role;

      public Builder setUsername(String username) {
        this.username = username;
        return this;
      }

      public Builder setDeposit(BigDecimal deposit) {
        this.deposit = deposit;
        return this;
      }

      public Builder setRole(Role role) {
        this.role = role;
        return this;
      }

      public User build() {
        return new User(username, deposit, role);
      }
    }
  }

  enum Role {
    BUYER, SELLER
  }

}
