package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import jakarta.inject.Inject;
import org.jvnet.hk2.annotations.Service;

import java.sql.SQLException;

@SuppressWarnings("unused")
@Service
public class UserService implements IUserService {

  @Inject
  private IUserRepository userRepository;

  @Inject
  private UserAdapter userAdapter;

  @Override
  public User createUser(UserContext userContext) {
    User user;
    try {
      co.mvpmatch.vendingmachine.data.user.User userEntity = userRepository.createUser(userAdapter.fromContext(userContext));
      user = userAdapter.fromDb(userEntity);
    } catch (SQLException e) {
      throw new IUserService.VendingMachineCreateUserException("Could not create User", e);
    }
    return user;
  }

  @Override
  public User getUserByUsername(String username) {
    User user;
    try {
      co.mvpmatch.vendingmachine.data.user.User userEntity = userRepository.getUser(username);
      if (null == userEntity) {
        throw new IUserService.VendingMachineUserNotFoundException("Could not find User");
      }
      user = userAdapter.fromDb(userEntity);
    } catch (SQLException e) {
      throw new IUserService.VendingMachineUserInternalErrorException("Could not find User, internal error", e);
    }
    return user;
  }

  @Override
  public User deleteUser(String username) {
    User user;
    try {
      co.mvpmatch.vendingmachine.data.user.User userEntity = userRepository.getUser(username);
      if (null == userEntity) {
        throw new VendingMachineUserNotFoundException("User does not exist and cannot be deleted");
      }
      userEntity = userRepository.deleteUser(username);
      if (null == userEntity) {
        throw new IUserService.VendingMachineDeleteUserException("Could not delete User", null);
      }
      user = userAdapter.fromDb(userEntity);
    } catch (SQLException e) {
      throw new IUserService.VendingMachineDeleteUserException("Could not delete User, internal error", e);
    }
    return user;
  }
}
