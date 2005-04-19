/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:49:33.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasActionListener;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasImage;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 * Renders a menu item.
 */
@Tag(name="menuItem", bodyContent="JSP=")
public class MenuCommandTag extends CommandTag
    implements HasIdBindingAndRendered, HasLabelAndAccessKey, IsDisabled,  HasAction,
               HasActionListener, HasCommandType, HasImage, IsImmediateCommand {

  public static final String COMMAND_TYPE = "menuCommand";

// ----------------------------------------------------------------- attributes

  private String image;
  private String label;
  private String accessKey;
  private String labelWithAccessKey;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    image = null;
    label = null;
    accessKey = null;
    labelWithAccessKey = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);

    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, COMMAND_TYPE, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getImage() {
    return image;
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

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }
}