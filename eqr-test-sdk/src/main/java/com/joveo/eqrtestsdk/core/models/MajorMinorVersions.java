package com.joveo.eqrtestsdk.core.models;

public class MajorMinorVersions {
  private Integer majorVersion;
  private Integer minorVersion;

  public MajorMinorVersions(Integer majorVersion, Integer minorVersion) {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
  }

  public Integer getMajorVersion() {
    return majorVersion;
  }

  public void setMajorVersion(Integer majorVersion) {
    this.majorVersion = majorVersion;
  }

  public Integer getMinorVersion() {
    return minorVersion;
  }

  public void setMinorVersion(Integer minorVersion) {
    this.minorVersion = minorVersion;
  }
}
