package com.assu.study.clean_me.account.adapter.out.persistence;

import com.assu.study.clean_me.account.application.port.out.LoadAccountPort;
import com.assu.study.clean_me.account.application.port.out.UpdateAccountStatePort;
import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.common.PersistenceAdapter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@PersistenceAdapter
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountStatePort {
  @Override
  public Account loadAccount(Account.AccountId accountId, LocalDateTime baselineDate) {
    return null;
  }

  @Override
  public void updateActivities(Account account) {}
}
