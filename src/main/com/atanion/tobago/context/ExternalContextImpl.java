/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 06.06.2003 08:30:05.
 * $Id$
 */
package com.atanion.tobago.context;

import com.atanion.tobago.webapp.TobagoMultipartFormdataRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ExternalContextImpl extends ExternalContext {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(ExternalContextImpl.class);

// ///////////////////////////////////////////// attribute

  private ServletContext servletContext;

  private ServletRequest request;

  private ServletResponse response;

  private Map requestMap;

  private Map requestParameterMap;

// ///////////////////////////////////////////// constructor

  public ExternalContextImpl(
      ServletContext servletContext, ServletRequest request,
      ServletResponse response) {
    this.servletContext = servletContext;
    this.request = request;
    this.response = response;
  }

// ///////////////////////////////////////////// code

  public Object getSession(boolean create) {
    return ((HttpServletRequest) request).getSession(create);
  }

  public Iterator getRequestParameterNames() {
    final Enumeration namEnum = request.getParameterNames();

    Iterator iterator = new Iterator() {
      public boolean hasNext() {
        return namEnum.hasMoreElements();
      }

      public Object next() {
        return namEnum.nextElement();
      }

      public void remove() {
        throw new UnsupportedOperationException();
      }
    };

    return iterator;
  }

  public Locale getRequestLocale() {
    return request.getLocale();
  }

  public String getRequestPathInfo() {
    return ((HttpServletRequest) request).getPathInfo();
  }

  public Cookie[] getRequestCookies() {
    return ((HttpServletRequest) request).getCookies();
  }

  public String getRequestContextPath() {
    return ((HttpServletRequest) request).getContextPath();
  }

  public String getInitParameter(String name) {
    return servletContext.getInitParameter(name);
  }

  public Set getResourcePaths(String path) {
    return servletContext.getResourcePaths(path);
  }

  public InputStream getResourceAsStream(String path) {
    return servletContext.getResourceAsStream(path);
  }

  public URL getResource(String path) {
    URL url;
    try {
      url = servletContext.getResource(path);
    } catch (MalformedURLException e) {
      return null;
    }
    return url;
  }

  public String encodeActionURL(String sb) {
    return ((HttpServletResponse) response).encodeURL(sb);
  }

  public String encodeResourceURL(String sb) {
    return ((HttpServletResponse) response).encodeURL(sb);
  }

  public String encodeNamespace(String aValue) {
    return aValue;
  }

  public String encodeURL(String url) {
    return ((HttpServletResponse) response).encodeURL(url);
  }

  public void dispatch(String requestURI)
      throws IOException, FacesException {

    RequestDispatcher requestDispatcher
        = request.getRequestDispatcher(requestURI);

    // get original request:
    ServletRequest originalRequest;
    if (request instanceof TobagoMultipartFormdataRequest) {
      originalRequest
          = ((TobagoMultipartFormdataRequest) request).getRequest();
    } else {
      originalRequest = request;
    }

    try {
      requestDispatcher.forward(originalRequest, response);
    } catch (IOException e) {
      LOG.error("requestURI: '" + requestURI + "'", e);
      throw e;
    } catch (Exception e) {
      LOG.error("requestURI: '" + requestURI + "'", e);
      throw new FacesException(e);
    }
  }

  public void redirect(String requestURI) throws IOException {
    ((HttpServletResponse) response).sendRedirect(requestURI);
    FacesContext.getCurrentInstance().responseComplete();
  }

  public Iterator getRequestLocales() {
    return new LocalesIterator(request.getLocales());
  }

// ///////////////////////////////////////////// bean getter + setter

  public Object getContext() {
    return servletContext;
  }

  public Object getRequest() {
    return request;
  }

  public Object getResponse() {
    return response;
  }

  public Map getRequestMap() {
    if (requestMap == null) {
      requestMap = new RequestMap(request);
    }
    return requestMap;
  }

  public Map getRequestParameterMap() {
    if (requestParameterMap == null) {
      requestParameterMap = new RequestParameterMap(request);
    }
    return requestParameterMap;
  }

  public void log(String message) {
    LOG.info(message); // fixme jsfbeta: Is this nice?
  }

  public void log(String message, Throwable e) {
    LOG.info(message, e); // fixme   jsfbeta: Is this nice?
  }

  public boolean isUserInRole(String role) {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return httpRequest.isUserInRole(role);
  }

  public String getRemoteUser() {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return httpRequest.getRemoteUser();
  }

  public String getAuthType() {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return httpRequest.getAuthType();
  }

  public Principal getUserPrincipal() {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return httpRequest.getUserPrincipal();
  }

  public String getRequestServletPath() {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    return httpRequest.getServletPath();
  }


// ///////////////////////////////////////////// not implemented yet!



  public Map getApplicationMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }

  public Map getSessionMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }

  public Map getRequestParameterValuesMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }

  public Map getRequestHeaderMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }

  public Map getRequestHeaderValuesMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }

  public Map getRequestCookieMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }

  public Map getInitParameterMap() {
    LOG.error(
        "This method isn't already implemented, and should not be called!"); //fixme: ea4
    return null;
  }


  private static class LocalesIterator implements Iterator {

    private Enumeration locales;

    public LocalesIterator(Enumeration locales) {
      this.locales = locales;
    }

    public boolean hasNext() {
      return locales.hasMoreElements();
    }

    public Object next() {
      return locales.nextElement();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

}
