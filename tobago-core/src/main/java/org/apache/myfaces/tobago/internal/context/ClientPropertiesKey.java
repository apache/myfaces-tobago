/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.context;

import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.Theme;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public final class ClientPropertiesKey implements Serializable {

  private static final String KEY_IN_REQUEST = ClientPropertiesKey.class.getName();
  
  private final String contentType;
  private final Theme theme;
  private final UserAgent userAgent;
  private final Locale locale;

  private final int hashCode;

  public static ClientPropertiesKey get(FacesContext facesContext) {
    // todo later: refactor when having JSF 2.0: using attributes of facesContext
    Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
    ClientPropertiesKey key = (ClientPropertiesKey) requestMap.get(KEY_IN_REQUEST);
    if (key == null) {
      ClientProperties clientProperties = VariableResolverUtils.resolveClientProperties(facesContext);
      key = new ClientPropertiesKey(clientProperties);
      requestMap.put(KEY_IN_REQUEST, key);
    }

    return key;
  }

  public static void reset(FacesContext facesContext) {
    Map<String, Object> requestMap = facesContext.getExternalContext().getRequestMap();
    requestMap.remove(KEY_IN_REQUEST);
  }
  
  private ClientPropertiesKey(ClientProperties clientProperties) {
    contentType = clientProperties.getContentType();
    theme = clientProperties.getTheme();
    userAgent = clientProperties.getUserAgent();
    locale = clientProperties.getLocale();
    hashCode = calcHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ClientPropertiesKey that = (ClientPropertiesKey) o;

    if (!contentType.equals(that.contentType)) {
      return false;
    }
    if (!locale.equals(that.locale)) {
      return false;
    }
    if (!theme.equals(that.theme)) {
      return false;
    }
    if (!userAgent.equals(that.userAgent)) {
      return false;
    }

    return true;
  }

  private int calcHashCode() {
    int result = contentType.hashCode();
    result = 31 * result + theme.hashCode();
    result = 31 * result + userAgent.hashCode();
    result = 31 * result + locale.hashCode();
    return result;
  }

  @Override
  public int hashCode() {
    return hashCode;
  }

  @Override
  public String toString() {
    return "ClientPropertiesKey(" + contentType + "," + theme + "," + userAgent + "," + locale + "," + hashCode + ')';
  }

  public String getContentType() {
    return contentType;
  }

  public Theme getTheme() {
    return theme;
  }

  public UserAgent getUserAgent() {
    return userAgent;
  }

  public Locale getLocale() {
    return locale;
  }
}
