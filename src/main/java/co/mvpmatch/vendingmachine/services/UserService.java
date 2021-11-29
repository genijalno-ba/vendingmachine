package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.accesscontrol.IPasswordService;
import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.tokensession.ITokenSessionRepository;
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

  @Inject
  private IPasswordService passwordService;

  @Inject
  private ITokenSessionRepository tokenSessionRepository;

  @Override
  public User createUser(UserContext userContext) {
    hashPassword(userContext);
    try {
      boolean success = 1 == userRepository.createUser(userAdapter.fromContext(userContext));
      if (!success) {
        throw new IUserService.VendingMachineCreateUserException("Could not create User", null);
      }
      co.mvpmatch.vendingmachine.data.user.User userEntity = userRepository.getUser(userContext.getUsername());
      return userAdapter.fromDb(userEntity);
    } catch (SQLException e) {
      if (e.getMessage().contains("violates unique constraint")) {
        throw new IUserService.VendingMachineCreateUserAlreadyExistsException("User already exists", e);
      }
      throw new IUserService.VendingMachineCreateUserException("Could not create User", e);
    }
  }

  private void hashPassword(UserContext userContext) {
    userContext.setPassword(passwordService.digestPassword(userContext.getPassword()));
  }

  @Override
  public User getUserByUsername(String username) {
    try {
      co.mvpmatch.vendingmachine.data.user.User userEntity = userRepository.getUser(username);
      if (null == userEntity) {
        throw new IUserService.VendingMachineUserNotFoundException("Could not find User");
      }
      return userAdapter.fromDb(userEntity);
    } catch (SQLException e) {
      throw new IUserService.VendingMachineUserInternalErrorException("Could not find User, internal error", e);
    }
  }

  @Override
  public User deleteUser(String username) {
    try {
      co.mvpmatch.vendingmachine.data.user.User userEntity = userRepository.getUser(username);
      if (null == userEntity) {
        throw new VendingMachineUserNotFoundException("User does not exist and cannot be deleted");
      }
      tokenSessionRepository.deleteTokenSessionByUsername(username);
      boolean success = 1 == userRepository.deleteUser(username);
      if (!success) {
        throw new IUserService.VendingMachineDeleteUserException("Could not delete User", null);
      }
      return userAdapter.fromDb(userEntity);
    } catch (SQLException e) {
      throw new IUserService.VendingMachineDeleteUserException("Could not delete User, internal error", e);
    }
  }
}
