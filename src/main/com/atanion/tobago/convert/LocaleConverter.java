/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 15.04.2004 19:00:46.
 * $Id$
 */
package com.atanion.tobago.convert;

import com.atanion.util.StringUtils;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Locale;

public class LocaleConverter implements Converter {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public Object getAsObject(
      FacesContext facesContext, UIComponent component, String value) {
    Locale locale = null;
    String[] strings = StringUtils.split(value, "_");
    switch (strings.length) {
      case 1:
        locale = new Locale(strings[0]);
        break;
      case 2:
        locale = new Locale(strings[0], strings[1]);
        break;
      case 3:
        locale = new Locale(strings[0], strings[1], strings[2]);
        break;
    }
    if (locale == null) {
      throw new ConverterException("Can't parse " + value + " to a locale.");
    }
    return locale;
  }

  public String getAsString(
      FacesContext facesContext, UIComponent component, Object value) {
    return value.toString();
  }

// ///////////////////////////////////////////// bean getter + setter

}
