package com.assu.study.cleanme;

import static org.assertj.core.api.BDDAssertions.then;

import com.assu.study.cleanme.account.application.port.out.LoadAccountPort;
import com.assu.study.cleanme.account.domain.Account;
import com.assu.study.cleanme.account.domain.Money;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SendMoneySystemTest {
  @Autowired private TestRestTemplate testRestTemplate;
  @Autowired private LoadAccountPort loadAccountPort;

  @DisplayName("송금하기 시스템 테스트")
  @Test
  @Sql("SendMoneySystemTest.sql")
  void sendMoney() {
    // Given (테스트 케이스가 동작하기 위해 갖춰져야 하는 선행 조건 준비)
    Money initialSourceBalance = sourceAccount().calculateBalance();
    Money initialTargetBalance = targetAccount().calculateBalance();

    // When (테스트하고자 하는 대상 코드 실행)
    ResponseEntity response =
        whenSendMoney(sourceAccountId(), targetAccountId(), transferredAmount());

    // Then (대상 코드의 수행 결과 판단)
    then(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    then(sourceAccount().calculateBalance())
        .isEqualTo(initialSourceBalance.minus(transferredAmount()));

    then(targetAccount().calculateBalance())
        .isEqualTo(initialTargetBalance.plus(transferredAmount()));
  }

  // 여기서부터 헬퍼 메서드들 (DSL)
  private Account sourceAccount() {
    return loadAccount(sourceAccountId());
  }

  private Account targetAccount() {
    return loadAccount(targetAccountId());
  }

  private Account loadAccount(Account.AccountId accountId) {
    return loadAccountPort.loadAccount(accountId, LocalDateTime.now());
  }

  private Account.AccountId sourceAccountId() {
    return new Account.AccountId(1L);
  }

  private Account.AccountId targetAccountId() {
    return new Account.AccountId(2L);
  }

  private Money transferredAmount() {
    return Money.of(500L);
  }

  private ResponseEntity whenSendMoney(
      Account.AccountId sourceAccountId, Account.AccountId targetAccountId, Money amount) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
    HttpEntity<Void> request = new HttpEntity<>(null, headers);

    return testRestTemplate.exchange(
        "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
        HttpMethod.POST,
        request,
        Object.class,
        sourceAccountId.getValue(),
        targetAccountId.getValue(),
        amount.getAmount());
  }
}
