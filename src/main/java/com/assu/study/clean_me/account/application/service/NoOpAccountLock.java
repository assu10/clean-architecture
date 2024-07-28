package com.assu.study.clean_me.account.application.service;

import com.assu.study.clean_me.account.application.port.out.AccountLock;
import com.assu.study.clean_me.account.domain.Account;
import org.springframework.stereotype.Component;

@Component
public class NoOpAccountLock implements AccountLock {
  @Override
  public void lockAccount(Account.AccountId accountId) {}

  @Override
  public void releaseAccount(Account.AccountId accountId) {}
}