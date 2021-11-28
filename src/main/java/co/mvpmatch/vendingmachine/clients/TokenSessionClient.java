package co.mvpmatch.vendingmachine.clients;

import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TokenSessionClient extends AbstractClient<ITokenSessionService.TokenSession> implements ITokenSessionService {

  @Override
  public TokenSession createTokenSession(TokenSessionContext tokenSessionContext) {
    return post("token-session", tokenSessionContext, TokenSession.class);
  }

  @Override
  public TokenSession readTokenSession(String token) {
    return get("token-session/" + URLEncoder.encode(token, StandardCharsets.UTF_8), TokenSession.class);
  }

  @Override
  public TokenSession deleteTokenSession(String token) {
    return delete("token-session/" + URLEncoder.encode(token, StandardCharsets.UTF_8), TokenSession.class);
  }
}
