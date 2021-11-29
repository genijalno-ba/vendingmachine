package co.mvpmatch.vendingmachine.accesscontrol;

import co.mvpmatch.vendingmachine.contracts.IUserService;
import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import co.mvpmatch.vendingmachine.data.user.User;
import co.mvpmatch.vendingmachine.rest.ErrorMessage;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("unused")
@Secured
@Provider
@Priority(Priorities.AUTHORIZATION)
public class AuthorizationFilter implements ContainerRequestFilter {

  @Context
  private ResourceInfo resourceInfo;

  @Inject
  private IUserRepository userRepository;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    // Get the resource class which matches with the requested URL
    // Extract the roles declared by it
    Class<?> resourceClass = resourceInfo.getResourceClass();
    Collection<IUserService.Role> classRoles = extractRoles(resourceClass);

    // Get the resource method which matches with the requested URL
    // Extract the roles declared by it
    Method resourceMethod = resourceInfo.getResourceMethod();
    Collection<IUserService.Role> methodRoles = extractRoles(resourceMethod);

    try {
      User user = getUser(requestContext);
      IUserService.Role userRole = user.getRole();
      // Check if the user is allowed to execute the method
      // The method annotations override the class annotations
      if (methodRoles.isEmpty()) {
        checkPermissions(userRole, classRoles);
      } else {
        checkPermissions(userRole, methodRoles);
      }

    } catch (Exception e) {
      requestContext.abortWith(
          Response.status(Response.Status.FORBIDDEN).entity(ErrorMessage.create(e.getMessage())).type(MediaType.APPLICATION_JSON).build());
    }
  }

  private User getUser(ContainerRequestContext requestContext) throws SQLException {
    final String username = requestContext.getSecurityContext().getUserPrincipal().getName();
    return userRepository.getUser(username);
  }

  private Collection<IUserService.Role> extractRoles(AnnotatedElement annotatedElement) {
    if (annotatedElement == null) {
      return new ArrayList<>();
    } else {
      Secured secured = annotatedElement.getAnnotation(Secured.class);
      if (secured == null) {
        return new ArrayList<>();
      } else {
        IUserService.Role[] allowedRoles = secured.value();
        return Arrays.asList(allowedRoles);
      }
    }
  }

  private void checkPermissions(IUserService.Role userRole, Collection<IUserService.Role> allowedRoles) throws VendingMachineUserForbiddenException {
    // Check if the user contains one of the allowed roles
    if (allowedRoles.isEmpty()) {
      // all roles allowed
      return;
    }
    for (IUserService.Role role : allowedRoles) {
      if (role.equals(userRole)) {
        // user role allowed
        return;
      }
    }
    // Throw an Exception if the user has not permission to execute the method
    throw new VendingMachineUserForbiddenException("User is not authorized");
  }

  public static class VendingMachineUserForbiddenException extends Exception {
    public static final long serialVersionUID = 1L;

    public VendingMachineUserForbiddenException(String message) {
      super(message);
    }
  }
}
