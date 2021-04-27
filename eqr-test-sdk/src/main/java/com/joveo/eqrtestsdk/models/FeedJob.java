package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FeedJob {
  private String title;
  private String city;
  private String state;
  private String country;
  private String description;
  private int referenceNumber;
  private String url;

  @JsonIgnore public Map<String, String> additionalFeedNode = new HashMap<>();

  @JsonSerialize(using = LocalDateSerializer.class)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonFormat(pattern = "dd-MM-yyyy")
  private LocalDate date;

  private String category;
  private String careerLevel;
  private String department;
  private int cpc;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * set tile.
   *
   * @param title title
   * @param cdata enable cdata for field
   */
  public void setTitle(String title, boolean cdata) {
    this.title = title;
    if (cdata) {
      this.title = getCdataField(title);
    }
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  /**
   * set city.
   *
   * @param city city
   * @param cdata enable cdata for field
   */
  public void setCity(String city, boolean cdata) {
    this.city = city;
    if (cdata) {
      this.city = getCdataField(city);
    }
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  /**
   * set state.
   *
   * @param state state
   * @param cdata enable cdata for field
   */
  public void setState(String state, boolean cdata) {
    this.state = state;
    if (cdata) {
      this.state = getCdataField(state);
    }
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * set country.
   *
   * @param country country
   * @param cdata enable cdata for field
   */
  public void setCountry(String country, boolean cdata) {
    this.country = country;
    if (cdata) {
      this.country = getCdataField(country);
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * set description.
   *
   * @param description description
   * @param cdata enable cdata for field
   */
  public void setDescription(String description, boolean cdata) {
    this.description = description;
    if (cdata) {
      this.description = getCdataField(description);
    }
  }

  public int getReferenceNumber() {
    return referenceNumber;
  }

  public void setReferenceNumber(int referenceNumber) {
    this.referenceNumber = referenceNumber;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * set url.
   *
   * @param url url
   * @param cdata enable cdata for field
   */
  public void setUrl(String url, boolean cdata) {
    this.url = url;
    if (cdata) {
      this.url = getCdataField(url);
    }
  }

  public LocalDate getDate() {
    return date;
  }

  public void setDate(LocalDate date) {
    this.date = date;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * set category.
   *
   * @param category category
   * @param cdata enable cdata for field
   */
  public void setCategory(String category, boolean cdata) {
    this.category = category;
    if (cdata) {
      this.category = getCdataField(category);
    }
  }

  public String getCareerLevel() {
    return careerLevel;
  }

  public void setCareerLevel(String careerLevel) {
    this.careerLevel = careerLevel;
  }

  /**
   * set career level.
   *
   * @param careerLevel career level
   * @param cdata enable cdata for field
   */
  public void setCareerLevel(String careerLevel, boolean cdata) {
    this.careerLevel = careerLevel;
    if (cdata) {
      this.careerLevel = getCdataField(careerLevel);
    }
  }

  public String getDepartment() {
    return department;
  }

  public void setDepartment(String department) {
    this.department = department;
  }

  /**
   * set department.
   *
   * @param department department
   * @param cdata enable cdata for field
   */
  public void setDepartment(String department, boolean cdata) {
    this.department = department;
    if (cdata) {
      this.department = getCdataField(department);
    }
  }

  public int getCpc() {
    return cpc;
  }

  public void setCpc(int cpc) {
    this.cpc = cpc;
  }

  @JsonAnyGetter
  public Map<String, String> getAdditionalFeedNode() {
    return additionalFeedNode;
  }

  public void setAdditionalFeedNode(Map<String, String> extraParams) {
    this.additionalFeedNode = extraParams;
  }

  /**
   * adding custom feed node.
   *
   * @param key name of additional feed
   * @param value value of additional feed
   * @param cdata enable cdata for field
   */
  public void addAdditionalFeedNode(String key, String value, boolean cdata) {
    this.additionalFeedNode.put(key, value);
    if (cdata) {
      this.additionalFeedNode.put(key, getCdataField(value));
    }
  }

  public void addAdditionalFeedNode(String key, String value) {
    this.additionalFeedNode.put(key, value);
  }

  private String getCdataField(String data) {
    return "<![CDATA[" + data + "]]>";
  }
}
