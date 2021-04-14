package com.joveo.javaapplication.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
  @Value("${spring.data.mongodb.uri}")
  private String mongodbUri;

  /*
   * Use the standard Mongo driver API to create a com.mongodb.client.MongoClient instance.
   */
  public @Bean MongoClient mongoClient() {
    return MongoClients.create(mongodbUri);
  }

  public @Bean MongoTemplate mongoTemplate() {
    return new MongoTemplate(mongoClient(), "mojo");
  }
}
