/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 06.12.2004 20:49:49.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class UILabeledInputLayout extends UILayout
    implements TobagoConstants {
  private static final Log LOG = LogFactory.getLog(UILabeledInputLayout.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.LabeledInputLayout";
  public static final String COMPONENT_FAMILY = "com.atanion.tobago.Layout";

  public void layoutBegin(FacesContext facesContext, UIComponent component) {
    // do nothing
  }

  
  public String getFamily() {
    return COMPONENT_FAMILY;
  }

}
