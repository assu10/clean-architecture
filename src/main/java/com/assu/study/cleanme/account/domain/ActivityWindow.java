package com.assu.study.cleanme.account.domain;

import java.time.LocalDateTime;
import java.util.*;
import lombok.NonNull;

// 현재 계좌의 전체 계좌 활동 리스트에서 특정 범위의 계좌 활동만 볼수있는 범위
public class ActivityWindow {
  // 범위 안에서의 계좌 활동 리스트
  private List<Activity> activities;

  // 범위 안에서 첫 번째 활동의 시간
  public LocalDateTime getStartTimestamp() {
    return activities.stream()
        .min(Comparator.comparing(Activity::getTimestamp))
        .orElseThrow(IllegalStateException::new)
        .getTimestamp();
  }

  // 범위 안에서 마지막 활동의 시간
  public LocalDateTime getEndTimestamp() {
    return activities.stream()
        .max(Comparator.comparing(Activity::getTimestamp))
        .orElseThrow(IllegalStateException::new)
        .getTimestamp();
  }

  // 해당 기간동안 입금액과 출금액
  public Money calculateBalance(Account.AccountId accountId) {
    // 입금액
    Money depositBalance =
        activities.stream()
            .filter(a -> a.getTargetAccountId().equals(accountId))
            .map(Activity::getMoney)
            .reduce(Money.ZERO, Money::add);

    // 출금액
    Money withdrawalBalance =
        activities.stream()
            .filter(a -> a.getSourceAccountId().equals(accountId))
            .map(Activity::getMoney)
            .reduce(Money.ZERO, Money::add);

    return Money.add(depositBalance, withdrawalBalance.negate());
  }

  public ActivityWindow(@NonNull List<Activity> activities) {
    this.activities = activities;
  }

  public ActivityWindow(@NonNull Activity... activities) {
    this.activities = new ArrayList<>(Arrays.asList(activities));
  }

  public List<Activity> getActivities() {
    return Collections.unmodifiableList(this.activities);
  }

  public void addActivity(Activity activity) {
    this.activities.add(activity);
  }
}
