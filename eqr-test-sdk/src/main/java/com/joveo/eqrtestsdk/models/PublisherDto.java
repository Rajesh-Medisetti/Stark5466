package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class PublisherDto {

  public PublisherDto() {
    this.placement = new Placement();
  }

  public void setName(String name) {
    this.placement.name = name;
    this.placement.value = name;
  }

  public void setMinBid(Double number) {
    this.placement.minBid = number;
  }

  public void setCountry(String country) {
    this.placement.country = country;
  }

  public void setPublisherUrl(String url) {
    this.placement.url = url;
  }

  public void setBidType(String bidType) {
    this.placement.bidType = bidType;
  }

  public void setIndustry(String industry) {
    this.placement.industry = industry;
  }

  public void setCreatedBy(String userName) {
    this.placement.clickDefinitions.agencies.agencyId.definition.createdBy = userName;
  }

  public void setAgencyIdForClickDefinitions(String agencyId) {
    this.placement.clickDefinitions.agencies.agencyId.definition.agencyId = agencyId;
    this.agencyId = agencyId;
  }

  public void setAgency(String key, Placement.ClickDefinitions.Agencies.AgencyId agencyIdClass) {
    this.placement.clickDefinitions.agencies.setAgency(key, agencyIdClass);
  }

  @JsonIgnore
  public Placement.ClickDefinitions.Agencies.AgencyId getAgencyIdClass() {
    return this.placement.clickDefinitions.agencies.agencyId;
  }

  /** adding contact details of Publishers. */
  public void addPublisherContactDetails(String email) {
    if (this.placement.publisherContactDetailsRevamp == null) {
      this.placement.publisherContactDetailsRevamp = new ArrayList<>();
    }
    this.placement.publisherContactDetailsRevamp.add(
        new Placement.PublisherContactDetailsRevamp(email));
  }

  public void setValue(String value) {
    this.placement.value = value;
  }

  public void setId(String id) {
    this.placement.id = id;
  }

  /** setting the default values. */
  public void setDefaultValue() {

    this.placement.deliverFeedByFtp = false;
    this.placement.perClientPlacements = false;
    this.placement.ftpConfig = new Placement.FtpConfig();
    this.placement.feedFileType = "newXml";
    this.placement.currency = "USD";

    if (this.placement.country == null) {
      this.placement.country = "United States";
    }

    this.placement.clickDefinitions = new Placement.ClickDefinitions();
    this.placement.feedIndexLatency = null;
  }

  @Valid private Placement placement;

  @NotNull(message = "agencyId can't be null")
  private String agencyId;

  @JsonIgnore
  public String getValue() {
    return this.placement.value;
  }

  public static class Placement {

    @NotEmpty(message = "name can't be null/empty")
    public String name;

    @NotEmpty(message = "bidType can't be null/empty")
    public String bidType;

    @NotEmpty(message = "publisherUrl can't be null/empty ")
    public String url;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String industry;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<PublisherContactDetailsRevamp> publisherContactDetailsRevamp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("deliverFeedByFTP")
    public Boolean deliverFeedByFtp;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean perClientPlacements;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String id;

    public String value;

    @Min(value = 0, message = "minBid must be a positive number")
    public Double minBid;

    public Integer feedIndexLatency;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public FtpConfig ftpConfig;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String feedFileType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String currency;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String country;

    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public ClickDefinitions clickDefinitions;

    /** Validating Publisher Name. */
    @AssertTrue(message = "publisher Name can't contain spaces")
    @JsonIgnore
    public boolean isValidName() {
      return name == null || !name.contains(" ");
    }

    public static class PublisherContactDetailsRevamp {
      public String email;

      public PublisherContactDetailsRevamp(String email) {
        this.email = email;
      }
    }

    public static class FtpConfig {}

    public static class ClickDefinitions {
      @Valid public Agencies agencies;

      public ClickDefinitions() {
        agencies = new Agencies();
      }

      public static class Agencies {
        public static class AgencyId {

          public Clients clients;
          @Valid public Definition definition;
          public boolean enable;

          /** constructor for AgencyId. */
          public AgencyId() {
            clients = new Clients();
            definition = new Definition();
            enable = false;
          }
        }

        @JsonIgnore public AgencyId agencyId;
        @JsonIgnore public Map<String, AgencyId> agency;

        public Agencies() {
          this.agency = new HashMap<>();
          this.agencyId = new AgencyId();
        }

        @JsonAnyGetter
        public Map<String, AgencyId> getAgency() {
          return agency;
        }

        @JsonAnySetter
        public void setAgency(String key, AgencyId agencyId) {
          agency.put(key, agencyId);
        }

        public static class Clients {}

        public static class Definition {
          public String createdApp;

          @NotNull(message = "username can't be null")
          public String createdBy;

          public String status;
          public boolean enable;

          @NotNull(message = "agencyId can't be null")
          public String agencyId;

          public NewBot newBot;
          public boolean bot;

          /** adding click-definition. */
          public Definition() {
            newBot = new NewBot();
            createdApp = "mojo";
            createdBy = null;
            status = "Approved";
            enable = true;
            agencyId = null;
            bot = true;
          }

          public static class NewBot {
            public boolean joveoBotlogic;
            public boolean cmBotLogic;

            public NewBot() {
              joveoBotlogic = true;
              cmBotLogic = true;
            }
          }
        }
      }
    }
  }
}
