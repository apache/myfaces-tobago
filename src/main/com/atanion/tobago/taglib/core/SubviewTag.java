/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 08.04.2003 18:45:24.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.taglib.component.TobagoBodyTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;

public class SubviewTag extends TobagoBodyTag {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(SubviewTag.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UINamingContainer.COMPONENT_TYPE;
  }

//  public String getRendererType() { // todo: spec says "return null"
//    return null;
//  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.getAttributes().put(
        TobagoConstants.ATTR_LAYOUT_TRANSPARENT, Boolean.TRUE);
  }

  protected int getDoStartValue() throws JspException {
    return EVAL_BODY_BUFFERED;
  }

  protected int getDoEndValue() throws JspException {
    return EVAL_PAGE;
  }

// ///////////////////////////////////////////// bean getter + setter

}
