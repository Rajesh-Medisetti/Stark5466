package com.joveo.eqrtestsdk.core.entities;

import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.automation.PauseDto;
import com.joveo.eqrtestsdk.models.automation.Status;
import java.util.ArrayList;
import java.util.List;

public class Automation {

  public final String id;
  private final String clientId;
  private final Driver driver;

  /** . Constructor for Automation */
  public Automation(String id, String clientId, Driver driver) {
    this.id = id;
    this.clientId = clientId;
    this.driver = driver;
  }

  /** . pause automation */
  public void pause()
      throws UnexpectedResponseException, ApiRequestException, InvalidInputException {

    PauseDto pauseDto = new PauseDto();

    List<String> ids = new ArrayList<>();

    ids.add(id);

    pauseDto.setIds(ids);
    pauseDto.setStatus(Status.P);

    driver.automationService.pause(id, driver.session, driver.conf, pauseDto);
  }
}
