package com.assu.study.cleanme.account.application.port.in;

// 인커밍 포트 인터페이스
public interface SendMoneyUseCase {
  boolean sendMoney(SendMoneyCommand command);
}
