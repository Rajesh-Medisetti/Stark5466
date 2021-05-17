package com.joveo.eqrtestsdk.models.clickmeterevents;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ClickEvent {

  private String event;
  private String timestamp;
  private String id;
  private Data data;
  private Request request;

  public ClickEvent() {
    data = new Data();
    request = new Request();
  }

  /** This method is used to set default values for click events. */
  public void setDefaultValues() {
    this.setEvent("click");
    this.setDomain(1501, "/domains/1501", "9nl.es");
    this.setGroup(11096543, "/groups/386502441");
    this.setUnique(true);
    this.setAgent(
        "human",
        "UNKNOWN",
        "Chrome+90",
        "Chrome",
        "Chrome+90",
        "Chrome",
        "en-GB%2Cen-US%3Bq%3D0.9%2Cen%3Bq%3D0.8");
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp.toInstant(ZoneOffset.UTC).toString();
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setUser(long id) {
    this.data.user.id = id;
  }

  /**
   * This method is used to set datapoint information.
   *
   * @param id Id
   * @param ref Ref
   * @param shortUrl Short Url
   * @param destinationUrl Destination Url
   */
  public void setDatapoint(long id, String ref, String shortUrl, String destinationUrl) {
    this.data.datapoint.id = id;
    this.data.datapoint.ref = ref;
    this.data.datapoint.shortUrl = shortUrl;
    this.data.datapoint.destinationUrl = destinationUrl;
  }

  /**
   * This method is used to assign domain information.
   *
   * @param id Id
   * @param ref Ref
   * @param name Name
   */
  public void setDomain(long id, String ref, String name) {
    this.data.domain.id = id;
    this.data.domain.ref = ref;
    this.data.domain.name = name;
  }

  public void setGroup(long id, String ref) {
    this.data.group.id = id;
    this.data.group.ref = ref;
  }

  public void setUnique(Boolean unique) {
    this.request.unique = unique;
  }

  /**
   * This method is used to set Agent information.
   *
   * @param type Type
   * @param raw Raw
   * @param browserName Browser Name
   * @param browserFamilyName Browser FamilyName
   * @param osName OS Name
   * @param osFamilyName OS FamilyName
   * @param language Language
   */
  public void setAgent(
      String type,
      String raw,
      String browserName,
      String browserFamilyName,
      String osName,
      String osFamilyName,
      String language) {
    this.request.agent.type = type;
    this.request.agent.raw = raw;
    setBrowser(browserName, browserFamilyName);
    setOs(osName, osFamilyName);
    this.request.agent.language = language;
  }

  public void setAgentType(String type) {
    this.request.agent.type = type;
  }

  private void setBrowser(String browserName, String browserFamilyName) {
    if (browserName == null && browserFamilyName == null) {
      return;
    }
    this.request.agent.browser = new Request.Agent.Browser();
    this.request.agent.browser.name = browserName;
    this.request.agent.browser.familyName = browserFamilyName;
  }

  private void setOs(String osName, String osFamilyName) {
    if (osName == null && osFamilyName == null) {
      return;
    }
    this.request.agent.os = new Request.Agent.OS();
    this.request.agent.os.name = osName;
    this.request.agent.os.familyName = osFamilyName;
  }

  /**
   * This method is used to set location information.
   *
   * @param ip IP
   * @param country Country
   * @param city City
   * @param latitude Latitude
   * @param longitude Longitude
   */
  public void setLocation(
      String ip, String country, String city, Double latitude, Double longitude) {
    this.request.location.ip = ip;
    this.request.location.country = country;
    this.request.location.city = city;
    this.request.location.latitude = latitude;
    this.request.location.longitude = longitude;
  }

  public void setParams(String params) {
    this.request.params = params;
  }

  public static class Data {
    public User user;
    public Datapoint datapoint;
    public Domain domain;
    public Group group;

    /** This method is used to set data information. */
    public Data() {
      user = new User();
      datapoint = new Datapoint();
      domain = new Domain();
      group = new Group();
    }

    public static class User {
      public long id;
    }

    public static class Datapoint {
      public long id;
      public String ref;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public String shortUrl;

      public String destinationUrl;
    }

    public static class Domain {
      public long id;
      public String ref;
      public String name;
    }

    public static class Group {
      public long id;
      public String ref;
    }
  }

  public static class Request {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean unique;

    public Agent agent;
    public Location location;
    public String params;

    public Request() {
      agent = new Agent();
      location = new Location();
    }

    public static class Agent {
      public String type;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public String raw;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public Browser browser;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public OS os;

      public String language;

      public Agent() {}

      public static class Browser {
        public String name;
        public String familyName;
      }

      public static class OS {
        public String name;
        public String familyName;
      }
    }

    public static class Location {
      public String ip;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public String country;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public String city;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public Double latitude;

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public Double longitude;
    }
  }
}
