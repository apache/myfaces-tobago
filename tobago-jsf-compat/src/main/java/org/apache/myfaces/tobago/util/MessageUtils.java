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

import org.apache.commons.beanutils.ConstructorUtils;
import org.apache.myfaces.tobago.application.LabelValueExpressionFacesMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Utility to create and add {@link FacesMessage} object to the context.
 * The message will be internationalized with a bundle in the following order:
 * <ol>
 * <li>Application bundle</li>
 * <li>Tobago bundle</li>
 * <li>Default JSF bundle</li>
 * </ol>
 */
public class MessageUtils {

  private static final Logger LOG = LoggerFactory.getLogger(MessageUtils.class);

  private static final String DETAIL_SUFFIX = "_detail";

  public static void addMessage(
      FacesContext facesContext, UIComponent component, FacesMessage.Severity severity,
      String messageId, Object[] args) {
    facesContext.addMessage(component.getClientId(facesContext),
        getMessage(facesContext, facesContext.getViewRoot().getLocale(), severity, messageId, args));
  }

  public static FacesMessage getMessage(
      FacesContext facesContext, Locale locale,
      FacesMessage.Severity severity, String messageId, Object... args) {

    ResourceBundle appBundle = getApplicationBundle(facesContext, locale);
    String summary = getBundleString(appBundle, messageId);
    String detail = getBundleString(appBundle, messageId + DETAIL_SUFFIX);

    if (summary == null || detail == null) {
      ResourceBundle tobagoBundle = getTobagoBundle();
      if (summary == null) {
        summary = getBundleString(tobagoBundle, messageId);
      }
      if (detail == null) {
        detail = getBundleString(tobagoBundle, messageId + DETAIL_SUFFIX);
      }

      if (summary == null || detail == null) {
        ResourceBundle defBundle = getDefaultBundle(facesContext, locale);
        if (summary == null) {
          summary = getBundleString(defBundle, messageId);
        }
        if (detail == null) {
          detail = getBundleString(defBundle, messageId + DETAIL_SUFFIX);
        }
      }
    }

    if (summary == null && detail == null) {
      //Neither detail nor summary found
      facesContext.getExternalContext().log("No message with id " + messageId + " found in any bundle");
      return new FacesMessage(severity, messageId, null);
    }

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

  private static ResourceBundle getTobagoBundle() {
    // XXX This is ugly, can be removed after merging tobago-jsf-compat to tobago-core
    try {
      Class clazz = Class.forName("org.apache.myfaces.tobago.context.TobagoResourceBundle");
      Object bundle = ConstructorUtils.invokeConstructor(clazz, new Object[0]);
      return (ResourceBundle) bundle;
    } catch (Exception e) {
      LOG.error("Can't create TobagoResourceBundle, but it should be in the core.", e);
      return null; // should not be possible.
    }
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
    final ValueExpression expression = component.getValueExpression("label");
    if (expression != null) {
      return expression.getExpressionString();
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
