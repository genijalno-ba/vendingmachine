package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.accesscontrol.IAuthenticationTokenIssuer;
import co.mvpmatch.vendingmachine.accesscontrol.IPasswordService;
import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.data.tokensession.ITokenSessionRepository;
import co.mvpmatch.vendingmachine.data.user.IUserRepository;
import co.mvpmatch.vendingmachine.data.user.User;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.jvnet.hk2.annotations.Service;

import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@SuppressWarnings("unused")
@Service
public class TokenSessionService extends AbstractService implements ITokenSessionService {

  @Inject
  private ITokenSessionRepository tokenSessionRepository;

  @Inject
  private TokenSessionAdapter tokenSessionAdapter;

  @Inject
  private IUserRepository userRepository;

  @Inject
  @Named("jwt")
  private IAuthenticationTokenIssuer jwtTokenIssuer;

  @Inject
  private IPasswordService passwordService;

  @Override
  public TokenSession createTokenSession(TokenSessionContext tokenSessionContext) {
      String username = tokenSessionContext.getUsername();
      String password = tokenSessionContext.getPassword();
    try {
      authenticate(username, password);
      return issueToken(username);
    } catch (SQLException e) {
      if (!e.getMessage().contains("violates unique constraint")) {
        throw new VendingMachineCreateTokenSessionException("Could not create token session", e);
      }
    }
    try {
      boolean success = 1 == tokenSessionRepository.deleteTokenSessionByUsername(username);
      if (!success) {
        throw new ITokenSessionService.VendingMachineCreateTokenSessionException("Could not delete existing token session. Could not recreate token session", null);
      }
      return issueToken(username);
    }catch (SQLException e) {
      throw new VendingMachineCreateTokenSessionException("Token session already exists. Could not recreate token session", e);
    }
  }

  private void authenticate(String username, String password) throws SQLException {
    User user = userRepository.getUser(username);
    if (null == user) {
      throw new VendingMachineAuthenticateException("Username or password wrong");
    }
    String passwordHash = passwordService.digestPassword(password);
    if (!user.getPassword().equals(passwordHash)) {
      throw new VendingMachineAuthenticateException("Username or password wrong");
    }
  }

  private TokenSession issueToken(String username) throws SQLException {
    String token = jwtTokenIssuer.issueToken(username);
    OffsetDateTime validUntil = calculateValidUntil();
    co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSession = tokenSessionAdapter.fromContext(username, token, validUntil);
    boolean success =
        1 == tokenSessionRepository.createTokenSession(tokenSession);
    if (!success) {
      throw new VendingMachineAuthenticateIssueTokenException("Could not create token session");
    }
    return tokenSessionAdapter.fromEntity(tokenSession);
  }

  private OffsetDateTime calculateValidUntil() {
    final Instant NOW = Instant.now();
    return NOW.plus(IAuthenticationTokenIssuer.ONE_DAY).atOffset(ZoneOffset.UTC);
  }

  @Override
  public TokenSession readTokenSession(String token) {
    try {
      co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSessionEntity =
          tokenSessionRepository.getTokenSessionByToken(token);
      if (null == tokenSessionEntity) {
        throw new VendingMachineTokenSessionNotFoundException("Token session not found");
      }
      return tokenSessionAdapter.fromEntity(tokenSessionEntity);
    } catch (SQLException e) {
      throw new VendingMachineReadTokenSessionException("Could not read token session", e);
    }
  }

  @Override
  public TokenSession deleteTokenSession(String token) {
    try {
      co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSessionEntity =
          tokenSessionRepository.getTokenSessionByToken(token);
      if (null == tokenSessionEntity) {
        throw new VendingMachineDeleteTokenSessionNotFoundException("Token session not found");
      }
      boolean success = 1 == tokenSessionRepository.deleteTokenSession(token);
      if (!success) {
        throw new VendingMachineDeleteTokenSessionException("Could not delete token session", null);
      }
      return tokenSessionAdapter.fromEntity(tokenSessionEntity);
    } catch (SQLException e) {
      throw new VendingMachineDeleteTokenSessionException("Could not delete token session", e);
    }
  }

}
