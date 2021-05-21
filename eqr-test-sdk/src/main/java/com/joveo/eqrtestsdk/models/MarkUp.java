package com.joveo.eqrtestsdk.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.joveo.eqrtestsdk.models.validationgroups.MarkUpPublisher;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@JsonSerialize
@Valid
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class MarkUp {

  /** . MarkUp Constructor. */
  public MarkUp(Double markUp, String clientId, String entityId, String entity) {
    this.value = markUp;
    this.effectiveFrom = Instant.now().toEpochMilli() / 1000;
    this.entities = new ArrayList<>();
    this.entities.add(new Entity(clientId, entityId, entity));
  }

  @NotEmpty(message = "agencyIdCan't be empty")
  private String agencyId;

  private long effectiveFrom;

  @Min(value = 0, message = "markUp can't be negative")
  private Double value;

  @Valid
  @Size(min = 1, message = "at least one entity should be to update")
  private List<Entity> entities;

  public MarkUp(Double value, String clientId, String entityId, String pubId, String entity) {
    this(value, clientId, entityId, entity);
    this.entities.get(0).placementId = pubId;
  }

  public String getAgencyId() {
    return agencyId;
  }

  public long getEffectiveFrom() {
    return effectiveFrom;
  }

  public void setEffectiveFrom(long effectiveFrom) {
    this.effectiveFrom = effectiveFrom;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public List<Entity> getEntities() {
    return entities;
  }

  public void setEntities(List<Entity> entities) {
    this.entities = entities;
  }

  public void setAgencyId(String instanceIdentifier) {
    this.agencyId = instanceIdentifier;
  }

  public static class Entity {

    /** . Entity Constructor */
    public Entity(String clientId, String entityId, String entity) {
      this.clientId = clientId;
      this.entityId = entityId;
      level = entity;
    }

    public String clientId;
    public String entityId;

    public String getClientId() {
      return clientId;
    }

    public void setClientId(String clientId) {
      this.clientId = clientId;
    }

    public String getEntityId() {
      return entityId;
    }

    public void setEntityId(String entityId) {
      this.entityId = entityId;
    }

    public String getLevel() {
      return level;
    }

    public void setLevel(String level) {
      this.level = level;
    }

    public String getPlacementId() {
      return placementId;
    }

    public void setPlacementId(String placementId) {
      this.placementId = placementId;
    }

    public String level;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotEmpty(
        message = "publisherId can't be null/empty",
        groups = {MarkUpPublisher.class})
    public String placementId;
  }
}