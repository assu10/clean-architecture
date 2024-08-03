package com.assu.study.cleanme.account.application.service;

import com.assu.study.cleanme.account.application.port.in.GetAccountBalanceQuery;
import com.assu.study.cleanme.account.application.port.out.LoadAccountPort;
import com.assu.study.cleanme.account.domain.Account;
import com.assu.study.cleanme.account.domain.Money;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

// 조회를 위한 서비스
@RequiredArgsConstructor
public class GetAccountBalanceService implements GetAccountBalanceQuery {
  // DB 로부터 데이터 로드를 위해 호출하는 아웃고잉 포트
  private final LoadAccountPort loadAccountPort;

  @Override
  public Money getAccountBalance(Account.AccountId accountId) {
    return loadAccountPort.loadAccount(accountId, LocalDateTime.now()).calculateBalance();
  }
}
