/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
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
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTarget;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.util.annotation.Tag;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;

@Tag(name="link")
public class LinkTag extends CommandTag
    implements HasId, HasLabelAndAccessKey, IsDisabled, HasAction,
               HasActionListener, IsInline, HasCommandType, HasTarget, HasTip,
               IsImmediateCommand, HasImage, IsRendered ,HasBinding
    {

// /////////////////////////////////////////// constants
     private static final Log LOG = LogFactory.getLog(LinkTag.class);
// /////////////////////////////////////////// attributes

  private String target;
  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;
  private String tip;
  private String defaultCommand;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_TARGET, target, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip, getIterationHelper());
   ComponentUtil.setBooleanProperty(component, ATTR_DEFAULT_COMMAND, defaultCommand, getIterationHelper());
  }

  public void release() {
    super.release();
    target = null;
    label = null;
    image = null;
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
   }

// /////////////////////////////////////////// bean getter + setter

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getAccessKey() {
    return accessKey;
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

  public void setTip(String tip) {
    this.tip = tip;
  }

public void setDefaultCommand(String defaultCommand) {
    this.defaultCommand = defaultCommand;
  }
}
