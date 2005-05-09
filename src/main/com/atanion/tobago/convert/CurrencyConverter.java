/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 09.12.2003 15:52:53.
 * $Id$
 */
package com.atanion.tobago.convert;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import java.util.Currency;

public class CurrencyConverter implements Converter {

// ----------------------------------------------------------------- interfaces

// ---------------------------- interface Converter

  public Object getAsObject(FacesContext facesContext, UIComponent component,
      String string)
      throws ConverterException {
    return Currency.getInstance(string);
  }

  public String getAsString(FacesContext facesContext, UIComponent component,
      Object object)
      throws ConverterException {
    try {
      return ((Currency) object).getCurrencyCode();
    } catch (ClassCastException e) {
      throw new ConverterException("object='" + object + "'", e);
    }
  }
}

