/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasIdBindingAndRendered;
import org.apache.myfaces.tobago.taglib.decl.HasImage;
import org.apache.myfaces.tobago.taglib.decl.HasLabelAndAccessKey;
import org.apache.myfaces.tobago.taglib.decl.IsDisabled;
import org.apache.myfaces.tobago.apt.annotation.BodyContentDescription;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;

/**
 *
 *       Container component to hold submenus and items.
 *
 */
@Tag(name="menu")
@BodyContentDescription(
    anyClassOf={"org.apache.myfaces.tobago.taglib.component.MenuTag",
    "org.apache.myfaces.tobago.taglib.component.MenuCommandTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSelectBooleanTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSelectOneTag",
    "org.apache.myfaces.tobago.taglib.component.MenuSeparatorTag" })

public class MenuTag extends TobagoTag
    implements HasIdBindingAndRendered, HasLabelAndAccessKey, IsDisabled, HasImage {
  public static final String MENU_TYPE = "menu";


// ----------------------------------------------------------------- attributes

  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;
//  private String disabled;

// ----------------------------------------------------------- business methods
  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(null);
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, "menu", getIterationHelper());
  }

  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }


  public void release() {
    super.release();
    label = null;
    accessKey = null;
    labelWithAccessKey = null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
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