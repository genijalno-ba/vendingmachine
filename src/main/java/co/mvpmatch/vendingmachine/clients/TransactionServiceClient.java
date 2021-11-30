package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.ITransactionService;

public class TransactionServiceClient extends AbstractClient<ITransactionService.Transaction> implements ITransactionService {

  @Override
  public Transaction buy(TransactionContext transactionContext) {
    return post("buy", transactionContext, Transaction.class);
  }
}
