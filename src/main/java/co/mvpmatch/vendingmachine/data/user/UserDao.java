package co.mvpmatch.vendingmachine.data.user;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.AbstractDao;
import org.jvnet.hk2.annotations.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class UserDao extends AbstractDao implements IUserDao {

  @Override
  public User create(User entity) throws SQLException {
    boolean success = 1 == executeUpdate("INSERT INTO user_table(username, password, deposit, role) VALUES (?, ?, ?, ?)",
        entity.getUsername(),
        entity.getPassword(),
        entity.getDeposit(),
        entity.getRole().name());
    if (success) {
      return read(entity);
    }
    return null;
  }

  @Override
  public User read(User entity) throws SQLException {
    ResultSet resultSet = executeQuery("SELECT * FROM user_table WHERE username = ?", entity.getUsername());
    return fromResultSet(resultSet);
  }

  @Override
  public User update(User entity) throws SQLException {
    executeUpdate("UPDATE user_table SET deposit = ?, role = ? WHERE username = ?",
        entity.getDeposit(),
        entity.getRole(),
        entity.getUsername());
    return read(entity);
  }

  @Override
  public User delete(User entity) throws SQLException {
    User user = read(entity);
    executeUpdate("DELETE FROM user_table WHERE username = ?", entity.getUsername());
    return user;
  }

  private User fromResultSet(ResultSet resultSet) throws SQLException {
    if (resultSet.next()) {
      User user = new User();
      user.setUsername(resultSet.getString("username"));
      user.setDeposit(resultSet.getBigDecimal("deposit"));
      user.setPassword(resultSet.getString("password"));
      String role = resultSet.getString("role");
      if (null != role) {
        user.setRole(IUserService.Role.valueOf(role));
      }
      return user;
    }
    return null;
  }

}
