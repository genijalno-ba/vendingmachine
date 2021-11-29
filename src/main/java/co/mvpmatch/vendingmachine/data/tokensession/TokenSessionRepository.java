package co.mvpmatch.vendingmachine.data.tokensession;

import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.SQLException;

@SuppressWarnings("unused")
@Service
public class TokenSessionRepository implements ITokenSessionRepository {

  @Inject
  ITokenSessionDao tokenSessionDao;

  @Override
  public int createTokenSession(TokenSession tokenSession) throws SQLException {
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
  public int deleteTokenSession(String token) throws SQLException {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setToken(token);
    return tokenSessionDao.deleteByToken(tokenSession);
  }

  @Override
  public int deleteTokenSessionByUsername(String username) throws SQLException {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setUsername(username);
    return tokenSessionDao.delete(tokenSession);
  }
  
}
