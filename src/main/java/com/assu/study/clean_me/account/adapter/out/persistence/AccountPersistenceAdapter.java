package com.assu.study.clean_me.account.adapter.out.persistence;

import com.assu.study.clean_me.account.application.port.out.LoadAccountPort;
import com.assu.study.clean_me.account.application.port.out.UpdateAccountStatePort;
import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.account.domain.Activity;
import com.assu.study.clean_me.common.PersistenceAdapter;
import jakarta.persistence.EntityExistsException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {

  private final AccountRepository accountRepository;
  private final ActivityRepository activityRepository;
  private final AccountMapper accountMapper;

  // Account 도메인 엔티티 반환
  @Override
  public Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate) {
    // 계좌 정보 조회
    AccountJpaEntity account =
        accountRepository.findById(accountId.getValue()).orElseThrow(EntityExistsException::new);

    // 해당 계좌의 특정 시간까지의 활동 조회
    List<ActivityJpaEntity> activityJpaEntities =
        activityRepository.findByOwnerSince(accountId.getValue(), baselineDate);

    // 특정 시간까지의 모든 출금 정보 조회
    Long withdrawalBalance =
        orZero(activityRepository.getWithdrawalBalanceUntil(account.getId(), baselineDate));

    // 특정 시간까지의 모든 입금 정보 조회
    Long depositBalance =
        orZero(activityRepository.getDepositBalanceUntil(accountId.getValue(), baselineDate));

    return accountMapper.mapToDomainEntity(
        account, activityJpaEntities, withdrawalBalance, depositBalance);
  }

  private Long orZero(Long value) {
    return value == null ? 0L : value;
  }

  // 계좌 상태 업데이트
  @Override
  public void updateActivities(Account account) {
    // Account 엔티티의 모든 활동을 순회하여 id 가 있는지 확인 후 없다면 새로운 활동 저장
    for (Activity activity : account.getActivityWindow().getActivities()) {
      if (activity.getId() == null) {
        activityRepository.save(accountMapper.mapToJpaEntity(activity));
      }
    }
  }
}
