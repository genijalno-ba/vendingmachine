package co.mvpmatch.vendingmachine.data;

import java.sql.SQLException;

public interface IDao<T> {

  T create(T entity) throws SQLException;

  T read(T entity) throws SQLException;

  T update(T entity) throws SQLException;

  T delete(T entity) throws SQLException;
}
