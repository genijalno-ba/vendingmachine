package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import co.mvpmatch.vendingmachine.data.user.User;
import jakarta.ws.rs.core.SecurityContext;

import java.sql.SQLException;

public class AbstractService {

  protected User getLoggedUser(SecurityContext securityContext, IUserRepository userRepository) throws SQLException {
    String username = securityContext.getUserPrincipal().getName();
    return userRepository.getUser(username);
  }
}
