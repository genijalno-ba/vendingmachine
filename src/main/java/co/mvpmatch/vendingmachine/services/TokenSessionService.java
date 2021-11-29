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
public class TokenSessionService implements ITokenSessionService {

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
    TokenSession tokenSession;
    try {
      String username = tokenSessionContext.getUsername();
      String password = tokenSessionContext.getPassword();
      authenticate(username, password);
      tokenSession = issueToken(username);
    } catch (SQLException e) {
      throw new VendingMachineCreateTokenSessionException("Could not create token session", e);
    }
    return tokenSession;
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
    co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSessionEntity =
        tokenSessionRepository.createTokenSession(tokenSessionAdapter.fromContext(username, token, validUntil));
    return tokenSessionAdapter.fromEntity(tokenSessionEntity);
  }

  private OffsetDateTime calculateValidUntil() {
    final Instant NOW = Instant.now();
    return NOW.plus(IAuthenticationTokenIssuer.ONE_DAY).atOffset(ZoneOffset.UTC);
  }

  @Override
  public TokenSession readTokenSession(String token) {
    TokenSession tokenSession;
    try {
      co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSessionEntity =
          tokenSessionRepository.getTokenSessionByToken(token);
      if (null == tokenSessionEntity) {
        throw new VendingMachineTokenSessionNotFoundException("Token session not found");
      }
      tokenSession = tokenSessionAdapter.fromEntity(tokenSessionEntity);
    } catch (SQLException e) {
      throw new VendingMachineReadTokenSessionException("Could not read token session", e);
    }
    return tokenSession;
  }

  @Override
  public TokenSession deleteTokenSession(String token) {
    TokenSession tokenSession;
    try {
      co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSessionEntity =
          tokenSessionRepository.getTokenSessionByToken(token);
      if (null == tokenSessionEntity) {
        throw new VendingMachineDeleteTokenSessionNotFoundException("Token session not found");
      }
      tokenSessionEntity = tokenSessionRepository.deleteTokenSession(token);
      tokenSession = tokenSessionAdapter.fromEntity(tokenSessionEntity);
    } catch (SQLException e) {
      throw new VendingMachineDeleteTokenSessionException("Could not delete token session", e);
    }
    return tokenSession;
  }

}
