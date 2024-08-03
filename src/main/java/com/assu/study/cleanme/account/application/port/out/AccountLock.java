package com.assu.study.cleanme.account.application.port.out;

import com.assu.study.cleanme.account.domain.Account;

public interface AccountLock {
  void lockAccount(Account.AccountId accountId);

  void releaseAccount(Account.AccountId accountId);
}
