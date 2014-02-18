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

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

public final class ClientPropertiesKey implements Serializable {

  private static final String KEY_IN_FACES_CONTEXT = ClientPropertiesKey.class.getName();

  private final String contentType;
  private final Theme theme;
  private final UserAgent userAgent;
  private final Locale locale;

  private final int hashCode;

  public static ClientPropertiesKey get(final FacesContext facesContext) {
    final Map<Object, Object> attributes = facesContext.getAttributes();
    ClientPropertiesKey key = (ClientPropertiesKey) attributes.get(KEY_IN_FACES_CONTEXT);
    if (key == null) {
      final ClientProperties clientProperties = VariableResolverUtils.resolveClientProperties(facesContext);
      key = new ClientPropertiesKey(clientProperties, facesContext.getViewRoot());
      attributes.put(KEY_IN_FACES_CONTEXT, key);
    }
    return key;
  }

  public static void reset(final FacesContext facesContext) {
    final Map<Object, Object> attributes = facesContext.getAttributes();
    attributes.remove(KEY_IN_FACES_CONTEXT);
  }

  private ClientPropertiesKey(final ClientProperties clientProperties, final UIViewRoot viewRoot) {
    contentType = clientProperties.getContentType();
    theme = clientProperties.getTheme();
    userAgent = clientProperties.getUserAgent();
    locale = viewRoot.getLocale();
    hashCode = calcHashCode();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final ClientPropertiesKey that = (ClientPropertiesKey) o;

    if (!contentType.equals(that.contentType)) {
      return false;
    }
    if (locale == null) {
      if (that.locale != null) {
        return false;
      }
    } else {
      if (!locale.equals(that.locale)) {
        return false;
      }
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
    if (locale != null) {
      result = 31 * result + locale.hashCode();
    }
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
