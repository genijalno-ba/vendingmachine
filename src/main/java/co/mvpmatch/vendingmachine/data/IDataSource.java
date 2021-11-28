package co.mvpmatch.vendingmachine.data;

import org.jvnet.hk2.annotations.Contract;

import java.sql.Connection;
import java.sql.SQLException;

@Contract
public interface IDataSource {

  Connection getConnection() throws SQLException;

}
