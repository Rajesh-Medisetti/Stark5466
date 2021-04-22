package com.joveo.eqrtestsdk.core.entities;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.services.CampaignService;
import com.joveo.eqrtestsdk.core.services.ClientService;
import com.joveo.eqrtestsdk.core.services.JobGroupService;
import com.joveo.eqrtestsdk.core.services.PublisherService;
import com.joveo.eqrtestsdk.core.services.SchedulerService;
import java.util.Collections;
import javax.inject.Singleton;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

public class DriverModule extends AbstractModule {

  @Override
  protected void configure() {}

  @Provides
  static JoveoHttpExecutor providesJoveHttpExecutor() {
    return new JoveoHttpExecutor(providesHttpClient(), providesObjectMapper());
  }

  @Provides
  static ObjectMapper providesObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    ;
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return objectMapper;
  }

  @Provides
  static Validator providesValidator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }

  @Singleton
  @Provides
  static ClientService providesClientService(
      JoveoHttpExecutor executor, SchedulerService schedulerService, Validator validator) {
    return new ClientService(executor, schedulerService, validator, providesObjectMapper());
  }

  @Singleton
  @Provides
  static CampaignService providesCampaignService(JoveoHttpExecutor executor, Validator validator) {
    return new CampaignService(executor, validator);
  }

  @Singleton
  @Provides
  static JobGroupService providesJobGroupService(JoveoHttpExecutor executor, Validator validator) {
    return new JobGroupService(executor, validator);
  }

  @Singleton
  @Provides
  static PublisherService providesPublisherService(
      JoveoHttpExecutor executor, ObjectMapper objectMapper, Validator validator) {
    return new PublisherService(executor, objectMapper, validator);
  }

  @Provides
  static HttpClient providesHttpClient() {
    return HttpClients.custom()
        .setDefaultHeaders(
            Collections.singletonList(new BasicHeader("Content-Type", "application/json")))
        .build();
  }
}
