package co.mvpmatch.vendingmachine.accesscontrol;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jvnet.hk2.annotations.Service;

import java.time.Instant;
import java.util.Date;

@SuppressWarnings("unused")
@Service
@Named("jwt")
public class JwtTokenIssuer implements IAuthenticationTokenIssuer {

  @Context
  private UriInfo uriInfo;

  @Inject
  private IKeyGenerator keyGenerator;

  @Override
  public String issueToken(String subject) {
    final Instant NOW = Instant.now();
    return Jwts.builder()
        .setSubject(subject)
        .setIssuer(uriInfo.getAbsolutePath().toString())
        .setIssuedAt(Date.from(NOW))
        .setExpiration(Date.from(NOW.plus(ONE_DAY)))
        .signWith(SignatureAlgorithm.HS256, keyGenerator.generateKey())
        .compact();
  }
}
