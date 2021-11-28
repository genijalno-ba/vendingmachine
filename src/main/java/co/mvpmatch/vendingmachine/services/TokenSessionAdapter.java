package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.data.tokensession.TokenSession;
import org.jvnet.hk2.annotations.Service;

import java.time.OffsetDateTime;

@Service
public class TokenSessionAdapter {

  public TokenSession fromContext(String username, String token, OffsetDateTime validUntil) {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setUsername(username);
    tokenSession.setToken(token);
    tokenSession.setValidUntil(validUntil);
    return tokenSession;
  }

  public ITokenSessionService.TokenSession fromEntity(TokenSession entity) {
    return ITokenSessionService.TokenSession.create(
        entity.getUsername(),
        entity.getToken(),
        entity.getValidUntil()
    );
  }
}
