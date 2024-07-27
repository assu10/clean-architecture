package com.assu.study.clean_me.account.application.port.in;

import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.account.domain.Money;

// 쿼리(=조회) 를 위한 인커밍 전용 포트
public interface GetAccountBalanceQuery {
  Money getAccountBalance(Account.AccountId accountId);
}
