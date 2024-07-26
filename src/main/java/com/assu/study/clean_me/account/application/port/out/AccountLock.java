package com.assu.study.clean_me.account.application.port.out;

import com.assu.study.clean_me.account.domain.Account;

public interface AccountLock {
  void lockAccount(Account.AccountId accountId);

  void releaseAccount(Account.AccountId accountId);
}
