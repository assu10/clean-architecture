package com.assu.study.clean_me.account.application.port.in;

import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.account.domain.Money;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class SendMoneyCommand {
  @NotNull private final Account.AccountId sourceAccountId;

  @NotNull private final Account.AccountId targetAccountId;

  @NotNull private final Money money;

  public SendMoneyCommand(
      Account.AccountId sourceAccountId, Account.AccountId targetAccountId, Money money) {
    this.sourceAccountId = sourceAccountId;
    this.targetAccountId = targetAccountId;
    this.money = money;
  }
}
