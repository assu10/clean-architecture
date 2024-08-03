package com.assu.study.clean_me.account.domain;

import static com.assu.study.clean_me.common.AccountTestData.defaultAccount;
import static com.assu.study.clean_me.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AccountTest {

  @Test
  void calculatesBalance() {
    Account.AccountId accountId = new Account.AccountId(1L);
    Account account =
        defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                new ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L))
                        .build(),
                    defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

    Money balance = account.calculateBalance();
    assertThat(balance).isEqualTo(Money.of(1555L));
  }

  @Test
  void withdrawalSucceeds() {
    Account.AccountId accountId = new Account.AccountId(1L);
    // 특정 상태의 Account 를 인스턴스화함
    Account account =
        defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                new ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L))
                        .build(),
                    defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

    boolean success = account.withdraw(Money.of(555L), new Account.AccountId(998L));

    assertThat(success).isTrue();
    assertThat(account.getActivityWindow().getActivities()).hasSize(3);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(1000L));
  }

  @Test
  void withdrawalFailure() {
    Account.AccountId accountId = new Account.AccountId(1L);
    Account account =
        defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                new ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L))
                        .build(),
                    defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

    boolean success = account.withdraw(Money.of(1556L), new Account.AccountId(111L));

    assertThat(success).isFalse();
    assertThat(account.getActivityWindow().getActivities()).hasSize(2);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(1555L));
  }

  @Test
  void depositSuccess() {
    Account.AccountId accountId = new Account.AccountId(1L);
    Account account =
        defaultAccount()
            .withAccountId(accountId)
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                new ActivityWindow(
                    defaultActivity()
                        .withTargetAccount(accountId)
                        .withMoney(Money.of(999L))
                        .build(),
                    defaultActivity().withTargetAccount(accountId).withMoney(Money.of(1L)).build()))
            .build();

    boolean success = account.deposit(Money.of(445L), new Account.AccountId(111L));

    assertThat(success).isTrue();
    assertThat(account.getActivityWindow().getActivities()).hasSize(3);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(2000L));
  }
}
