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

  public static String appendAttribute(UIComponent component, String name,
      String appendValue) {
    Object attribute = component.getAttributes().get(name);
    return attribute != null
        ? attribute.toString() + " " + appendValue : appendValue;    
  }

  public static String generateOnchange(UIInput component,
      FacesContext facesContext) {

    StringBuffer buffer = new StringBuffer();
    Validator[] validators = component.getValidators();
    for (int i = 0; i < validators.length; i++) {
      if (validators[i] instanceof LongRangeValidator) {
        String functionCall = "validateLongRange('"
            + component.getClientId(facesContext) + "')";
        if (LOG.isDebugEnabled()) {
          LOG.debug("validator functionCall: " + functionCall);
        }
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
