package com.assu.study.clean_me.account.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

// 계좌에 대한 모든 입출금 엔티티
@RequiredArgsConstructor
@Value
public class Activity {

  private ActivityId id;

  @NonNull private final Account.AccountId ownerAccountId;

  // 출금 계좌
  @NonNull private final Account.AccountId sourceAccountId;

  // 입금 계좌
  @NonNull private final Account.AccountId targetAccountId;

  @NonNull private final LocalDateTime timestamp;

  // 계좌 간 오간 금액
  @NonNull @Getter private final Money money;

  public Activity(
      @NonNull Account.AccountId ownerAccountId,
      @NonNull Account.AccountId sourceAccountId,
      @NonNull Account.AccountId targetAccountId,
      @NonNull LocalDateTime timestamp,
      @NonNull Money money) {
    this.id = null;
    this.ownerAccountId = ownerAccountId;
    this.sourceAccountId = sourceAccountId;
    this.targetAccountId = targetAccountId;
    this.timestamp = timestamp;
    this.money = money;
  }

  @Value
  public static class ActivityId {
    private final Long value;
  }
}
