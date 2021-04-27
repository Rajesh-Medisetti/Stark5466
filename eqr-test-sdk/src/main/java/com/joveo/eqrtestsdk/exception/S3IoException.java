package com.joveo.eqrtestsdk.exception;

public class S3IoException extends MojoException {

  public S3IoException(String message, Throwable cause) {
    super(message, cause);
  }

  public S3IoException(String message) {
    super(message);
  }
}
