package co.mvpmatch.vendingmachine.accesscontrol;

import org.jvnet.hk2.annotations.Contract;

import java.time.Duration;
import java.time.temporal.TemporalAmount;

@SuppressWarnings("unused")
@Contract
public interface IAuthenticationTokenIssuer {

  TemporalAmount ONE_DAY = Duration.ofDays(1);

  String issueToken(String subject);
}
