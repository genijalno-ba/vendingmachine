package co.mvpmatch.vendingmachine.data.tokensession;

import co.mvpmatch.vendingmachine.data.AbstractDao;
import org.jvnet.hk2.annotations.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Service
public class TokenSessionDao extends AbstractDao implements ITokenSessionDao {

  @Override
  public TokenSession create(TokenSession entity) throws SQLException {
    boolean success = 1 == executeUpdate("INSERT INTO token_session_table (username, token, valid_until) VALUES (?, ?, ?)",
        entity.getUsername(),
        entity.getToken(),
        entity.getValidUntil());
    if (success) {
      return read(entity);
    }
    return null;
  }

  @Override
  public TokenSession read(TokenSession entity) throws SQLException {
    ResultSet resultSet = executeQuery("SELECT * FROM token_session_table WHERE username = ?", entity.getUsername());
    return fromResultSet(resultSet);
  }

  @Override
  public TokenSession readByToken(TokenSession entity) throws SQLException {
    ResultSet resultSet = executeQuery("SELECT * FROM token_session_table WHERE token = ?", entity.getToken());
    return fromResultSet(resultSet);
  }

  @Override
  public TokenSession update(TokenSession entity) throws SQLException {
    executeUpdate("UPDATE token_session_table SET token = ?, valid_until = ? WHERE username = ?",
        entity.getToken(),
        entity.getValidUntil(),
        entity.getUsername());
    return read(entity);
  }

  @Override
  public TokenSession delete(TokenSession entity) throws SQLException {
    TokenSession tokenSession = read(entity);
    executeUpdate("DELETE FROM token_session_table WHERE username = ?", entity.getUsername());
    return tokenSession;
  }

  @Override
  public TokenSession deleteByToken(TokenSession entity) throws SQLException {
    TokenSession tokenSession = readByToken(entity);
    executeUpdate("DELETE FROM token_session_table WHERE token = ?", entity.getToken());
    return tokenSession;
  }

  private TokenSession fromResultSet(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      TokenSession tokenSession = new TokenSession();
      tokenSession.setUsername(resultSet.getString("username"));
      tokenSession.setToken(resultSet.getString("token"));
      tokenSession.setValidUntil(resultSet.getObject("valid_until", OffsetDateTime.class));
      return tokenSession;
    }
    return null;
  }

}
