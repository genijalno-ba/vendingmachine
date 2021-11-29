package co.mvpmatch.vendingmachine.accesscontrol;

import org.jvnet.hk2.annotations.Contract;

import java.security.Key;

@Contract
public interface IKeyGenerator {

  Key generateKey();
}
