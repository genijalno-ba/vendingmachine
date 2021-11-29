package co.mvpmatch.vendingmachine.accesscontrol;

import org.jvnet.hk2.annotations.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@SuppressWarnings("unused")
@Service
public class PasswordService implements IPasswordService {

  @Override
  public String digestPassword(String plainTextPassword) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      md.update(plainTextPassword.getBytes(StandardCharsets.UTF_8));
      byte[] passwordDigest = md.digest();
      return new String(Base64.getEncoder().encode(passwordDigest));
    } catch (Exception e) {
      throw new RuntimeException("Exception encoding password", e);
    }
  }
}
