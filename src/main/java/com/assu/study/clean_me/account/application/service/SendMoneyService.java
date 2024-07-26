package com.assu.study.clean_me.account.application.service;

import com.assu.study.clean_me.account.application.port.in.SendMoneyCommand;
import com.assu.study.clean_me.account.application.port.in.SendMoneyUseCase;
import com.assu.study.clean_me.account.application.port.out.AccountLock;
import com.assu.study.clean_me.account.application.port.out.LoadAccountPort;
import com.assu.study.clean_me.account.application.port.out.UpdateAccountStatePort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

// 인커밍 포트 인터페이스인 SendMoneyUseCase 구현
@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

  // 계좌를 조회하기 위한 아웃고잉 인터페이스
  private final LoadAccountPort loadAccountPort;

  private final AccountLock accountLock;

  // 계좌 상태를 업데이트하기 위한 아웃고잉 인터페이스
  private final UpdateAccountStatePort updateAccountStatePort;

  @Override
  public boolean sendMoney(SendMoneyCommand command) {
    // TODO: 비즈니스 규칙 검증

    // TODO: 모델 상태 조작

    // TODO: 출력값 반환

    return false;
  }
}
