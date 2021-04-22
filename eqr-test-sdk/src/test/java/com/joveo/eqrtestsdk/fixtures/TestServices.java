package com.joveo.eqrtestsdk.fixtures;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.services.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class TestServices {
  @Inject public ClientService clientService;
  @Inject public CampaignService campaignService;
  @Inject public JobGroupService jobGroupService;
  @Inject public PublisherService publisherService;

  public Session session;

  public Config config;

  public static TestServices setup(String JOVEO_ENV) {
    Injector injector = Guice.createInjector(new TestModule());
    return injector
        .getInstance(TestServices.class)
        .setConfigAndSession(
            new TestSession(), ConfigFactory.load(JOVEO_ENV.toString().toLowerCase() + ".conf"));
  }

  public TestServices setConfigAndSession(Session session, Config config) {
    this.config = config;
    this.session = session;
    return this;
  }

  public ObjectMapper getObjectMapper() {
    return clientService.objectMapper;
  }
}
