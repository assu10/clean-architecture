package com.assu.study.cleanme.account.application.port.out;

import com.assu.study.cleanme.account.domain.Account;

// 계좌 상태를 업데이트하는 아웃고잉 포트 인터페이스
public interface UpdateAccountStatePort {
  void updateActivities(Account account);
}
