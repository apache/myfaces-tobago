/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.myfaces.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * User: weber
 * Date: Jun 14, 2005
 * Time: 5:40:04 PM
 */
public class MessageFactory {


  private static final Log LOG = LogFactory.getLog(MessageFactory.class);

  private static Map<Locale,ResourceBundle> facesMessagesMap
      = new HashMap<Locale, ResourceBundle>();

  public static FacesMessage createFacesMessage(FacesContext facesContext,
      String key, FacesMessage.Severity severity) {
    return createFacesMessage(facesContext, "tobago", key, severity);
  }
  public static FacesMessage createFacesMessage(FacesContext facesContext,
      String bundle, String key, FacesMessage.Severity severity) {
    String summary = getMessageText(facesContext, bundle, key);
    String detail = getMessageText(facesContext, bundle, key + "_detail");
    return new FacesMessage(severity, summary != null ? summary : key, detail);

  }

  private static String getMessageText(
      FacesContext facesContext, String bundle, String key) {
    String message = ResourceManagerUtil.getProperty(facesContext, bundle, key);
    if (message == null || message.length() < 1) {
      try {
        Locale locale = facesContext.getViewRoot().getLocale();
        message = getFacesMessages(locale).getString(key);
      } catch (Exception e) {
        // ignore at this point
      }
    }
    return message;
  }

  private static ResourceBundle getFacesMessages(Locale locale) {
    ResourceBundle facesMessages = facesMessagesMap.get(locale);
    if (facesMessages == null) {
      facesMessages
          = ResourceBundle.getBundle(FacesMessage.FACES_MESSAGES, locale);
      facesMessagesMap.put(locale, facesMessages);
    }
    return facesMessages;
  }
}
