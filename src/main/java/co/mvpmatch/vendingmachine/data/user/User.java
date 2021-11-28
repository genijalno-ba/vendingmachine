package co.mvpmatch.vendingmachine.data.user;

import co.mvpmatch.vendingmachine.contracts.IUserService;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class User {

  private String username;
  private String password;
  private BigDecimal deposit;
  private IUserService.Role role;

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

  public IUserService.Role getRole() {
    return role;
  }

  public void setRole(IUserService.Role role) {
    this.role = role;
  }
}
