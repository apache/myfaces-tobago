/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:44:18
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.Validator;
import javax.servlet.http.HttpServletResponse;

public class HtmlUtils {

  private static final Log LOG = LogFactory.getLog(HtmlUtils.class);
  public static final String LAYOUT_ATTRIBUTE_PREFIX = "layout.";

  public static String generateAttribute(String name, Object value) {
    String stringValue;
    if (value == null) {
      stringValue = null;
    } else if (value instanceof String) {
      stringValue = (String) value;
    } else {
      stringValue = value.toString();
    }
    return (stringValue != null && stringValue.length() > 0)
        ? name + "=\"" + value + "\""
        : "";
  }

  public static String generateAttribute(String name, Object value,
      String defaultValue) {
    String stringValue;
    if (value == null) {
      stringValue = null;
    } else if (value instanceof String) {
      stringValue = (String) value;
    } else {
      stringValue = value.toString();
    }
    return name + "=\""
        + (stringValue != null && stringValue.length() > 0
        ? stringValue
        : defaultValue)
        + "\"";
  }

  public static String generateAttribute(String name, boolean value) {
    return value ? generateAttribute(name, name) : "";
  }

  public static String generateAttribute(String name, int value) {
    return generateAttribute(name, String.valueOf(value));
  }

  public static String generatePositiveAttribute(String name, int value) {
    return value > 0
        ? generateAttribute(name, String.valueOf(value))
        : "";
  }

  public static String generateContent(String value) {
    return value != null ? value : "";
  }

  public static String generateAttribute(String name, UIComponent component) {
    Object attribute = component.getAttributes().get(name);
    if (attribute != null) {
      return generateAttribute(name, attribute.toString());
    } else {
      return "";
    }
  }

  public static String generateAttributeFromKey(String name,
      UIComponent component, String key) {
    Object attribute = component.getAttributes().get(key);
    if (attribute != null) {
      return generateAttribute(name, attribute.toString());
    } else {
      return "";
    }
  }

  public static String generateAttribute(String name, UIComponent component,
      String defaultValue) {
    Object attribute = component.getAttributes().get(name);
    if (attribute != null) {
      return generateAttribute(name, attribute, defaultValue);
    } else {
      return generateAttribute(name, (Object) null, defaultValue);
    }
  }

  public static String generateAttributeAppend(String name,
      UIComponent component, String appendValue) {
    return generateAttribute(name, appendAttribute(component, name, appendValue));
  }

  public static String appendAttribute(UIComponent component, String name,
      String appendValue) {
    Object attribute = component.getAttributes().get(name);
    return attribute != null
        ? attribute.toString() + " " + appendValue : appendValue;    
  }

  public static String preToHtml(String preformatedText) {
    if (preformatedText == null) {
      return null;
    }
    StringBuffer sb = new StringBuffer();
    char[] preformatedChars = preformatedText.toCharArray();
    for (int i = 0; i < preformatedChars.length; i++) {
      switch (preformatedChars[i]) {
        case '\n':
          sb.append("<br />");
          break;
        case '\r':
          break;
        case ' ':
          sb.append("&nbsp;");
          break;
        case '\t':
          sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
          break;
        default:
          sb.append(preformatedChars[i]);
      }
    }


    return sb.toString();
  }

  public static String generateOnchange(UIInput component,
      FacesContext facesContext) {

    StringBuffer buffer = new StringBuffer();
    Validator[] validators = component.getValidators();
    for (int i = 0; i < validators.length; i++) {
      if (validators[i] instanceof LongRangeValidator) {
        String functionCall = "validateLongRange('"
            + component.getClientId(facesContext) + "')";
        LOG.debug("validator functionCall: " + functionCall);
        buffer.append(functionCall);
      } else {
        buffer.append("true");
      }
      if (i + 1 < validators.length) { // is not last
        buffer.append(" && ");
      }
    }

    Object onchange = component.getAttributes().get(
        TobagoConstants.ATTR_ONCHANGE);
    if (onchange != null) { // append the onchange attribute
      if (buffer.length() > 0) {
        buffer.append(" && ");
      }
      buffer.append(onchange);
    }

    if (buffer.length() > 0) { // has content ?
      return buffer.toString();
    } else {
      return null;
    }
  }

  public static int getLayoutAttributeAsInt(UIComponent component, String name,
      int defaultValue) {

    Integer i = (Integer) component.getAttributes().get(
        LAYOUT_ATTRIBUTE_PREFIX + name);
    if (i != null) {
      return i.intValue();
    } else {
      return defaultValue;
    }
  }

  // fixme: is this longer needed?
  public static String generateUrl(FacesContext facesContext, String url) {
    String result;
    HttpServletResponse response = (HttpServletResponse)
        facesContext.getExternalContext().getResponse();
    Application application = facesContext.getApplication();
    ViewHandler viewHandler = application.getViewHandler();
    if (!url.startsWith("/")) { // extern
      result = response.encodeURL(url);
    } else {
      result = viewHandler.getActionURL(facesContext, url);
    }
    return result;
  }

}
