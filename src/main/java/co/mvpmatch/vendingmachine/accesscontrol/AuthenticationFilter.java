package co.mvpmatch.vendingmachine.accesscontrol;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.security.Key;

@SuppressWarnings("unused")
@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

  public static final String AUTHENTICATION_SCHEME = "Bearer";
  private static final String REALM = "vendingmachine";

  @Inject
  private IKeyGenerator keyGenerator;

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
    }
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

  private void validateToken(String token) {
    // Check if the token was issued by the server and if it's not expired
    // Throw an Exception if the token is invalid
    // Validate the token
    Key key = keyGenerator.generateKey();
    Jwts.parser().setSigningKey(keyGenerator.generateKey()).parseClaimsJws(token);
  }
}
