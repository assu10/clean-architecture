package com.assu.study.cleanme.account.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.assu.study.cleanme.account.application.port.in.SendMoneyCommand;
import com.assu.study.cleanme.account.application.port.out.AccountLock;
import com.assu.study.cleanme.account.application.port.out.LoadAccountPort;
import com.assu.study.cleanme.account.application.port.out.UpdateAccountStatePort;
import com.assu.study.cleanme.account.domain.Account;
import com.assu.study.cleanme.account.domain.Money;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

class SendMoneyServiceTest {
  private final LoadAccountPort loadAccountPort = Mockito.mock(LoadAccountPort.class);
  private final AccountLock accountLock = Mockito.mock(AccountLock.class);
  private final UpdateAccountStatePort updateAccountStatePort =
      Mockito.mock(UpdateAccountStatePort.class);
  private final SendMoneyService sendMoneyService =
      new SendMoneyService(
          loadAccountPort, accountLock, updateAccountStatePort, moneyTransferProperties());

  @Test
  void givenWithdrawalFails_thenOnlySourceAccountIdLockedAndReleased() {
    // Given (테스트 케이스가 동작하기 위해 갖춰져야 하는 선행 조건 준비)
    Account.AccountId sourceAccountId = new Account.AccountId(41L);
    Account sourceAccount = givenAnAccountWithId(sourceAccountId);

    Account.AccountId targetAccountId = new Account.AccountId(42L);
    Account targetAccount = givenAnAccountWithId(targetAccountId);

    givenWithdrawalWillFail(sourceAccount);
    givenDepositWillSucceed(targetAccount);

    SendMoneyCommand command =
        new SendMoneyCommand(sourceAccountId, targetAccountId, Money.of(300L));

    // When (테스트하고자 하는 대상 코드 실행)
    // 유스케이스 실행
    boolean success = sendMoneyService.sendMoney(command);

    // Then (대상 코드의 수행 결과 판단)
    // 트랜잭션이 성공적이었는지 확인
    assertThat(success).isFalse();

    // sourceAccount 에 대해 lock, release 메서드가 실행되었는지 확인
    then(accountLock).should().lockAccount(eq(sourceAccountId));
    then(accountLock).should().releaseAccount(eq(sourceAccountId));

    // 인출에 실패하면 targetAccount 는 한번도 잠기지 않음
    then(accountLock).should(times(0)).lockAccount(eq(targetAccountId));
  }

  @Test
  void transactionSucceeds() {
    // Given (테스트 케이스가 동작하기 위해 갖춰져야 하는 선행 조건 준비)
    Account sourceAccount = givenSourceAccount();
    Account targetAccount = givenTargetAccount();

    givenWithdrawalWillSucceed(sourceAccount);
    givenDepositWillSucceed(targetAccount);

    Money money = Money.of(500L);

    // 유스케이스의 입력으로 사용할 command
    SendMoneyCommand command =
        new SendMoneyCommand(sourceAccount.getId().get(), targetAccount.getId().get(), money);

    // When (테스트하고자 하는 대상 코드 실행)
    // 유스케이스 실행
    boolean success = sendMoneyService.sendMoney(command);

    // Then (대상 코드의 수행 결과 판단)
    // 트랜잭션이 성공적이었는지 확인
    assertThat(success).isTrue();

    Account.AccountId sourceAccountId = sourceAccount.getId().get();
    Account.AccountId targetAccountId = targetAccount.getId().get();

    // AccountLock 에 대해 특정 메서드가 호출되었는지 확인
    then(accountLock).should().lockAccount(eq(sourceAccountId));
    then(sourceAccount).should().withdraw(eq(money), eq(targetAccountId));
    then(accountLock).should().releaseAccount(sourceAccountId);

    then(accountLock).should().lockAccount(eq(targetAccountId));
    then(targetAccount).should().deposit(eq(money), eq(sourceAccountId));
    then(accountLock).should().releaseAccount(targetAccountId);

    thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
  }

  private void givenWithdrawalWillFail(Account account) {
    // mock 객체 생성
    given(account.withdraw(any(Money.class), any(Account.AccountId.class))).willReturn(false);
  }

  private void thenAccountsHaveBeenUpdated(Account.AccountId... accountIds) {
    ArgumentCaptor<Account> accountCaptor = ArgumentCaptor.forClass(Account.class);

    then(updateAccountStatePort)
        .should(times(accountIds.length))
        .updateActivities(accountCaptor.capture());

    List<Account.AccountId> updatedAccountIds =
        accountCaptor.getAllValues().stream()
            .map(Account::getId)
            .map(Optional::get)
            .collect(Collectors.toList());

    for (Account.AccountId accountId : accountIds) {
      assertThat(updatedAccountIds).contains(accountId);
    }
  }

  private Account givenSourceAccount() {
    return givenAnAccountWithId(new Account.AccountId(41L));
  }

  private Account givenAnAccountWithId(Account.AccountId id) {
    Account account = Mockito.mock(Account.class);

    // account.getId() 호출 시 id 리턴
    given(account.getId()).willReturn(Optional.of(id));

    // loadAccountPort.loadAccount() 호출 시 account 리턴
    given(loadAccountPort.loadAccount(eq(account.getId().get()), any(LocalDateTime.class)))
        .willReturn(account);

    return account;
  }

  private void givenWithdrawalWillSucceed(Account account) {
    // 어떤 금액과 아이디가 들어와도 true 리턴
    given(account.withdraw(any(Money.class), any(Account.AccountId.class))).willReturn(true);
  }

  private void givenDepositWillSucceed(Account account) {
    // 어떤 금액과 아이디가 들어와도 true 리턴
    given(account.deposit(any(Money.class), any(Account.AccountId.class))).willReturn(true);
  }

  private Account givenTargetAccount() {
    return givenAnAccountWithId(new Account.AccountId(42L));
  }

  private MoneyTransferProperties moneyTransferProperties() {
    return new MoneyTransferProperties(Money.of(Long.MAX_VALUE));
  }
}
