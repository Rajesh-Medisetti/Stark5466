package com.joveo.eqrtestsdk.exception;

public class ApiRequestException extends MojoException {
  public ApiRequestException(String message) {
    super(message);
  }

  public ApiRequestException(Throwable cause) {
    super(cause);
  }

  public ApiRequestException(String message, Throwable cause) {
    super(message, cause);
  }
}
