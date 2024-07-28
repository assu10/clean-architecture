package com.assu.study.clean_me.account.adapter.out.persistence;

import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.account.domain.Activity;
import com.assu.study.clean_me.account.domain.ActivityWindow;
import com.assu.study.clean_me.account.domain.Money;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
class AccountMapper {
  Account mapToDomainEntity(
      AccountJpaEntity account,
      List<ActivityJpaEntity> activityJpaEntities,
      Long withdrawalBalance,
      Long depositBalance) {

    // 특정 시간(= ActivityWindow) 직전의 계좌 잔고 계산
    Money baselineBalance = Money.subtract(Money.of(depositBalance), Money.of(withdrawalBalance));

    // 도메인 엔티티 반환
    return Account.withId(
        new Account.AccountId(account.getId()),
        baselineBalance,
        mapToActivityWindow(activityJpaEntities));
  }

  // ActivityJpaEntity List 를 ActivityWindow 로 mapping
  ActivityWindow mapToActivityWindow(List<ActivityJpaEntity> activityJpaEntities) {
    List<Activity> mappedActivities = new ArrayList<>();

    for (ActivityJpaEntity activityJpa : activityJpaEntities) {
      mappedActivities.add(
          new Activity(
              new Activity.ActivityId(activityJpa.getId()),
              new Account.AccountId(activityJpa.getOwnerAccountId()),
              new Account.AccountId(activityJpa.getSourceAccountId()),
              new Account.AccountId(activityJpa.getTargetAccountId()),
              activityJpa.getTimestamp(),
              Money.of(activityJpa.getAmount())));
    }

    return new ActivityWindow(mappedActivities);
  }

  // Activity 를 ActivityJpaEntity 로 mapping
  ActivityJpaEntity mapToJpaEntity(Activity activity) {
    return new ActivityJpaEntity(
        activity.getId() == null ? null : activity.getId().getValue(),
        activity.getTimestamp(),
        activity.getOwnerAccountId().getValue(),
        activity.getSourceAccountId().getValue(),
        activity.getTargetAccountId().getValue(),
        activity.getMoney().getAmount().longValue());
  }
}
