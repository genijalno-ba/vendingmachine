package co.mvpmatch.vendingmachine.data.tokensession;

import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;

@Contract
public interface ITokenSessionRepository {

  int createTokenSession(TokenSession tokenSession) throws SQLException;

  TokenSession getTokenSessionByUsername(String username) throws SQLException;

  TokenSession getTokenSessionByToken(String token) throws SQLException;

  int deleteTokenSession(String token) throws SQLException;

  int deleteTokenSessionByUsername(String username) throws SQLException;
}
