package co.mvpmatch.vendingmachine.services;

import co.mvpmatch.vendingmachine.contracts.ITokenSessionService;
import co.mvpmatch.vendingmachine.data.tokensession.ITokenSessionRepository;
import jakarta.inject.Inject;
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

  @Override
  public TokenSession createTokenSession(TokenSessionContext tokenSessionContext) {
    TokenSession tokenSession;
    try {
      String username = tokenSessionContext.getUsername();
      String token = "xyz";
      OffsetDateTime validUntil = calculateValidUntil();
      //TODO: create JWT token
      co.mvpmatch.vendingmachine.data.tokensession.TokenSession tokenSessionEntity =
          tokenSessionRepository.createTokenSession(tokenSessionAdapter.fromContext(username, token, validUntil));
      tokenSession = tokenSessionAdapter.fromEntity(tokenSessionEntity);
    } catch (SQLException e) {
      throw new VendingMachineCreateTokenSessionException("Could not create token session", e);
    }
    return tokenSession;
  }

  private OffsetDateTime calculateValidUntil() {
    final Instant NOW = Instant.now();
    return NOW.plus(ONE_DAY).atOffset(ZoneOffset.UTC);
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
