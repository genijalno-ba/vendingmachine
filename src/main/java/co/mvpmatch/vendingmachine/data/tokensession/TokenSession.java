package co.mvpmatch.vendingmachine.data.tokensession;

import java.time.OffsetDateTime;

public class TokenSession {

  private String username;
  private String token;
  private OffsetDateTime validUntil;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public OffsetDateTime getValidUntil() {
    return validUntil;
  }

  public void setValidUntil(OffsetDateTime validUntil) {
    this.validUntil = validUntil;
  }
}
