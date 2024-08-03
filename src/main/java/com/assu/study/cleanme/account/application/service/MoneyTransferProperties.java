package com.assu.study.cleanme.account.application.service;

import com.assu.study.cleanme.account.domain.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferProperties {
  private Money maximumTransferThreshold = Money.of(1_000_000L);
}
