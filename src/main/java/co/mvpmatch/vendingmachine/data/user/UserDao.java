package co.mvpmatch.vendingmachine.data.user;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.AbstractDao;
import org.jvnet.hk2.annotations.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("unused")
@Service
public class UserDao extends AbstractDao<User> implements IUserDao {

  @Override
  public int create(User entity) throws SQLException {
    return executeUpdate("INSERT INTO user_table(username, password, deposit, role) VALUES (?, ?, ?, ?)",
        entity.getUsername(),
        entity.getPassword(),
        entity.getDeposit(),
        entity.getRole().name());
  }

  @Override
  public User read(User entity) throws SQLException {
    return executeQuery("SELECT * FROM user_table WHERE username = ?",
        entity.getUsername());
  }

  @Override
  public int update(User entity) throws SQLException {
    return executeUpdate("UPDATE user_table SET deposit = ?, role = ? WHERE username = ?",
        entity.getDeposit(),
        entity.getRole().name(),
        entity.getUsername());
  }

  @Override
  public int delete(User entity) throws SQLException {
    return executeUpdate("DELETE FROM user_table WHERE username = ?",
        entity.getUsername());
  }

  protected User fromResultSet(ResultSet resultSet) throws SQLException {
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

}
