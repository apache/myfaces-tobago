/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 19.01.2004 10:18:46.
 * $Id$
 */
package com.atanion.tobago.taglib.core;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.taglib.component.TobagoBodyTag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

// todo: must extend UIComponentBodyTag
public class VerbatimTag extends TobagoBodyTag {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(VerbatimTag.class);

// ///////////////////////////////////////////// attribute

  private boolean escape;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public String getRendererType() {
    return "Verbatim";
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setProperty(component, TobagoConstants.ATTR_ESCAPE, escape);
  }

    public void handleBodyContent() {
    if (bodyContent != null) {
      ((UIOutput)getComponentInstance()).setValue(bodyContent.getString());
    }
  }


// ///////////////////////////////////////////// bean getter + setter

  public void setEscape(boolean escape) {
    this.escape = escape;
  }
}
