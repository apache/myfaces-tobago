/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class MenuSelectBooleanTag extends CommandTag {
  public static final String MENU_TYPE = "menuSelectBoolean";

// ----------------------------------------------------------------- attributes


  private String image;
  private String label;
  private String accessKey;
  private String labelWithAccessKey;
  private String value;

// ----------------------------------------------------------- business methods

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);
    
    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_MENU_TYPE, MENU_TYPE, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
  }

  public void release() {
    super.release();
    value = null;
    image = null;
    label = null;
    accessKey = null;
    labelWithAccessKey = null;
  }

// ------------------------------------------------------------ getter + setter


  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }
}