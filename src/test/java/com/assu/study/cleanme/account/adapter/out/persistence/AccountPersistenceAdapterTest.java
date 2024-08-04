package com.assu.study.cleanme.account.adapter.out.persistence;

import static com.assu.study.cleanme.common.AccountTestData.defaultAccount;
import static com.assu.study.cleanme.common.ActivityTestData.defaultActivity;
import static org.assertj.core.api.Assertions.assertThat;

import com.assu.study.cleanme.account.domain.Account;
import com.assu.study.cleanme.account.domain.ActivityWindow;
import com.assu.study.cleanme.account.domain.Money;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({AccountPersistenceAdapter.class, AccountMapper.class})
class AccountPersistenceAdapterTest {
  @Autowired private AccountPersistenceAdapter accountPersistenceAdapter;

  @Autowired private ActivityRepository activityRepository;

  @DisplayName("계좌 조회")
  @Test
  @Sql("AccountPersistenceAdapterTest.sql") // DB 를 특정 상태로 만듦
  void loadAccount() {
    // When
    // 어댑터를 이용하여 계좌 조회
    Account account =
        accountPersistenceAdapter.loadAccount(
            new Account.AccountId(1L), LocalDateTime.of(2023, 8, 10, 0, 0));

    // Then
    // Sql 스크립트에서 설정한 상태값을 가지고 있는지 검증
    assertThat(account.getActivityWindow().getActivities()).hasSize(2);
    assertThat(account.calculateBalance()).isEqualTo(Money.of(500));
  }

  @DisplayName("새로운 계좌 활동 DB 에 저장")
  @Test
  void updateActivities() {
    // Given
    // 새로운 계좌 활동을 가진 Account 를 만듦
    Account account =
        defaultAccount()
            .withBaselineBalance(Money.of(555L))
            .withActivityWindow(
                new ActivityWindow(defaultActivity().withId(null).withMoney(Money.of(1L)).build()))
            .build();

    // When
    // 위에서 만든 Account 를 어댑터로 전달
    accountPersistenceAdapter.updateActivities(account);

    // Then
    // DB 에 잘 저장되었는지 확인
    assertThat(activityRepository.count()).isEqualTo(1);

    ActivityJpaEntity savedActivity = activityRepository.findAll().get(0);
    assertThat(savedActivity.getAmount()).isEqualTo(1L);
  }
}
