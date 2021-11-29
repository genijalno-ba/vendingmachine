package co.mvpmatch.vendingmachine.accesscontrol;

import org.jvnet.hk2.annotations.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@SuppressWarnings("unused")
@Service
public class SecretKeyGenerator implements IKeyGenerator {

  @Override
  public Key generateKey() {
    String secretKey = "0sajf!f";
    return new SecretKeySpec(secretKey.getBytes(), 0, secretKey.getBytes().length, "DES");
  }
}
