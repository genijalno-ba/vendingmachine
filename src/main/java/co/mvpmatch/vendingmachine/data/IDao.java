package co.mvpmatch.vendingmachine.data;

import java.sql.SQLException;

public interface IDao<T> {

  int create(T entity) throws SQLException;

  T read(T entity) throws SQLException;

  int update(T entity) throws SQLException;

  int delete(T entity) throws SQLException;
}
