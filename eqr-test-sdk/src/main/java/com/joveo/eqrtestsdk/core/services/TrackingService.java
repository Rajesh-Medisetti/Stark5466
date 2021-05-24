package com.joveo.eqrtestsdk.core.services;

import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.models.MajorMinorVersions;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.RedisIoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.time.LocalDateTime;
import org.redisson.Redisson;
import org.redisson.api.RKeys;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrackingService {
  private static Logger logger = LoggerFactory.getLogger(TrackingService.class);

  private RedissonClient redisClient;
  private Config config;
  private JoveoHttpExecutor executor;
  private String mapName;

  @Inject
  public TrackingService(JoveoHttpExecutor executor) {
    this.executor = executor;
  }

  /**
   * This method provides major and minor version numbers of gandalf run.
   *
   * @return MajorMinorVersions object
   * @throws RedisIoException throws exception when versions contains other than numbers
   */
  public MajorMinorVersions getVersions() throws RedisIoException {

    RMap<String, String> allKeyValues = redisClient.getMap(this.mapName, StringCodec.INSTANCE);

    Integer minorVersion;
    Integer majorVersion;

    try {
      minorVersion = Integer.valueOf(allKeyValues.get("\"minorVersion\"").replaceAll("\"", ""));
      majorVersion = Integer.valueOf(allKeyValues.get("\"majorVersion\"").replaceAll("\"", ""));
    } catch (NumberFormatException e) {
      logger.error("Number format exception while parsing version number" + e.getMessage());
      throw new RedisIoException("Number format exception while parsing version number", e);
    }

    MajorMinorVersions versions = new MajorMinorVersions(majorVersion, minorVersion);

    return versions;
  }

  public void addElementToSet(String value) {
    RSet<String> spamIps = redisClient.getSet("spam_ips", StringCodec.INSTANCE);
    spamIps.add(value);
  }

  public void removeElementFromSet(String value) {
    RSet<String> spamIps = redisClient.getSet("spam_ips", StringCodec.INSTANCE);
    spamIps.remove(value);
  }

  public void removeKey(String keyName) {
    RKeys keys = redisClient.getKeys();
    keys.delete(keyName);
  }

  /**
   * This method is used to trigger gandalf major run.
   *
   * @param session Session
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public void runGandalf(Session session, String gandalfMajorRunUrl)
      throws ApiRequestException, UnexpectedResponseException {
    logger.info("Triggering Gandalf for major run at: " + LocalDateTime.now());
    RestResponse response = executor.post(session, gandalfMajorRunUrl, null);
    if (response.getResponseCode() != 200) {
      logger.error("Unable to run gandalf major run");
      throw new UnexpectedResponseException("Unable to run gandalf major run");
    }
    logger.info("Gandalf major run started...");
  }

  /**
   * Connection for Amazon Elastic Cache.
   *
   * @param redisUrl Elastic Cache url
   * @param mapName Name of hash map stored in Elastic Cache
   */
  public void setup(String redisUrl, String mapName) {
    Config config = new Config();
    config.useSingleServer().setAddress(redisUrl);
    redisClient = Redisson.create(config);
    this.mapName = mapName;
  }

  public void close() {
    redisClient.shutdown();
  }
}
