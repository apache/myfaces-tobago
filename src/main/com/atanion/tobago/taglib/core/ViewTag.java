/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 08.04.2003 18:45:24.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import com.atanion.tobago.taglib.component.TobagoBodyTag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIViewRoot;
import javax.servlet.jsp.JspException;

public class ViewTag extends TobagoBodyTag {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(ViewTag.class);

// ///////////////////////////////////////////// attribute

  private String locale;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIViewRoot.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return null;
  }

  protected int getDoStartValue() throws JspException {
    return EVAL_BODY_INCLUDE;
  }

  protected int getDoEndValue() throws JspException {
    return EVAL_PAGE;
  }

// ///////////////////////////////////////////// bean getter + setter

  public void setLocale(String locale) {
    LOG.error("locale not supported yet!"); // fixme
    this.locale = locale;
  }

}
