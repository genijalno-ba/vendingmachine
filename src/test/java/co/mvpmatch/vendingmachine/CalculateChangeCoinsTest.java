package co.mvpmatch.vendingmachine;

import co.mvpmatch.vendingmachine.services.TransactionService;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Collection;

import static co.mvpmatch.vendingmachine.contracts.IDepositService.*;
import static org.junit.Assert.assertEquals;

public class CalculateChangeCoinsTest {

  private final TransactionService transactionService = new TransactionService();

  @Test
  public void test01() {
    BigDecimal total = BigDecimal.valueOf(400);
    BigDecimal deposit = BigDecimal.valueOf(500);
    Collection<BigDecimal> coins = transactionService.calculateChange(total, deposit);
    assertEquals(1, coins.size());
    coins.stream().findFirst()
        .ifPresent(coin -> assertEquals(COIN_100, coin));
  }

  @Test
  public void test02() {
    BigDecimal total = BigDecimal.valueOf(240);
    BigDecimal deposit = BigDecimal.valueOf(500);
    Collection<BigDecimal> coins = transactionService.calculateChange(total, deposit);
    assertEquals(2, coins.stream().filter(COIN_100::equals).count());
    assertEquals(1, coins.stream().filter(COIN_50::equals).count());
    assertEquals(1, coins.stream().filter(COIN_10::equals).count());
  }

  @Test
  public void test03() {
    BigDecimal total = BigDecimal.valueOf(160);
    BigDecimal deposit = BigDecimal.valueOf(500);
    Collection<BigDecimal> coins = transactionService.calculateChange(total, deposit);
    assertEquals(3, coins.stream().filter(COIN_100::equals).count());
    assertEquals(2, coins.stream().filter(COIN_20::equals).count());
  }

  @Test
  public void test04() {
    BigDecimal total = BigDecimal.valueOf(355);
    BigDecimal deposit = BigDecimal.valueOf(500);
    Collection<BigDecimal> coins = transactionService.calculateChange(total, deposit);
    assertEquals(1, coins.stream().filter(COIN_100::equals).count());
    assertEquals(2, coins.stream().filter(COIN_20::equals).count());
    assertEquals(1, coins.stream().filter(COIN_5::equals).count());
  }
}
