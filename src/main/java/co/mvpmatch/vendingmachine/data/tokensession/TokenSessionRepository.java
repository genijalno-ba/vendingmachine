package co.mvpmatch.vendingmachine.data.tokensession;

import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneOffset;

@Service
public class TokenSessionRepository implements ITokenSessionRepository {

  @Inject
  ITokenSessionDao tokenSessionDao;

  @Override
  public TokenSession createTokenSession(TokenSession tokenSession) throws SQLException {
    return tokenSessionDao.create(tokenSession);
  }

  @Override
  public TokenSession getTokenSessionByUsername(String username) throws SQLException {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setUsername(username);
    return tokenSessionDao.read(tokenSession);
  }

  @Override
  public TokenSession getTokenSessionByToken(String token) throws SQLException {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setToken(token);
    return tokenSessionDao.readByToken(tokenSession);
  }

  @Override
  public TokenSession deleteTokenSession(String token) throws SQLException {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setToken(token);
    tokenSession.setValidUntil(Instant.now().atOffset(ZoneOffset.UTC));
    TokenSession entity = tokenSessionDao.deleteByToken(tokenSession);
    tokenSession.setUsername(entity.getUsername());
    return tokenSession;
  }
}
