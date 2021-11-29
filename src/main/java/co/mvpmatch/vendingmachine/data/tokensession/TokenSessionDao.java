package co.mvpmatch.vendingmachine.data.tokensession;

import co.mvpmatch.vendingmachine.data.AbstractDao;
import org.jvnet.hk2.annotations.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@SuppressWarnings("unused")
@Service
public class TokenSessionDao extends AbstractDao<TokenSession> implements ITokenSessionDao {

  @Override
  public int create(TokenSession entity) throws SQLException {
    return executeUpdate("INSERT INTO token_session_table (username, token, valid_until) VALUES (?, ?, ?)",
        entity.getUsername(),
        entity.getToken(),
        entity.getValidUntil());
  }

  @Override
  public TokenSession read(TokenSession entity) throws SQLException {
    return executeQuery("SELECT * FROM token_session_table WHERE username = ?",
        entity.getUsername());
  }

  @Override
  public TokenSession readByToken(TokenSession entity) throws SQLException {
    return executeQuery("SELECT * FROM token_session_table WHERE token = ?",
        entity.getToken());
  }

  @Override
  public int update(TokenSession entity) throws SQLException {
    return executeUpdate("UPDATE token_session_table SET token = ?, valid_until = ? WHERE username = ?",
        entity.getToken(),
        entity.getValidUntil(),
        entity.getUsername());
  }

  @Override
  public int delete(TokenSession entity) throws SQLException {
    return executeUpdate("DELETE FROM token_session_table WHERE username = ?",
        entity.getUsername());
  }

  @Override
  public int deleteByToken(TokenSession entity) throws SQLException {
    return executeUpdate("DELETE FROM token_session_table WHERE token = ?", entity.getToken());
  }

  protected TokenSession fromResultSet(ResultSet resultSet) throws SQLException {
    TokenSession tokenSession = new TokenSession();
    tokenSession.setUsername(resultSet.getString("username"));
    tokenSession.setToken(resultSet.getString("token"));
    tokenSession.setValidUntil(resultSet.getObject("valid_until", OffsetDateTime.class));
    return tokenSession;
  }

}
