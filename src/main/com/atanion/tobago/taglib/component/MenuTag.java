/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIPanel;
import javax.faces.component.UIComponent;

public class MenuTag extends TobagoTag {
// ----------------------------------------------------------------- attributes

  private String label;
  private String image;
  private String labelWithAccessKey;
//  private String disabled;

// ----------------------------------------------------------- business methods
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
    component.setRendererType(null);
   ComponentUtil.setStringProperty(component, ATTR_MENU_TYPE, "menu", getIterationHelper());
  }

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }


  public void release() {
    super.release();
    label = null;
    labelWithAccessKey = null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }
}