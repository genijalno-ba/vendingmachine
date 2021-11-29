package co.mvpmatch.vendingmachine.data;

import jakarta.inject.Inject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractDao<T> {

  @Inject
  IDataSource dataSource;

  protected abstract T fromResultSet(ResultSet resultSet) throws SQLException;

  protected T executeQuery(String sql, Object... args) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = getPreparedStatement(connection, sql, args);
      ResultSet resultSet = preparedStatement.executeQuery();
      if (resultSet.next()) {
        return fromResultSet(resultSet);
      }
    }
    return null;
  }

  protected Collection<T> executeQueryCollection(String sql, Object... args) throws SQLException {
    Collection<T> result = new ArrayList<>();
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = getPreparedStatement(connection, sql, args);
      ResultSet resultSet = preparedStatement.executeQuery();
      while (resultSet.next()) {
        result.add(fromResultSet(resultSet));
      }
    }
    return result;
  }

  protected int executeUpdate(String sql, Object... args) throws SQLException {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = getPreparedStatement(connection, sql, args);
      return preparedStatement.executeUpdate();
    }
  }

  private PreparedStatement getPreparedStatement(Connection connection, String sql, Object... args) throws SQLException {
    PreparedStatement preparedStatement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    int idx = 1;
    for (Object arg : args) {
      preparedStatement.setObject(idx, arg);
      idx++;
    }
    Logger.getLogger(AbstractDao.class.getName()).log(Level.INFO, "SQL: " + preparedStatement.toString());
    return preparedStatement;
  }
}
