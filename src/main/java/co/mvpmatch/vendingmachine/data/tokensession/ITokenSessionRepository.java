package co.mvpmatch.vendingmachine.data.tokensession;

import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;

@Contract
public interface ITokenSessionRepository {

  TokenSession createTokenSession(TokenSession tokenSession) throws SQLException;

  TokenSession getTokenSessionByUsername(String username) throws SQLException;

  TokenSession getTokenSessionByToken(String token) throws SQLException;

  TokenSession deleteTokenSession(String token) throws SQLException;
}
