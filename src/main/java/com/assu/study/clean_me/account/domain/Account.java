package com.assu.study.clean_me.account.domain;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

// 계좌의 현재 스냅샷을 제공하는 엔티티
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {
  private final AccountId id;

  // 계좌의 현재 잔고를 계산하기 한 ActivityWindow 의 첫 번째 활동 바로 전의 잔고
  private final Money baselineBalance;

  // 한 계좌에 대한 모든 활동(activity) 들을 항상 메모리에 한꺼번에 올리지 않고,
  // Account 엔티티는 ActivityWindow 값 객체(VO) 에서 포착한 지난 며칠 혹은 특정 범위에 해당하는 활동만 보유
  private final ActivityWindow activityWindow;

  // ID 가 없는 Account 엔티티 생성
  // 아직 생성되지 않은 새로운 엔티티 생성
  public static Account withoutId(Money baselineBalance, ActivityWindow activityWindow) {
    return new Account(null, baselineBalance, activityWindow);
  }

  public Optional<AccountId> getId() {
    return Optional.ofNullable(this.id);
  }

  // 현재 총 잔액은 기준 잔고(baselineBalance) 에 ActivityWindow 의 모든 활동들의 잔고를 합한 값
  public Money calculateBalance() {
    return Money.add(this.baselineBalance, this.activityWindow.calculateBalance(this.id));
  }

  // 계좌에서 일정 금액 인출 시도
  // 성공한다면 새로운 활동 생성
  // 인출에 성공하면 true 리턴, 실패하면 false 리턴
  public boolean withdraw(Money money, AccountId targetAccountId) {
    if (!mayWithdraw(money)) {
      return false;
    }

    Activity withdrawal =
        new Activity(this.id, this.id, targetAccountId, LocalDateTime.now(), money);
    this.activityWindow.addActivity(withdrawal);

    return true;
  }

  // 출금 가능 상태인지 확인
  private boolean mayWithdraw(Money money) {
    return Money.add(this.calculateBalance(), money.negate()).isPositiveOrZero();
  }

  // 계좌에 일정 금액 입금
  // 성공한다면 새로운 활동 생성
  // 입금에 성공하면 true 리턴, 실패하면 false 리턴
  public boolean deposit(Money money, AccountId sourceAccountId) {
    Activity deposit = new Activity(this.id, sourceAccountId, this.id, LocalDateTime.now(), money);
    this.activityWindow.addActivity(deposit);

    return true;
  }

  @Value
  public static class AccountId {
    private Long value;
  }
}
