package org.apache.myfaces.tobago.util;

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

import org.apache.myfaces.tobago.context.ResourceManagerUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public final class MessageFactory {

  public MessageFactory() {
    // utils class
  }

  private static final Map<Locale, ResourceBundle> FACES_MESSAGES_MAP = new HashMap<Locale, ResourceBundle>();

  public static FacesMessage createFacesMessage(
      FacesContext facesContext, String key, FacesMessage.Severity severity, Object[] args) {
    return createFacesMessage(facesContext, "tobago", key, severity, args);
  }

  public static FacesMessage createFacesMessage(
      FacesContext facesContext, String key, FacesMessage.Severity severity) {
    return createFacesMessage(facesContext, key, severity, new Object[0]);
  }

  public static FacesMessage createFacesMessage(
      FacesContext facesContext, String bundle, String key, FacesMessage.Severity severity, Object[] args) {
    String summary = getMessageText(facesContext, bundle, key);
    String detail = getMessageText(facesContext, bundle, key + "_detail");
    if (args != null && args.length > 0) {
      Locale locale = getLocale(facesContext);
      if (summary != null) {
        MessageFormat format = new MessageFormat(summary, locale);
        summary = format.format(args);
      }

      if (detail != null) {
        MessageFormat format = new MessageFormat(detail, locale);
        detail = format.format(args);
      }
    }
    return new FacesMessage(severity, summary != null ? summary : key, detail);
  }

  public static Locale getLocale(FacesContext facesContext) {
    UIViewRoot root = facesContext.getViewRoot();
    Locale locale;
    if (root != null) {
      locale = root.getLocale();
    } else {
      locale = facesContext.getApplication().getViewHandler().calculateLocale(facesContext);
    }
    return locale;
  }

  public static FacesMessage createFacesMessage(
      FacesContext facesContext, String bundle, String key, FacesMessage.Severity severity) {
    return createFacesMessage(facesContext, bundle, key, severity, new Object[0]);
  }

  public static String getMessageText(
      FacesContext facesContext, String bundle, String key) {
    String message = ResourceManagerUtil.getProperty(facesContext, bundle, key);
    if (message == null || message.length() < 1) {
      try {
        Locale locale = getLocale(facesContext);
        message = getFacesMessages(locale).getString(key);
      } catch (Exception e) {
        /* ignore at this point */
      }
    }
    return message;
  }

  public static ResourceBundle getFacesMessages(Locale locale) {
    ResourceBundle facesMessages = FACES_MESSAGES_MAP.get(locale);
    if (facesMessages == null) {
      facesMessages
          = ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES, locale);
      FACES_MESSAGES_MAP.put(locale, facesMessages);
    }
    return facesMessages;
  }
}
