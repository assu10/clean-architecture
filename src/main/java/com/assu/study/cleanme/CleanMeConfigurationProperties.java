package com.assu.study.cleanme;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cleanme")
public class CleanMeConfigurationProperties {
  private long transferThreshold = Long.MAX_VALUE;
}
