package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

/*
  * Copyright (c) 2004 Atanion GmbH, Germany
  * All rights reserved. Created 29.07.2003 at 15:09:53.
  * $Id$
  */

public class ToolBarTag extends PanelTag {

  public static final String LABEL_BOTTOM = "bottom";
  public static final String LABEL_RIGHT = "right";
  public static final String LABEL_OFF = "off";

  public static final String ICON_SMALL = "small";
  public static final String ICON_BIG = "big";
  public static final String ICON_OFF = "off";

  private String labelPosition = LABEL_BOTTOM;
  private String iconSize = ICON_SMALL;


  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    ComponentUtil.setStringProperty(
        component, ATTR_LABEL_POSITION, labelPosition, getIterationHelper());
    ComponentUtil.setStringProperty(
        component, ATTR_ICON_SIZE, iconSize, getIterationHelper());
  }

  public void release() {
    super.release();
    labelPosition = LABEL_BOTTOM;
    iconSize = ICON_SMALL;
  }

  public void setLabelPosition(String labelPosition) {
    this.labelPosition = labelPosition;
  }

  public void setIconSize(String iconSize) {
    this.iconSize = iconSize;
  }

}
