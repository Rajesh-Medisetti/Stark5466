package com.joveo.eqrtestsdk.core.services;

import com.google.inject.Inject;
import com.joveo.eqrtestsdk.api.Session;
import com.joveo.eqrtestsdk.core.models.MajorMinorVersions;
import com.joveo.eqrtestsdk.core.mojo.JoveoHttpExecutor;
import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.MojoException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import java.time.LocalDateTime;
import org.redisson.Redisson;
import org.redisson.api.RMap;
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
   * @throws MojoException throws custom mojo exception On unexpected behaviour
   */
  public MajorMinorVersions getVersions() throws MojoException {

    RMap<String, String> allKeyValues = redisClient.getMap(this.mapName, StringCodec.INSTANCE);

    Integer minorVersion;
    Integer majorVersion;
    MajorMinorVersions versions = new MajorMinorVersions();

    try {
      minorVersion = Integer.valueOf(allKeyValues.get("\"minorVersion\"").replaceAll("\"", ""));
      majorVersion = Integer.valueOf(allKeyValues.get("\"majorVersion\"").replaceAll("\"", ""));
    } catch (NumberFormatException e) {
      logger.error("Number format exception while parsing version number" + e.getMessage());
      throw new MojoException("Number format exception while parsing version number", e);
    }

    versions.setMajorVersion(majorVersion);
    versions.setMinorVersion(minorVersion);

    return versions;
  }

  /**
   * This method is used to trigger gandalf major run.
   *
   * @param session Session
   * @throws ApiRequestException something wrong with request
   * @throws UnexpectedResponseException The API response was not as expected
   */
  public void runGandalf(Session session) throws ApiRequestException, UnexpectedResponseException {
    logger.info("Triggering Gandalf for major run at: " + LocalDateTime.now());
    RestResponse response =
        executor.post(session, "http://gandalf.staging.joveo.com/maintainance/runmajor", null);
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
    config = new Config();
    config.useSingleServer().setAddress(redisUrl);
    redisClient = Redisson.create(config);
    this.mapName = mapName;
  }
}
