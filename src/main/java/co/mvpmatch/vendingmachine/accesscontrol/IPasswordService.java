package co.mvpmatch.vendingmachine.accesscontrol;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface IPasswordService {

  String digestPassword(String plainTextPassword);
}
