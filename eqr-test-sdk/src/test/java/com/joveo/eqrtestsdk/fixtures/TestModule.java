package com.joveo.eqrtestsdk.fixtures;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import java.util.Collections;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

public class TestModule extends AbstractModule {

  @Override
  protected void configure() {}

  @Provides
  static JoveoHttpExecutor providesJoveHTTPExecutor() {
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

  //    @Singleton
  //    @Provides
  //    static ClientService providesClientService(JoveoHTTPExecutor executor, SchedulerService
  // schedulerService, Validator validator){
  //        return new ClientService(executor,schedulerService,validator);
  //    }
  //
  //    @Singleton
  //    @Provides
  //    static CampaignService providesCampaignService(JoveoHTTPExecutor executor, Validator
  // validator){
  //        return new CampaignService(executor,validator);
  //    }
  //
  //    @Singleton
  //    @Provides
  //    static JobGroupService JobGroupService(JoveoHTTPExecutor executor, Validator validator){
  //        return new JobGroupService(executor,validator);
  //    }
  //
  //    @Singleton
  //    @Provides
  //    static PublisherService PublisherService(JoveoHTTPExecutor executor,ObjectMapper
  // objectMapper,Validator validator){
  //        return new PublisherService(executor,objectMapper,validator);
  //    }

  @Provides
  static HttpClient providesHttpClient() {
    return HttpClients.custom()
        .setDefaultHeaders(
            Collections.singletonList(new BasicHeader("Content-Type", "application/json")))
        .build();
  }
}
