package co.mvpmatch.vendingmachine.data.tokensession;

import co.mvpmatch.vendingmachine.data.IDao;
import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;

@Contract
public interface ITokenSessionDao extends IDao<TokenSession> {

  TokenSession readByToken(TokenSession entity) throws SQLException;

  int deleteByToken(TokenSession entity) throws SQLException;

  int deleteByUsername(TokenSession tokenSession) throws SQLException;
}
