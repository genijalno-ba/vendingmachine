package co.mvpmatch.vendingmachine.data.user;

import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.SQLException;

@SuppressWarnings("unused")
@Service
public class UserRepository implements IUserRepository {

  @Inject
  private IUserDao userDao;

  @Override
  public int createUser(User user) throws SQLException {
    return userDao.create(user);
  }

  @Override
  public User getUser(String username) throws SQLException {
    User user = new User();
    user.setUsername(username);
    return userDao.read(user);
  }

  @Override
  public int deleteUser(String username) throws SQLException {
    User user = new User();
    user.setUsername(username);
    return userDao.delete(user);
  }
}
