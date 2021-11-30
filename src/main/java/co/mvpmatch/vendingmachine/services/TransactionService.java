package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IProductService;
import co.mvpmatch.vendingmachine.contracts.ITransactionService;
import co.mvpmatch.vendingmachine.data.product.IProductRepository;
import co.mvpmatch.vendingmachine.data.product.Product;
import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import co.mvpmatch.vendingmachine.data.user.User;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.SecurityContext;
import org.jvnet.hk2.annotations.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static co.mvpmatch.vendingmachine.contracts.IDepositService.*;

@SuppressWarnings("unused")
@Service
public class TransactionService extends AbstractService implements ITransactionService {

  @Context
  private SecurityContext securityContext;

  @Inject
  private IUserRepository userRepository;

  @Inject
  private IProductRepository productRepository;

  @Inject
  private ProductAdapter productAdapter;

  @Override
  public Transaction buy(TransactionContext transactionContext) {
    BigInteger productId = transactionContext.getProductId();
    int requestedAmount = transactionContext.getAmount();
    try {
      User loggedUser = getLoggedUser(securityContext, userRepository);
      BigDecimal buyerDeposit = loggedUser.getDeposit();
      Product product = productRepository.getByProductId(productId);
      if (requestedAmount > product.getAmountAvailable()) {
        throw new ITransactionService.VendingMachineProductCountTooLowException("Not enough products available");
      }
      BigDecimal totalCost = BigDecimal.ZERO;
      for (int i = 0; i < requestedAmount; i++) {
        totalCost = totalCost.add(product.getCost());
      }
      if (totalCost.compareTo(buyerDeposit) > 0) {
        throw new ITransactionService.VendingMachineDepositTooLowException("Total cost exceeds user deposit");
      }
      // all good, proceed with transaction
      User seller = userRepository.getUser(product.getSellerId());
      product.setAmountAvailable(product.getAmountAvailable() - requestedAmount);
      loggedUser.setDeposit(buyerDeposit.subtract(totalCost));
      seller.setDeposit(seller.getDeposit().add(totalCost));
      productRepository.updateProduct(product);
      userRepository.updateUser(loggedUser);
      userRepository.updateUser(seller);
      Collection<BigDecimal> change = calculateChange(totalCost, buyerDeposit);
      Collection<IProductService.Product> itemsPurchased = new ArrayList<>() {{
        for (int i = 0; i < requestedAmount; i++) {
          add(productAdapter.fromEntity(product));
        }
      }};
      return Transaction.create(totalCost, change, itemsPurchased);
    } catch (SQLException e) {
      throw new ITransactionService.VendingMachineTransactionException("Could not execute transaction", e);
    }
  }

  public Collection<BigDecimal> calculateChange(BigDecimal totalCost, BigDecimal deposit) {
    Collection<BigDecimal> coins = new ArrayList<>();
    BigDecimal changeDue = deposit.subtract(totalCost);
    List<BigDecimal> possibleCoins = new LinkedList<>(ALLOWED_DEPOSIT_AMOUNT);
    possibleCoins.sort(Collections.reverseOrder());
    double mod_100 = changeDue.intValue() % COIN_100.intValue();
    double mod_50 = mod_100 % COIN_50.intValue();
    double mod_20 = mod_50 % COIN_20.intValue();
    double mod_10 = mod_20 % COIN_10.intValue();
    double mod_5 = mod_10 % COIN_5.intValue();
    int num_100 = (int) ((changeDue.doubleValue() - mod_100) / COIN_100.intValue());
    int num_50 = (int) ((mod_100 - mod_50) / COIN_50.intValue());
    int num_20 = (int) ((mod_50 - mod_20) / COIN_20.intValue());
    int num_10 = (int) ((mod_20 - mod_10) / COIN_10.intValue());
    int num_5 = (int) ((mod_10 - mod_5) / COIN_5.intValue());
    for (int i = 0; i < num_100; i++) {
      coins.add(COIN_100);
    }
    for (int i = 0; i < num_50; i++) {
      coins.add(COIN_50);
    }
    for (int i = 0; i < num_20; i++) {
      coins.add(COIN_20);
    }
    for (int i = 0; i < num_10; i++) {
      coins.add(COIN_10);
    }
    for (int i = 0; i < num_5; i++) {
      coins.add(COIN_5);
    }
    return coins;
  }
}
