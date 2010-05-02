package org.apache.myfaces.tobago.mock.faces;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

public class MockExternalContext extends ExternalContext {

  private ServletContext context = null;
  private ServletRequest request = null;
  private ServletResponse response = null;

  public MockExternalContext(ServletContext context,
      ServletRequest request,
      ServletResponse response) {
    this.context = context;
    this.request = request;
    this.response = response;
  }

  public Object getSession(boolean create) {
    if (sessionMap == null && create) {
      sessionMap = new MockSessionMap(((HttpServletRequest) request).getSession(true));
    }
    return sessionMap;
  }


  public Object getContext() {
    return context;
  }


  public Object getRequest() {
    return request;
  }


  public Object getResponse() {
    return response;
  }


  private Map applicationMap = null;

  public Map getApplicationMap() {
    if (applicationMap == null) {
      applicationMap = new MockApplicationMap(context);
    }
    return applicationMap;
  }


  private Map sessionMap = null;

  public Map getSessionMap() {
    if (sessionMap == null) {
      sessionMap = new MockSessionMap(((HttpServletRequest) request).getSession(true));
    }
    return sessionMap;
  }


  private Map requestMap = null;

  public Map getRequestMap() {
    if (requestMap == null) {
      requestMap = new MockRequestMap(request);
    }
    return requestMap;
  }


  private Map requestParameterMap = null;

  public Map getRequestParameterMap() {
    if (requestParameterMap != null) {
      return (requestParameterMap);
    } else {
      return Collections.EMPTY_MAP;
    }
  }

  public void setRequestParameterMap(Map requestParameterMap) {
    this.requestParameterMap = requestParameterMap;
  }


  public Map getRequestParameterValuesMap() {
    return Collections.EMPTY_MAP;
  }


  public Iterator getRequestParameterNames() {
    throw new UnsupportedOperationException();
  }


  public Map getRequestHeaderMap() {
    return Collections.EMPTY_MAP;      
  }


  public Map getRequestHeaderValuesMap() {
    throw new UnsupportedOperationException();
  }


  public Map getRequestCookieMap() {
    throw new UnsupportedOperationException();
  }


  public Locale getRequestLocale() {
    return request.getLocale();
  }


  public Iterator getRequestLocales() {
    return new LocalesIterator(request.getLocales());
  }


  public String getRequestPathInfo() {
    throw new UnsupportedOperationException();
  }


  public String getRequestContextPath() {
    throw new UnsupportedOperationException();
  }

  public String getRequestServletPath() {
    throw new UnsupportedOperationException();
  }


  public String getInitParameter(String name) {
    return null;
  }


  public Map getInitParameterMap() {
    throw new UnsupportedOperationException();
  }


  public Set getResourcePaths(String path) {
    throw new UnsupportedOperationException();
  }


  public URL getResource(String path) throws MalformedURLException {
    throw new UnsupportedOperationException();
  }


  public InputStream getResourceAsStream(String path) {
    throw new UnsupportedOperationException();
  }


  public String encodeActionURL(String sb) {
    throw new UnsupportedOperationException();
  }


  public String encodeResourceURL(String sb) {
    throw new UnsupportedOperationException();
  }


  public String encodeNamespace(String aValue) {
    return aValue;
  }


  public void dispatch(String requestURI)
      throws IOException, FacesException {
    throw new UnsupportedOperationException();
  }


  public void redirect(String requestURI)
      throws IOException {
    throw new UnsupportedOperationException();
  }


  public void log(String message) {
    context.log(message);
  }


  public void log(String message, Throwable throwable) {
    context.log(message, throwable);
  }


  public String getAuthType() {
    return ((HttpServletRequest) request).getAuthType();
  }

  public String getRemoteUser() {
    return ((HttpServletRequest) request).getRemoteUser();
  }

  public java.security.Principal getUserPrincipal() {
    return ((HttpServletRequest) request).getUserPrincipal();
  }

  public boolean isUserInRole(String role) {
    return ((HttpServletRequest) request).isUserInRole(role);
  }


  private class LocalesIterator implements Iterator {

    public LocalesIterator(Enumeration locales) {
      this.locales = locales;
    }

    private Enumeration locales;

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
