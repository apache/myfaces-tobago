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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.context.ResourceManagerUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @deprecated since Tobago 1.5.0. Please use {@link MessageUtils} instead
 */
@Deprecated
public final class MessageFactory {

  private MessageFactory() {
    // utils class
  }

  private static final Map<Locale, ResourceBundle> FACES_MESSAGES_MAP = new HashMap<Locale, ResourceBundle>();

  @Deprecated
  public static FacesMessage createFacesMessage(
      final FacesContext facesContext, final String key, final FacesMessage.Severity severity, final Object[] args) {
    return createFacesMessage(facesContext, "tobago", key, severity, args);
  }

  @Deprecated
  public static FacesMessage createFacesMessage(
      final FacesContext facesContext, final String key, final FacesMessage.Severity severity) {
    return createFacesMessage(facesContext, key, severity, new Object[0]);
  }

  @Deprecated
  public static FacesMessage createFacesMessage(
      final FacesContext facesContext, final String bundle, final String key, final FacesMessage.Severity severity,
      final Object[] args) {
    String summary = getMessageText(facesContext, bundle, key);
    String detail = getMessageText(facesContext, bundle, key + "_detail");
    if (args != null && args.length > 0) {
      final Locale locale = getLocale(facesContext);
      if (summary != null) {
        final MessageFormat format = new MessageFormat(summary, locale);
        summary = format.format(args);
      }

      if (detail != null) {
        final MessageFormat format = new MessageFormat(detail, locale);
        detail = format.format(args);
      }
    }
    return new FacesMessage(severity, summary != null ? summary : key, detail);
  }

  @Deprecated
  public static Locale getLocale(final FacesContext facesContext) {
    final UIViewRoot root = facesContext.getViewRoot();
    final Locale locale;
    if (root != null) {
      locale = root.getLocale();
    } else {
      locale = facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
    }
    return locale;
  }

  @Deprecated
  public static FacesMessage createFacesMessage(
      final FacesContext facesContext, final String bundle, final String key, final FacesMessage.Severity severity) {
    return createFacesMessage(facesContext, bundle, key, severity, new Object[0]);
  }

  @Deprecated
  public static String getMessageText(
      final FacesContext facesContext, final String bundle, final String key) {
    String message = ResourceManagerUtils.getProperty(facesContext, bundle, key);
    if (message == null || message.length() < 1) {
      try {
        final Locale locale = getLocale(facesContext);
        message = getFacesMessages(locale).getString(key);
      } catch (final Exception e) {
        /* ignore at this point */
      }
    }
    return message;
  }

  @Deprecated
  public static ResourceBundle getFacesMessages(final Locale locale) {
    ResourceBundle facesMessages = FACES_MESSAGES_MAP.get(locale);
    if (facesMessages == null) {
      facesMessages
          = ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES, locale);
      FACES_MESSAGES_MAP.put(locale, facesMessages);
    }
    return facesMessages;
  }
}
