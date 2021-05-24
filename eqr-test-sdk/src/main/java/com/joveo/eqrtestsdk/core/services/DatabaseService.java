package com.joveo.eqrtestsdk.core.services;

import static com.mongodb.client.model.Filters.eq;

import com.joveo.eqrtestsdk.core.models.ConversionCodes;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
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
  private MongoCollection<Document> conversionCodesCollection;
  private MongoCollection<Document> clientsCollection;

  public DatabaseService() {}

  /**
   * This method provides apply conversion id and apply conversion value for a given client id.
   *
   * @param clientId Client Id
   * @return Apply Conversion codes
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public ConversionCodes getApplyConversionCodes(String clientId)
      throws UnexpectedResponseException {
    Document document = conversionCodesCollection.find(eq("clientId", clientId)).first();
    if (document == null) {
      logger.error("There is no apply conversion attributes for client : " + clientId);
      throw new UnexpectedResponseException(
          "There is no apply conversion attributes for client : " + clientId);
    }
    ConversionCodes conversionCodes = new ConversionCodes();
    conversionCodes.setApplyStartConversionId(document.getLong("applyStartConversionId"));
    conversionCodes.setApplyStartConversionCode(document.getString("applyStartConversionCode"));
    conversionCodes.setApplyFinishConversionId(document.getLong("conversionId"));
    conversionCodes.setApplyFinishConversionCode(document.getString("conversionCode"));
    return conversionCodes;
  }

  /**
   * This method is used to fetch pixel hash id of a given client.
   *
   * @param clientId Client Id
   * @return Client pixel hash id
   * @throws UnexpectedResponseException On unexpected Response
   */
  public String fetchPixelHashId(String clientId) throws UnexpectedResponseException {
    Document document = clientsCollection.find(eq("_id", clientId)).first();
    if (document == null) {
      logger.error("There is client document for : " + clientId);
      throw new UnexpectedResponseException("There is client document for : " + clientId);
    }
    logger.info("clientId : " + clientId + " pixelCode: " + document.getString("pixelHashId"));
    return document.getString("pixelHashId");
  }

  /**
   * Connection to MongoDB.
   *
   * @param mongoHostName Mongo HostName
   * @param mongoDbName Mongo DbName
   * @param mongoConversionCollectionName Mongo Conversion Collection Name
   * @param mongoClientCollectionName Mongo Client Collection Name
   * @param username Username
   * @param password Password
   */
  public void setup(
      String mongoHostName,
      String mongoDbName,
      String mongoConversionCollectionName,
      String mongoClientCollectionName,
      String username,
      String password) {
    mongoClient =
        MongoClients.create("mongodb://" + username + ":" + password + "@" + mongoHostName);
    database = mongoClient.getDatabase(mongoDbName);
    conversionCodesCollection = database.getCollection(mongoConversionCollectionName);
    clientsCollection = database.getCollection(mongoClientCollectionName);
  }

  // This method used to close mongoDB connection.
  public void close() {
    mongoClient.close();
  }
}
