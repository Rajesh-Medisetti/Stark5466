package com.joveo.javaapplication.config;

import com.timgroup.statsd.NonBlockingStatsDClientBuilder;
import com.timgroup.statsd.StatsDClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatadogConfig {
  @Value("${datadog.statsd.hostname}")
  private String datadogStatsdHostname;

  @Value("${environment}")
  private String environment;

  /**
   * this method returns StatsDClient.
   *
   * @return
   */
  @Bean
  public StatsDClient statsDClient() {
    return new NonBlockingStatsDClientBuilder()
        .prefix("joveo.java-application")
        .hostname(datadogStatsdHostname)
        .port(8125)
        .constantTags(String.format("env:%s", environment))
        .build();
  }
}
