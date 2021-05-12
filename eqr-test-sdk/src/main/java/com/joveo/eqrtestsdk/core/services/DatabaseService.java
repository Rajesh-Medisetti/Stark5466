package com.joveo.eqrtestsdk.core.services;

import static com.mongodb.client.model.Filters.eq;

import com.joveo.eqrtestsdk.core.models.ConversionCodes;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseService {
  private static Logger logger = LoggerFactory.getLogger(DatabaseService.class);

  private MongoClient mongoClient;
  private MongoDatabase database;
  private MongoCollection<Document> collection;

  public DatabaseService() {}

  /**
   * This method provides apply conversion id and apply conversion value for a given client id.
   *
   * @param clientId Client Id
   * @return Apply Conversion codes
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public ConversionCodes getApplyConversionCodes(String clientId) throws MojoException {
    Document document = collection.find(eq("clientId", clientId)).first();
    if (document == null) {
      logger.error("There is no apply conversion attributes for client : " + clientId);
      throw new MojoException("There is no apply conversion attributes for client : " + clientId);
    }
    ConversionCodes conversionCodes = new ConversionCodes();
    conversionCodes.setConversionId(document.getLong("conversionId"));
    conversionCodes.setConversionCode(document.getString("conversionCode"));
    return conversionCodes;
  }

  /**
   * Connection to MongoDB.
   *
   * @param mongoHostName Mongo HostName
   * @param mongoDbName Mongo DbName
   * @param mongoCollectionName Mongo CollectionName
   * @param username Username
   * @param password Password
   */
  public void setup(
      String mongoHostName,
      String mongoDbName,
      String mongoCollectionName,
      String username,
      String password) {
    mongoClient =
        MongoClients.create("mongodb://" + username + ":" + password + "@" + mongoHostName);
    database = mongoClient.getDatabase(mongoDbName);
    collection = database.getCollection(mongoCollectionName);
  }
}
