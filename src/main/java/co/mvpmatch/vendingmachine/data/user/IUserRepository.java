package co.mvpmatch.vendingmachine.data.user;

import org.jvnet.hk2.annotations.Contract;

import java.sql.SQLException;

@Contract
public interface IUserRepository {

  int createUser(User user) throws SQLException;

  User getUser(String username) throws SQLException;

  int deleteUser(String username) throws SQLException;
}
