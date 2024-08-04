package com.assu.study.cleanme.account.application.service;

import com.assu.study.cleanme.account.application.port.in.SendMoneyCommand;
import com.assu.study.cleanme.account.application.port.in.SendMoneyUseCase;
import com.assu.study.cleanme.account.application.port.out.AccountLock;
import com.assu.study.cleanme.account.application.port.out.LoadAccountPort;
import com.assu.study.cleanme.account.application.port.out.UpdateAccountStatePort;
import com.assu.study.cleanme.account.domain.Account;
import com.assu.study.cleanme.common.UseCase;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

// 인커밍 포트 인터페이스인 SendMoneyUseCase 구현
@UseCase
@Slf4j
@RequiredArgsConstructor
@Transactional
public class SendMoneyService implements SendMoneyUseCase {

  // 계좌를 조회하기 위한 아웃고잉 인터페이스
  private final LoadAccountPort loadAccountPort;

  private final AccountLock accountLock;

  // 계좌 상태를 업데이트하기 위한 아웃고잉 인터페이스
  private final UpdateAccountStatePort updateAccountStatePort;

  private final MoneyTransferProperties moneyTransferProperties;

  // 1. 비즈니스 규칙 검증
  // 2. 모델 상태 조작
  // 3. 출력값 반환
  @Override
  public boolean sendMoney(SendMoneyCommand command) {
    System.out.println("=================== service");
    // 1. 비즈니스 규칙 검증

    // 이체 가능한 최대 한도를 넘는지 검사
    checkThreshold(command);

    // 오늘로부터 -10 일
    LocalDateTime baselineDate = LocalDateTime.now().minusDays(10);

    // 최근 10일 이내의 거래내역이 있는 계좌 정보 확인
    Account sourceAccount = loadAccountPort.loadAccount(command.getSourceAccountId(), baselineDate);
    Account targetAccount = loadAccountPort.loadAccount(command.getTargetAccountId(), baselineDate);

    // 입출금 계좌 아이디가 존재하는지 확인
    Account.AccountId sourceAccountId =
        sourceAccount
            .getId()
            .orElseThrow(() -> new IllegalStateException("source accountId not to be empty"));

    Account.AccountId targetAccountId =
        targetAccount
            .getId()
            .orElseThrow(() -> new IllegalStateException("target accountId not to be empty"));

    // 출금 계좌의 잔고가 다른 트랜잭션에 의해 변경되지 않도록 lock 을 검
    accountLock.lockAccount(sourceAccountId);

    // 출금 계좌에서 출금을 한 후 lock 해제
    if (!sourceAccount.withdraw(command.getMoney(), targetAccountId)) {
      accountLock.releaseAccount(sourceAccountId);
      return false;
    }

    // 출금 후 입금 계좌에 lock 을 건 후 입금 처리
    accountLock.lockAccount(targetAccountId);
    if (!targetAccount.deposit(command.getMoney(), sourceAccountId)) {
      accountLock.releaseAccount(sourceAccountId);
      accountLock.releaseAccount(targetAccountId);
      return false;
    }

    // 2. 모델 상태 조작
    updateAccountStatePort.updateActivities(sourceAccount);
    updateAccountStatePort.updateActivities(targetAccount);

    accountLock.releaseAccount(sourceAccountId);
    accountLock.releaseAccount(targetAccountId);

    // 3. 출력값 반환
    return true;
  }

  private void checkThreshold(SendMoneyCommand command) {
    if (command
        .getMoney()
        .isGreaterThenOrEqualTo(moneyTransferProperties.getMaximumTransferThreshold())) {
      throw new ThresholdExceededException(
          moneyTransferProperties.getMaximumTransferThreshold(), command.getMoney());
    }
  }
}
