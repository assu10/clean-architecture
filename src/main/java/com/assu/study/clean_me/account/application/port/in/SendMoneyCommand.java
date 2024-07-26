package com.assu.study.clean_me.account.application.port.in;

import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.account.domain.Money;
import com.assu.study.clean_me.common.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

// 입력 모델
@Value
public class SendMoneyCommand extends SelfValidating<SendMoneyCommand> {
  @NotNull private final Account.AccountId sourceAccountId;

  @NotNull private final Account.AccountId targetAccountId;

  @NotNull private final Money money;

  public SendMoneyCommand(
      Account.AccountId sourceAccountId, Account.AccountId targetAccountId, Money money) {
    this.sourceAccountId = sourceAccountId;
    this.targetAccountId = targetAccountId;
    this.money = money;

    if (!money.isPositive()) {
      throw new IllegalArgumentException();
    }
    // 각 필드에 지정된 Bean Validation 애너테이션 검증
    this.validateSelf();
  }
}
