package co.mvpmatch.vendingmachine.data.user;

import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;

@Contract
public interface IUserRepository {

  User createUser(User user) throws SQLException;

  User getUser(String username) throws SQLException;

  User deleteUser(String username) throws SQLException;
}
