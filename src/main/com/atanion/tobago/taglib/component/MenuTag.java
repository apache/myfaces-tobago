/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.HasImage;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;

/**
 * <![CDATA[
 *       Container component to hold submenus and items.
 *  ]]>
 */
@Tag(name="menu", bodyContent="JSP=(t:menu|t:menuitem|t:menucheck|t:menuradio|t:menuseparator)*")
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