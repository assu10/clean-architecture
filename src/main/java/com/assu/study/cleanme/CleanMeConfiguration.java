package com.assu.study.cleanme;

import com.assu.study.cleanme.account.application.service.MoneyTransferProperties;
import com.assu.study.cleanme.account.domain.Money;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CleanMeConfigurationProperties.class)
public class CleanMeConfiguration {
  @Bean
  public MoneyTransferProperties moneyTransferProperties(
      CleanMeConfigurationProperties properties) {
    return new MoneyTransferProperties(Money.of(properties.getTransferThreshold()));
  }
}
