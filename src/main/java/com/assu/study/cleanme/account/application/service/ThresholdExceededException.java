package com.assu.study.cleanme.account.application.service;

import com.assu.study.cleanme.account.domain.Money;

class ThresholdExceededException extends RuntimeException {
  public ThresholdExceededException(Money threshold, Money actual) {
    super(
        String.format(
            "Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!",
            actual, threshold));
  }
}
