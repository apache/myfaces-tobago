/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasActionListener;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasImage;
import com.atanion.tobago.taglib.decl.HasLabelWithAccessKey;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;


@Tag(name="toolBarCommand")
public class ToolBarCommandTag extends CommandTag
    implements HasId, HasBinding, HasLabelWithAccessKey, HasImage,
               IsDisabled, HasAction, HasActionListener, HasCommandType,
               IsImmediateCommand, IsInline, IsRendered
    {
// ----------------------------------------------------------------- attributes

  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;
  private String tip;

// ----------------------------------------------------------- business methods

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_BUTTON);

    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip, getIterationHelper());
  }

  public void release() {
    super.release();
    label = null;
    image = null;
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
  }

// ------------------------------------------------------------ getter + setter

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
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

  public void setTip(String tip) {
    this.tip = tip;
  }
}

