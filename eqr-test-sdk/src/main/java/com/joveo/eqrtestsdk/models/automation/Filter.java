package com.joveo.eqrtestsdk.models.automation;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import javax.validation.constraints.NotEmpty;

@JsonSerialize
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Filter {

  @NotEmpty(message = "filterId  can't be null/empty in Automation")
  String id;

  @NotEmpty(message = "filterName can't be null/empty")
  String name;

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public List<String> getPlacementIds() {
    return placementIds;
  }

  public Boolean getAllPlacements() {
    return allPlacements;
  }

  List<String> placementIds;
  Boolean allPlacements;

  /** . Filter constructor for Automation */
  public Filter(String id, String name, List<String> placementIds, Boolean allPlacements) {
    this.id = id;
    this.name = name;
    this.placementIds = placementIds;
    this.allPlacements = allPlacements;
  }
}
