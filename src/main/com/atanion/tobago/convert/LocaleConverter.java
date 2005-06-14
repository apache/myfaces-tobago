/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 15.04.2004 19:00:46.
 * $Id$
 */
package com.atanion.tobago.convert;

import com.atanion.util.LocaleUtil;

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
    Locale locale = LocaleUtil.createLocale(value);
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
