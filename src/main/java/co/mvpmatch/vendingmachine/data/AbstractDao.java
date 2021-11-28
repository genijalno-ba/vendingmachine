package co.mvpmatch.vendingmachine.data;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDao {

  @Inject
  IDataSource dataSource;

  protected ResultSet executeQuery(String sql, Object... args) throws SQLException {
    PreparedStatement preparedStatement = getPreparedStatement(sql, args);
    return preparedStatement.executeQuery();
  }

  protected int executeUpdate(String sql, Object... args) throws SQLException {
    PreparedStatement preparedStatement = getPreparedStatement(sql, args);
    return preparedStatement.executeUpdate();
  }

  private PreparedStatement getPreparedStatement(String sql, Object... args) throws SQLException {
    Connection connection = dataSource.getConnection();
    PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    int idx = 1;
    for (Object arg : args) {
      preparedStatement.setObject(idx, arg);
      idx++;
    }
    return preparedStatement;
  }
}
