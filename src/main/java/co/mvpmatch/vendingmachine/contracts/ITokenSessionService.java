package co.mvpmatch.vendingmachine.contracts;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import org.jvnet.hk2.annotations.Contract;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAmount;

@SuppressWarnings("unused")
@Contract
public interface ITokenSessionService {

  TokenSession createTokenSession(TokenSessionContext tokenSessionContext);

  TokenSession readTokenSession(String token);

  TokenSession deleteTokenSession(String token);

  class TokenSessionContext {
    private final String username;
    private final String password;

    private TokenSessionContext(String username, String password) {
      this.username = username;
      this.password = password;
    }

    @JsonbCreator
    public static TokenSessionContext create(
        @JsonbProperty("username") String username,
        @JsonbProperty("password") String password
    ) {
      return new TokenSessionContext(username, password);
    }

    public String getUsername() {
      return username;
    }

    public String getPassword() {
      return password;
    }
  }

  class TokenSession {
    private final String username;
    private final String token;
    private final OffsetDateTime validUntil;

    private TokenSession(String username, String token, OffsetDateTime validUntil) {
      this.username = username;
      this.token = token;
      this.validUntil = validUntil;
    }

    @JsonbCreator
    public static TokenSession create(
        @JsonbProperty("username") String username,
        @JsonbProperty("token") String token,
        @JsonbProperty("validUntil") OffsetDateTime validUntil
    ) {
      return new TokenSession(username, token, validUntil);
    }

    public String getUsername() {
      return username;
    }

    public String getToken() {
      return token;
    }

    public OffsetDateTime getValidUntil() {
      return validUntil;
    }
  }

  class VendingMachineCreateTokenSessionException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineCreateTokenSessionException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineTokenSessionNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineTokenSessionNotFoundException(String message) {
      super(message);
    }
  }

  class VendingMachineDeleteTokenSessionNotFoundException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineDeleteTokenSessionNotFoundException(String message) {
      super(message);
    }
  }

  class VendingMachineDeleteTokenSessionException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineDeleteTokenSessionException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineReadTokenSessionException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineReadTokenSessionException(String message, Throwable throwable) {
      super(message, throwable);
    }
  }

  class VendingMachineAuthenticateException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public VendingMachineAuthenticateException(String message) {
      super(message);
    }
  }

}
