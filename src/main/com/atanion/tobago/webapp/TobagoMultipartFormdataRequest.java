/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 30.01.2003 17:00:56.
 * $Id$
 */
package com.atanion.tobago.webapp;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TobagoMultipartFormdataRequest implements HttpServletRequest {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(
      TobagoMultipartFormdataRequest.class);

// ///////////////////////////////////////////// attribute

  private HttpServletRequest request;

  private Map parameters;

  private Map fileItems;

// ///////////////////////////////////////////// constructor

  TobagoMultipartFormdataRequest(HttpServletRequest request) {
    this.request = request;
    String contentType = request.getContentType();
    if (contentType == null ||
        !contentType.toLowerCase().startsWith("multipart/form-data")) {
      String errorText = "contentType is not multipart/form-data but '"
          + contentType + "'";
      LOG.error(errorText);
      throw new FacesException(errorText);
    } else {
      parameters = new HashMap();
      fileItems = new HashMap();
      DiskFileUpload fileUpload = new DiskFileUpload();
      fileUpload.setSizeMax(1024 * 1024);
      fileUpload.setRepositoryPath(System.getProperty("java.io.tmpdir"));
      // todo: make sizeMax and repositoryPath configurable
      List itemList;
      try {
        itemList = fileUpload.parseRequest(request);
      } catch (FileUploadException e) {
        LOG.error(e);
        throw new FacesException(e);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("parametercount = " + itemList.size());
      }
      Iterator items = itemList.iterator();
      while (items.hasNext()) {
        FileItem item = (FileItem) items.next();
        String key = item.getFieldName();
        if (LOG.isDebugEnabled()) {
          String value = item.getString();
          if (value.length() > 100) {
            value = value.substring(0, 100) + " [...]";
          }
          LOG.debug(
              "Parameter : '" + key + "'='" + value + "' isFormField="
              + item.isFormField());
        }
        if (item.isFormField()) {
          Object inStock = parameters.get(key);
          if (inStock == null) {
            String[] values = {item.getString()};
            parameters.put(key, values);
          } else if (inStock instanceof String[]) { // double (or more) parameter
            String[] oldValues = (String[]) inStock;
            String[] values = new String[oldValues.length + 1];
            int i = 0;
            for (; i < oldValues.length; i++) {
              values[i] = oldValues[i];
            }
            values[i] = item.getString();
            parameters.put(key, values);
          } else {
            LOG.error(
                "Program error. Unsupported class: "
                + inStock.getClass().getName());
          }
        } else {
          fileItems.put(key, item);
        }
      }
    }
  }

// ////////////////////////////////////////////// code

  public FileItem getFileItem(String key) {
    if (fileItems != null) {
      return (FileItem) fileItems.get(key);
    }
    return null;
  }

// ////////////////////////////////////////////// getter

  public HttpServletRequest getRequest() {
    return request;
  }
  
// ////////////////////////////////////////////// overwriten


  public String getParameter(String key) {
    String parameter;
    String[] values = (String[]) parameters.get(key);
    if (values == null) {
      parameter = null;
    } else {
      parameter = values[0];
    }
    return parameter;
  }

  public Enumeration getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  public String[] getParameterValues(String key) {
    return (String[]) parameters.get(key);
  }

  public Map getParameterMap() {
    return parameters;
  }

// ////////////////////////////////////////////// deligates

  public String getAuthType() {
    return request.getAuthType();
  }

  public Cookie[] getCookies() {
    return request.getCookies();
  }

  public long getDateHeader(String s) {
    return request.getDateHeader(s);
  }

  public String getHeader(String s) {
    return request.getHeader(s);
  }

  public Enumeration getHeaders(String s) {
    return request.getHeaders(s);
  }

  public Enumeration getHeaderNames() {
    return request.getHeaderNames();
  }

  public int getIntHeader(String s) {
    return request.getIntHeader(s);
  }

  public String getMethod() {
    return request.getMethod();
  }

  public String getPathInfo() {
    return request.getPathInfo();
  }

  public String getPathTranslated() {
    return request.getPathTranslated();
  }

  public String getContextPath() {
    return request.getContextPath();
  }

  public String getQueryString() {
    return request.getQueryString();
  }

  public String getRemoteUser() {
    return request.getRemoteUser();
  }

  public boolean isUserInRole(String s) {
    return request.isUserInRole(s);
  }

  public Principal getUserPrincipal() {
    return request.getUserPrincipal();
  }

  public String getRequestedSessionId() {
    return request.getRequestedSessionId();
  }

  public String getRequestURI() {
    return request.getRequestURI();
  }

  public StringBuffer getRequestURL() {
    return request.getRequestURL();
  }

  public String getServletPath() {
    return request.getServletPath();
  }

  public HttpSession getSession(boolean b) {
    return request.getSession(b);
  }

  public HttpSession getSession() {
    return request.getSession();
  }

  public boolean isRequestedSessionIdValid() {
    return request.isRequestedSessionIdValid();
  }

  public boolean isRequestedSessionIdFromCookie() {
    return request.isRequestedSessionIdFromCookie();
  }

  public boolean isRequestedSessionIdFromURL() {
    return request.isRequestedSessionIdFromURL();
  }

  /**
   * @deprecated
   */
  public boolean isRequestedSessionIdFromUrl() {
    return request.isRequestedSessionIdFromUrl();
  }

  public Object getAttribute(String s) {
    return request.getAttribute(s);
  }

  public Enumeration getAttributeNames() {
    return request.getAttributeNames();
  }

  public String getCharacterEncoding() {
    return request.getCharacterEncoding();
  }

  public void setCharacterEncoding(String s)
      throws UnsupportedEncodingException {
    request.setCharacterEncoding(s);
  }

  public int getContentLength() {
    return request.getContentLength();
  }

  public String getContentType() {
    return request.getContentType();
  }

  public ServletInputStream getInputStream() throws IOException {
    return request.getInputStream();
  }

  public String getProtocol() {
    return request.getProtocol();
  }

  public String getScheme() {
    return request.getScheme();
  }

  public String getServerName() {
    return request.getServerName();
  }

  public int getServerPort() {
    return request.getServerPort();
  }

  public BufferedReader getReader() throws IOException {
    return request.getReader();
  }

  public String getRemoteAddr() {
    return request.getRemoteAddr();
  }

  public String getRemoteHost() {
    return request.getRemoteHost();
  }

  public void setAttribute(String s, Object o) {
    request.setAttribute(s, o);
  }

  public void removeAttribute(String s) {
    request.removeAttribute(s);
  }

  public Locale getLocale() {
    return request.getLocale();
  }

  public Enumeration getLocales() {
    return request.getLocales();
  }

  public boolean isSecure() {
    return request.isSecure();
  }

  public RequestDispatcher getRequestDispatcher(String s) {
    return request.getRequestDispatcher(s);
  }

  /**
   * @deprecated
   */
  public String getRealPath(String s) {
    return request.getRealPath(s);
  }

}
