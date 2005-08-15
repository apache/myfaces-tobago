/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 03.12.2004 00:08:01.
 * $Id: AddressConverter.java,v 1.1.1.1 2004/12/15 12:51:35 lofwyr Exp $
 */
package com.atanion.tobago.demo.address;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class AddressConverter implements Converter {

  private static final Log LOG = LogFactory.getLog(AddressConverter.class);

  public Object getAsObject(
      FacesContext facesContext, UIComponent component, String reference) {
    if (reference == null || reference.length() == 0) {
      return null;
    }
    try {
      return new InternetAddress(reference);
    } catch (AddressException e) {
      LOG.error("", e);
      throw new ConverterException(e);
    }
  }

  public String getAsString(
      FacesContext facesContext, UIComponent component, Object object) {
    return object.toString();
  }

}
