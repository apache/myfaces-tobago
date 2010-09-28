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

import org.apache.myfaces.tobago.application.LabelValueBindingFacesMessage;
import org.apache.myfaces.tobago.application.LabelValueExpressionFacesMessage;
import org.apache.myfaces.tobago.compat.FacesUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// TODO merge with MessageFactory
public class MessageUtils {

    private static final String DETAIL_SUFFIX = "_detail";
  
    public static void addMessage(FacesContext facesContext, UIComponent component, FacesMessage.Severity severity,
        String messageId, Object[] args) {
      facesContext.addMessage(component.getClientId(facesContext),
          getMessage(facesContext, facesContext.getViewRoot().getLocale(), severity, messageId, args));
    }
  
    public static FacesMessage getMessage(FacesContext facesContext, Locale locale,
        FacesMessage.Severity severity, String messageId, Object... args) {
  
      String detail;
      ResourceBundle appBundle = getApplicationBundle(facesContext, locale);
      String summary = getBundleString(appBundle, messageId);
      if (summary != null) {
        detail = getBundleString(appBundle, messageId + DETAIL_SUFFIX);
      } else {
        ResourceBundle defBundle = getDefaultBundle(facesContext, locale);
        summary = getBundleString(defBundle, messageId);
        if (summary != null) {
          detail = getBundleString(defBundle, messageId + DETAIL_SUFFIX);
        } else {
          //Try to find detail alone
          detail = getBundleString(appBundle, messageId + DETAIL_SUFFIX);
          if (detail != null) {
            summary = null;
          } else {
            detail = getBundleString(defBundle, messageId + DETAIL_SUFFIX);
            if (detail != null) {
              summary = null;
            } else {
              //Neither detail nor summary found
              facesContext.getExternalContext().log("No message with id " + messageId + " found in any bundle");
              return new FacesMessage(severity, messageId, null);
            }
          }
        }
      }
  
    if (FacesUtils.supportsEL()) {
      if (args != null && args.length > 0) {
        MessageFormat format;
        if (summary != null) {
          format = new MessageFormat(summary, locale);
          summary = format.format(args);
        }

        if (detail != null) {
          format = new MessageFormat(detail, locale);
          detail = format.format(args);
        }
      }
      return new LabelValueExpressionFacesMessage(severity, summary, detail);
    } else {
      return new LabelValueBindingFacesMessage(severity, summary, detail, locale, args);
    }
    }
    
    private static String getBundleString(ResourceBundle bundle, String key) {
      try {
        return bundle == null ? null : bundle.getString(key);
      } catch (MissingResourceException e) {
        return null;
      }
    }
  
    private static ResourceBundle getApplicationBundle(FacesContext facesContext, Locale locale) {
      String bundleName = facesContext.getApplication().getMessageBundle();
      return bundleName != null ? getBundle(facesContext, locale, bundleName) : null;
    }
  
    private static ResourceBundle getDefaultBundle(FacesContext facesContext, Locale locale) {
      return getBundle(facesContext, locale, FacesMessage.FACES_MESSAGES);
    }
  
    private static ResourceBundle getBundle(FacesContext facesContext, Locale locale, String bundleName) {
      try {
        return ResourceBundle.getBundle(bundleName, locale, MessageUtils.class.getClassLoader());
      } catch (MissingResourceException ignore2) {
        try {
          return ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
        } catch (MissingResourceException damned) {
          facesContext.getExternalContext().log("resource bundle " + bundleName + " could not be found");
          return null;
        }
      }
    }
  
  public static String getLabel(FacesContext facesContext, UIComponent component) {
    Object label = component.getAttributes().get("label");
    if (label != null) {
      return label.toString();
    }
    if (FacesUtils.hasValueBindingOrValueExpression(component, "label")) {
      return FacesUtils.getExpressionString(component, "label");
    }
    return component.getClientId(facesContext);
  }

  public static String getFormatedMessage(String message, Locale locale, Object... args) {
    if (args != null && args.length > 0 && message != null) {
      MessageFormat format = new MessageFormat(message, locale);
      return format.format(args);
    }
    return message;
  }

}
