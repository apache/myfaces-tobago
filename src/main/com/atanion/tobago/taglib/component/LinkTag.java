/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;

public class LinkTag extends CommandTag {

// /////////////////////////////////////////// constants

// /////////////////////////////////////////// attributes

  private String target;
  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
   ComponentUtil.setStringProperty(component, ATTR_TARGET, target, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_IMAGE, image, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
   ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
  }

  public void release() {
    super.release();
    target = null;
    label = null;
    image = null;
    accessKey = null;
    labelWithAccessKey = null;
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
}
