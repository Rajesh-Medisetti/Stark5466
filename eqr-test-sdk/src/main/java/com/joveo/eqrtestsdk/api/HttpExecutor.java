package com.joveo.eqrtestsdk.api;

import com.joveo.eqrtestsdk.core.mojo.RestResponse;
import com.joveo.eqrtestsdk.exception.MojoException;
import java.util.Map;

public interface HttpExecutor {

  RestResponse post(
      Session session,
      String url,
      Object body,
      Map<String, String> headers,
      Map<String, String> urlParams)
      throws MojoException;

  RestResponse post(Session session, String url, Object body) throws MojoException;

  RestResponse get(
      Session session, String url, Map<String, String> headers, Map<String, String> urlParams)
      throws MojoException;

  RestResponse get(Session session, String url) throws MojoException;

  RestResponse put(
      Session session,
      String url,
      Object body,
      Map<String, String> headers,
      Map<String, String> urlParams)
      throws MojoException;

  RestResponse put(Session session, String url, Object body) throws MojoException;

  RestResponse delete(
      Session session, String url, Map<String, String> headers, Map<String, String> urlParams)
      throws MojoException;

  RestResponse delete(Session session, String url) throws MojoException;
}
