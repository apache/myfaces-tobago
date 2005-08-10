/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:58:25.
 * $Id: MockHttpServletRequest.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package org.apache.myfaces.tobago.mock.servlet;

import org.apache.commons.collections.iterators.IteratorEnumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletInputStream;
import javax.servlet.RequestDispatcher;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;
import java.util.Locale;
import java.io.IOException;
import java.io.BufferedReader;
import java.security.Principal;

public class MockHttpServletRequest implements HttpServletRequest {
// ----------------------------------------------------------------- attributes

  private Map parameters = new HashMap();
  private Map attributes = new HashMap();
  private MockHttpSession httpSession = null;

  private byte[] body;

// --------------------------------------------------------------- constructors

  public MockHttpServletRequest() {
  }

  public MockHttpServletRequest(byte[] body) {
    this.body = body;
  }

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface HttpServletRequest

  public String getAuthType() {
    return null;
  }

  public Cookie[] getCookies() {
    return new Cookie[0];
  }

  public long getDateHeader(String s) {
    return 0;
  }

  public String getHeader(String reference) {
    if (reference.equals("Content-type")) {
      return "multipart/form-data; boundary=xxx";
    }
    return null;
  }

  public Enumeration getHeaders(String s) {
    return null;
  }

  public Enumeration getHeaderNames() {
    return null;
  }

  public int getIntHeader(String s) {
    return 0;
  }

  public String getMethod() {
    return null;
  }

  public String getPathInfo() {
    return null;
  }

  public String getPathTranslated() {
    return null;
  }

  public String getContextPath() {
    return null;
  }

  public String getQueryString() {
    return null;
  }

  public String getRemoteUser() {
    return null;
  }

  public boolean isUserInRole(String s) {
    return false;
  }

  public Principal getUserPrincipal() {
    return null;
  }

  public String getRequestedSessionId() {
    return null;
  }

  public String getRequestURI() {
    return null;
  }

  public StringBuffer getRequestURL() {
    return null;
  }

  public String getServletPath() {
    return null;
  }

  public HttpSession getSession(boolean flag) {
    if (flag && httpSession == null) {
      httpSession = new MockHttpSession();
    }
    return httpSession;
  }

  public HttpSession getSession() {
    return httpSession;
  }

  public boolean isRequestedSessionIdValid() {
    return false;
  }

  public boolean isRequestedSessionIdFromCookie() {
    return false;
  }

  public boolean isRequestedSessionIdFromURL() {
    return false;
  }

  public boolean isRequestedSessionIdFromUrl() {
    return false;
  }

// ---------------------------- interface ServletRequest

  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  public Enumeration getAttributeNames() {
    return null;
  }

  public String getCharacterEncoding() {
    return null;
  }

  public void setCharacterEncoding(java.lang.String env) {
  }

  public int getContentLength() {
    return body.length;
  }

  public String getContentType() {
    return "multipart/form-data; boundary=xxx";
  }

  public ServletInputStream getInputStream() throws IOException {
    return new MockServletInputStream(body);
  }

  public String getParameter(String name) {
    String values[] = (String[]) parameters.get(name);
    if (values != null) {
      return (values[0]);
    } else {
      return (null);
    }
  }

  public Map getParameterMap() {
    return parameters;
  }

  public String getProtocol() {
    return null;
  }

  public String getScheme() {
    return null;
  }

  public String getServerName() {
    return null;
  }

  public int getServerPort() {
    return 0;
  }

  public BufferedReader getReader() throws IOException {
    return null;
  }

  public String getRemoteAddr() {
    return null;
  }

  public String getRemoteHost() {
    return null;
  }

  public void setAttribute(String name, Object o) {
    attributes.put(name, o);
  }

  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  public Locale getLocale() {
    return null;
  }

  public Enumeration getLocales() {
    return null;
  }

  public boolean isSecure() {
    return false;
  }

  public RequestDispatcher getRequestDispatcher(String s) {
    return null;
  }

  /** @deprecated */
  public String getRealPath(String s) {
    return null;
  }

  public int getRemotePort() {
    return 0;
  }

  public String getLocalName() {
    return null;
  }

  public String getLocalAddr() {
    return null;
  }

  public int getLocalPort() {
    return 0;
  }

// ----------------------------------------------------------- business methods

  public void addParameter(String name, String values[]) {
    parameters.put(name, values);
  }

// ---------------------------------------------------------- canonical methods

  public String toString() {
    StringBuffer buffer = new StringBuffer();
    Enumeration enumeration = getParameterNames();
    while (enumeration.hasMoreElements()) {
      String key = (String) enumeration.nextElement();
      String[] values = getParameterValues(key);
      buffer.append("parameter:");
      buffer.append(key);
      buffer.append("=[");
      for (int i = 0; i < values.length; i++) {
        String value = values[i];
        buffer.append(value);
        if (i < values.length - 1) {
          buffer.append(",");
        }
      }
      buffer.append("]\n");
    }
    return buffer.toString();
  }

  public Enumeration getParameterNames() {
    return (new IteratorEnumeration(parameters.keySet().iterator()));
  }

  public String[] getParameterValues(String name) {
    return (String[]) parameters.get(name);
  }
}