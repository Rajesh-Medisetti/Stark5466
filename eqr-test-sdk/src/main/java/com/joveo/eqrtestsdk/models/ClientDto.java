package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.validationgroups.EditClient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@JsonSerialize
@Valid
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class ClientDto {

  @Valid
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private ClientParams params;

  @JsonIgnore
  @NotEmpty(
      message = "clientId can't be null/empty",
      groups = {EditClient.class})
  @Null(message = "clientId must be null")
  private String clientId;

  public void setClientId(String id) {
    this.clientId = id;
  }

  @JsonIgnore
  public String getClientId() {
    return clientId;
  }

  public ClientDto() {
    this.params = new ClientParams();
  }

  public void setName(String name) {
    this.params.name = name;
    this.params.exportedName = name;
  }

  public void setCountry(String country) {
    this.params.country = country;
  }

  public void setBudget(Double budget) {
    params.budgetCap = new CapDto(budget);
  }

  public void setExportedName(String exportedName) {
    this.params.exportedName = exportedName;
  }

  public void setAdvertiserName(String advertiserName) {
    this.params.advertiserName = advertiserName;
  }

  public void setAts(String ats) {
    this.params.ats = ats;
  }

  public void setAtsUrl(String atsUrl) {
    this.params.atsUrl = atsUrl;
  }

  public void setFrequency(Frequency frequency) {
    this.params.frequency = (frequency == null) ? null : frequency.getValue();
  }

  public void setApplyConvWindow(int applyConvWindow) {
    this.params.applyConvWindow = applyConvWindow;
  }

  public void setTimezone(TimeZone timezone) {
    this.params.timezone = (timezone == null) ? null : timezone.getValue();
  }

  public void setEndDate(LocalDate endDate) {
    params.endDate = endDate;
  }

  public void setStartDate(LocalDate startDate) {
    params.startDate = startDate;
  }

  /** Setting Industry. */
  public void setIndustry(String industry) {

    this.params.industry = industry;
    if (this.params.industries == null) {
      this.params.industries = new ArrayList<>();
    }

    this.params.industries.add(industry);
  }

  /** Deleting The Feeds. */
  public void deleteFeed(String xmlFeedUrl) {
    if (params.feeds == null) {
      params.feeds = new ArrayList<>();
    }
    params.feeds.add(new ClientParams.Feeds(xmlFeedUrl, true));
  }

  /** Adding The Feeds. */
  public void addFeed(String xmlUrlFeed) {
    this.params.addFeed(xmlUrlFeed);
  }

  public void addFeed(String xmlUrlFeed, Set<MandatoryFields> mandatoryFields) {
    this.params.addFeed(xmlUrlFeed, mandatoryFields);
  }

  public void setClientIds(List<String> clientIds) {
    this.params.clientIds = clientIds;
  }

  public void setStatus(List<String> status) {
    this.params.status = status;
  }

  /** Sets the Default Value before Client Create. */
  @SuppressWarnings("checkstyle:CyclomaticComplexity")
  public void setDefaultValues() {

    if (this.params.country == null) {
      this.params.country = "US";
    }

    if (this.params.advertiserName == null) {
      this.params.advertiserName = "";
    }

    if (this.params.atsUrl == null) {
      this.params.atsUrl = "";
    }

    this.params.type = "DirectEmployer";

    if (this.params.industries == null) {
      this.params.industries = new ArrayList<String>();
    }

    this.params.excludedPublishers = "";

    if (this.params.startDate == null) {
      this.params.startDate = LocalDate.now();
    }

    if (this.params.endDate == null) {
      this.params.endDate = LocalDate.now().plusWeeks(1);
    }

    this.params.markdown = "";

    if (this.params.feeds == null) {
      this.params.feeds = new ArrayList<ClientParams.Feeds>(0);
    }
    this.params.sjCreate = false;
    this.params.globallyExcludedPublishers = "";
  }

  @JsonIgnore
  public LocalDate getEndDate() {
    return this.params.endDate;
  }

  @JsonIgnore
  public List<ClientParams.Feeds> getFeeds() {
    return params.feeds;
  }

  public static class ClientParams {

    @NotEmpty(message = "Client name can't be null/empty")
    @Null(
        message = "Client Name is not Editable",
        groups = com.joveo.eqrtestsdk.models.validationgroups.EditClient.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String name;

    @NotEmpty(
        message = "country can't be null/empty,it should be in prefix form like for AUSTRALIA = AU")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String country;

    @NotEmpty(message = "exportedName can't be null/empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String exportedName;

    @NotNull(message = "advertiserName can't be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String advertiserName;

    @NotEmpty(message = "ats can't be null/empty")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Null(message = "ats is not Editable", groups = EditClient.class)
    public String ats;

    @NotNull(message = "atsUrl can't be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String atsUrl;

    @NotNull(message = "frequency can't be null")
    @Null(message = "Frequency is not Editable", groups = EditClient.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String frequency;

    @NotNull(message = "applyConvWindow is in days,it can't be null ")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Min(
        value = 2,
        message = "applyConvWindow is Invalid, minimum is 2 days",
        groups = {EditClient.class, Default.class})
    @Max(
        value = 30,
        message = "applyConvWindow is Invalid, maximum is 30 days",
        groups = {EditClient.class, Default.class})
    public Integer applyConvWindow;

    @NotNull(message = "timezone can't be null,it should be in coded form")
    @Null(message = "TimeZone is not Editable", groups = EditClient.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String timezone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String type;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String excludedPublishers;

    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "endDate can't be null")
    @FutureOrPresent(
        message = "endDate can't be past",
        groups = {EditClient.class, Default.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDate endDate;

    @JsonFormat(pattern = "MM/dd/yyyy", shape = JsonFormat.Shape.STRING)
    @NotNull(message = "startDate can't be null")
    @FutureOrPresent(message = "startDate can't be past")
    @Null(message = "startDate is not Editable", groups = EditClient.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public LocalDate startDate;

    /** startDate must be <= endDate. */
    @AssertTrue(message = "startDate must be <= endDate")
    @JsonIgnore
    public boolean isValidRange() {
      if (endDate == null || startDate == null) {
        return true;
      }
      return endDate.compareTo(startDate) >= 0;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String markdown;

    @Size(min = 1, message = "number of feed must be at least one")
    @Valid
    @NotNull(message = "feeds can't be null")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Feeds> feeds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Boolean sjCreate;

    @Null(
        message = "industry is not editable in client",
        groups = {EditClient.class})
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String industry;

    @NotNull(message = "budgetCap can't be null")
    @Valid
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public CapDto budgetCap;

    @Size(max = 1, message = "Maximum number of industry can be 1")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<String> industries;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String globallyExcludedPublishers;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<String> clientIds;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<String> status;

    /** Adding the Feed with URL. */
    public void addFeed(String url) {

      if (this.feeds == null) {
        this.feeds = new ArrayList<>();
      }

      this.feeds.add(new Feeds(url));
    }

    /** Adding the Feed with URL and MandatoryFields. */
    public void addFeed(String url, Set<MandatoryFields> mandatoryFields) {

      if (this.feeds == null) {
        this.feeds = new ArrayList<>();
      }

      this.feeds.add(new Feeds(url, mandatoryFields));
    }

    @Valid
    public static class Feeds {

      @NotNull(message = "xmlFeedUrl can't be null")
      public String xmlFeedUrl;

      public String id;

      @JsonInclude(JsonInclude.Include.NON_DEFAULT)
      public boolean deleted;

      @Valid
      @JsonInclude(JsonInclude.Include.NON_NULL)
      public Set<MandatoryFields> mandatoryFields;

      @Valid
      @JsonInclude(JsonInclude.Include.NON_NULL)
      public SchemaMappings schemaMappings;

      public void setMandatoryFields(Set<MandatoryFields> mandatoryFields) {
        this.mandatoryFields = mandatoryFields;
      }

      @JsonInclude(JsonInclude.Include.NON_NULL)
      public Set<MandatoryFields> getMandatoryFields() {
        return this.mandatoryFields;
      }

      public Feeds(String url) {
        this(url, getDefaultMandatoryFields());
      }

      /** Feed constructor with url, mandatoryFields. */
      public Feeds(String url, Set<MandatoryFields> mandatoryField) {
        this.xmlFeedUrl = url;
        this.id = null;
        this.mandatoryFields = new HashSet<>();
        this.mandatoryFields.addAll(getDefaultMandatoryFields());
        this.mandatoryFields.addAll(mandatoryField);
        this.schemaMappings = new SchemaMappings();
      }

      /** Feed constructor with url to delete. */
      public Feeds(String url, boolean deleted) {
        this.xmlFeedUrl = url;
        this.deleted = deleted;
      }

      /** Ignore the Return Value in Json. */
      @JsonIgnore
      public static Set<MandatoryFields> getDefaultMandatoryFields() {
        Set<MandatoryFields> set = new HashSet<>();
        set.add(MandatoryFields.source);
        set.add(MandatoryFields.job);
        set.add(MandatoryFields.title);
        set.add(MandatoryFields.description);
        set.add(MandatoryFields.url);
        set.add(MandatoryFields.referencenumber);
        return set;
      }

      /** checking Mandatory Fields in Feed. */
      @SuppressWarnings("checkstyle:CyclomaticComplexity")
      @AssertTrue(
          message = "some mandatory Fields are not available in XmlFeed",
          groups = {Default.class, EditClient.class})
      @JsonIgnore
      public boolean isValidMandatoryFields() {

        if (this.deleted) {
          return true;
        } else if (this.mandatoryFields.contains(MandatoryFields.source)
            && this.schemaMappings.schemaMappingsJobCollection == null) {
          return false;
        } else if (this.mandatoryFields.contains(MandatoryFields.job)
            && this.schemaMappings.schemaMappingsJob == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.title)
            && this.schemaMappings.schemaMappingsTitle == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.description)
            && this.schemaMappings.schemaMappingsDescription == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.url)
            && this.schemaMappings.schemaMappingsUrl == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.referencenumber)
            && this.schemaMappings.schemaMappingsRefNumber == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.company)
            && this.schemaMappings.schemaMappingsCompany == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.city)
            && this.schemaMappings.schemaMappingsCity == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.category)
            && this.schemaMappings.schemaMappingsCategory == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.pubDate)
            && this.schemaMappings.schemaMappingsPublishedDate == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.lastmodifieddate)
            && this.schemaMappings.schemaMappingsModifiedDate == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.zip)
            && this.schemaMappings.schemaMappingsZip == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.date)
            && this.schemaMappings.schemaMappingsDatePosted == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.country)
            && this.schemaMappings.schemaMappingsCountry == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.state)
            && this.schemaMappings.schemaMappingsState == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.cpc)
            && this.schemaMappings.schemaMappingsCpcBid == null) {
          return false;
        }
        if (this.mandatoryFields.contains(MandatoryFields.type)
            && this.schemaMappings.schemaMappingsType == null) {
          return false;
        }
        return true;
      }

      public static class SchemaMappings {

        public String schemaMappingsJobCollection;
        public String schemaMappingsJob;
        public String schemaMappingsCompany;
        public String schemaMappingsTitle;
        public String schemaMappingsCity;
        public String schemaMappingsState;
        public String schemaMappingsCountry;
        public String schemaMappingsDescription;

        @JsonProperty("schemaMappingsURL")
        public String schemaMappingsUrl;

        public String schemaMappingsZip;
        public String schemaMappingsCategory;
        public String schemaMappingsDatePosted;
        public String schemaMappingsRefNumber;
        public String schemaMappingsModifiedDate;
        public String schemaMappingsPublishedDate;

        @JsonProperty("schemaMappingsCPCBid")
        public String schemaMappingsCpcBid;

        public String schemaMappingsType;

        @Valid
        @NotNull(message = "schemaMappingPublisher can't be null")
        public SchemaMappingPublisher schemaMappingPublisher;

        public Map<String, String> schemaMappingAdditional;

        public SchemaMappings() {
          this.schemaMappingAdditional = new HashMap<>();
          this.schemaMappingPublisher = new SchemaMappingPublisher();
        }

        public static class SchemaMappingPublisher {}
      }
    }
  }
}
