/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 29.03.2004 at 15:41:39.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class ItemsTag extends TobagoTag {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String reference;

  private String renderRange;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_REFERENCE, reference, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_RENDER_RANGE, renderRange, getIterationHelper());
  }

  public void release() {
    super.release();
    reference = null;
    renderRange = null;
  }

// ///////////////////////////////////////////// bean getter + setter
  public String getReference() {
    return reference;
  }

  public void setReference(String reference) {
    this.reference = reference;
  }

  public String getRenderRange() {
    return renderRange;
  }

  public void setRenderRange(String renderRange) {
    this.renderRange = renderRange;
  }
}