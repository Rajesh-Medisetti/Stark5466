package com.joveo.eqrtestsdk.core.entities;

import com.joveo.eqrtestsdk.exception.ApiRequestException;
import com.joveo.eqrtestsdk.exception.InvalidInputException;
import com.joveo.eqrtestsdk.exception.UnexpectedResponseException;
import com.joveo.eqrtestsdk.models.PublisherDto;

public class Publisher {

  public final String id;
  private Driver driver;

  public Publisher(Driver driver, String id) {
    this.driver = driver;
    this.id = id;
  }

  /**
   * Edit min bid of the publisher.
   *
   * @param minBid minimum bid for the publisher
   * @throws UnexpectedResponseException The API response was not as expected
   * @throws InvalidInputException invalid input provided
   * @throws ApiRequestException something wrong with request
   */
  public void editMinBid(double minBid)
      throws UnexpectedResponseException, InvalidInputException, ApiRequestException {

    PublisherDto publisher = new PublisherDto();

    publisher.setId(this.id);
    publisher.setName(this.id);
    publisher.setValue(this.id);
    publisher.setMinBid(minBid);

    driver.publisherService.edit(driver.session, driver.conf, publisher);
  }
}
