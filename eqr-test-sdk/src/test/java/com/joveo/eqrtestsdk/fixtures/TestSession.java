package com.joveo.eqrtestsdk.fixtures;

import com.joveo.eqrtestsdk.api.Session;

public class TestSession implements Session {
  @Override
  public String getAuthToken() {
    return "jwt-token-test";
  }

  @Override
  public String getAuthKey() {
    return "MojoAccessToken";
  }

  @Override
  public String getInstanceIdentifierKey() {
    return "MojoAgencyId";
  }

  @Override
  public String getInstanceIdentifier() {
    return "sdk-test-agency";
  }

  @Override
  public String getUsername() {
    return "sdk-test@joveo.com";
  }
}
