package co.mvpmatch.vendingmachine.accesscontrol;

import co.mvpmatch.vendingmachine.data.tokensession.ITokenSessionRepository;
import co.mvpmatch.vendingmachine.data.tokensession.TokenSession;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.security.Key;
import java.security.Principal;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@SuppressWarnings("unused")
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

  public static final String AUTHENTICATION_SCHEME = "Bearer";
  private static final String REALM = "vendingmachine";

  @Inject
  private IKeyGenerator keyGenerator;

  @Inject
  private ITokenSessionRepository tokenSessionRepository;

  @Override
  public void filter(ContainerRequestContext requestContext) {
    // Get the Authorization header from the request
    String authorizationHeader =
        requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
    // Validate the Authorization header
    if (!isTokenBasedAuthentication(authorizationHeader)) {
      abortWithUnauthorized(requestContext);
      return;
    }
    // Extract the token from the Authorization header
    String token = authorizationHeader
        .substring(AUTHENTICATION_SCHEME.length()).trim();
    try {
      // Validate the token
      validateToken(token);
    } catch (Exception e) {
      abortWithUnauthorized(requestContext);
      return;
    }
    // Identify User
    TokenSession tokenSession;
    try {
      tokenSession = tokenSessionRepository.getTokenSessionByToken(token);
    } catch (SQLException e) {
      Logger.getLogger(AuthenticationFilter.class.getName()).log(Level.SEVERE, null, e);
      abortWithInternalError(requestContext);
      return;
    }
    if (null == tokenSession) {
      abortWithUnauthorized(requestContext);
      return;
    }
    final String username = tokenSession.getUsername();
    final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
    requestContext.setSecurityContext(new SecurityContext() {
      @Override
      public Principal getUserPrincipal() {
        return () -> username;
      }

      @Override
      public boolean isUserInRole(String s) {
        return true;
      }

      @Override
      public boolean isSecure() {
        return currentSecurityContext.isSecure();
      }

      @Override
      public String getAuthenticationScheme() {
        return AUTHENTICATION_SCHEME;
      }
    });
  }

  private boolean isTokenBasedAuthentication(String authorizationHeader) {
    // Check if the Authorization header is valid
    // It must not be null and must be prefixed with "Bearer" plus a whitespace
    // The authentication scheme comparison must be case-insensitive
    return authorizationHeader != null && authorizationHeader.toLowerCase()
        .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
  }

  private void abortWithUnauthorized(ContainerRequestContext requestContext) {
    // Abort the filter chain with a 401 status code response
    // The WWW-Authenticate header is sent along with the response
    requestContext.abortWith(
        Response.status(Response.Status.UNAUTHORIZED)
            .header(HttpHeaders.WWW_AUTHENTICATE,
                AUTHENTICATION_SCHEME + " realm=\"" + REALM + "\"")
            .build());
  }

  private void abortWithInternalError(ContainerRequestContext requestContext) {
    // Abort the filter chain with a 401 status code response
    // The WWW-Authenticate header is sent along with the response
    requestContext.abortWith(
        Response.status(Response.Status.INTERNAL_SERVER_ERROR)
            .build());
  }

  private void validateToken(String token) {
    // Check if the token was issued by the server and if it's not expired
    // Throw an Exception if the token is invalid
    // Validate the token
    Key key = keyGenerator.generateKey();
    Jwts.parser().setSigningKey(keyGenerator.generateKey()).parseClaimsJws(token);
  }
}
