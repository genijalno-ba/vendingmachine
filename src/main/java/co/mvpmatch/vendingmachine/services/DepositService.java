package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IDepositService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import co.mvpmatch.vendingmachine.data.user.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jvnet.hk2.annotations.Service;

import java.math.BigDecimal;
import java.sql.SQLException;

@Service
public class DepositService extends AbstractService implements IDepositService {

  @Inject
  private IUserRepository userRepository;

  @Inject
  private UserAdapter userAdapter;

  @Context
  private SecurityContext securityContext;

  @Override
  public IUserService.User deposit(DepositContext depositContext) {
    BigDecimal amount = depositContext.getAmount();
    validateAmount(amount);
    try {
      User user = getLoggedUser(securityContext, userRepository);
      BigDecimal newDeposit = user.getDeposit().add(amount);
      user.setDeposit(newDeposit);
      boolean success = 1 == userRepository.updateUser(user);
      if (!success) {
        throw new IDepositService.VendingMachineDepositException("Could not deposit", null);
      }
      return userAdapter.fromDb(user);
    } catch (SQLException e) {
      throw new IDepositService.VendingMachineDepositException("Could not deposit", e);
    }
  }

  @Override
  public IUserService.User reset() {
    try {
      User user = getLoggedUser(securityContext, userRepository);
      user.setDeposit(BigDecimal.ZERO);
      boolean success = 1 == userRepository.updateUser(user);
      if (!success) {
        throw new IDepositService.VendingMachineResetDepositException("Could not reset deposit", null);
      }
      return userAdapter.fromDb(user);
    } catch (SQLException e) {
      throw new IDepositService.VendingMachineResetDepositException("Could not reset deposit", e);
    }
  }

  private void validateAmount(BigDecimal amount) {
    if (ALLOWED_DEPOSIT_AMOUNT.contains(amount)) {
      return;
    }
    throw new IDepositService.VendingMachineInvalidDepositException("Invalid amount, allowed are: " + ALLOWED_DEPOSIT_AMOUNT);
  }
}
