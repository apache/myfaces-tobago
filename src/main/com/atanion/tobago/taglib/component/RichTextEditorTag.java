/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class RichTextEditorTag extends InTag{

// ///////////////////////////////////////////// constant

// /////////////////////////////////////////// attributes

  private String statePreview;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

   ComponentUtil.setBooleanProperty(component, ATTR_STATE_PREVIEW, statePreview, getIterationHelper());
  }

  public void release() {
    super.release();
    statePreview = null;
  }

// /////////////////////////////////////////// bean getter + setter

  public String getStatePreview() {
    return statePreview;
  }

  public void setStatePreview(String statePreview) {
    this.statePreview = statePreview;
  }
}
