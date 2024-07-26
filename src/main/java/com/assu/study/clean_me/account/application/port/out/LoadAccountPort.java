package com.assu.study.clean_me.account.application.port.out;

import com.assu.study.clean_me.account.domain.Account;
import java.time.LocalDateTime;

// 계좌를 조회하는 아웃고잉 포트 인터페이스
public interface LoadAccountPort {
  Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate);
}
