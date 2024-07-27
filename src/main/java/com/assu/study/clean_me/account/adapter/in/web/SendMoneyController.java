package com.assu.study.clean_me.account.adapter.in.web;

import com.assu.study.clean_me.account.application.port.in.SendMoneyCommand;
import com.assu.study.clean_me.account.application.port.in.SendMoneyUseCase;
import com.assu.study.clean_me.account.domain.Account;
import com.assu.study.clean_me.account.domain.Money;
import com.assu.study.clean_me.common.WebAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@WebAdapter
@RestController
@RequiredArgsConstructor
class SendMoneyController {
  private final SendMoneyUseCase sendMoneyUseCase;

  @PostMapping("accounts/send/{sourceAccountId}/{targetAccountId}/{amount}")
  void sendMoney(
      @PathVariable("sourceAccountId") Long sourceAccountId,
      @PathVariable("targetAccountId") Long targetAccountId,
      @PathVariable("amount") Long amount) {
    SendMoneyCommand command =
        new SendMoneyCommand(
            new Account.AccountId(sourceAccountId),
            new Account.AccountId(targetAccountId),
            Money.of(amount));

    sendMoneyUseCase.sendMoney(command);
  }
}
