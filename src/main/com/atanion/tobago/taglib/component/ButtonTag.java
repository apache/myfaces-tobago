/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIComponent;

public class ButtonTag extends CommandTag {
// ----------------------------------------------------------------- attributes

  private String label;
  private String image;
  private String accessKey;
  private String labelWithAccessKey;

// ----------------------------------------------------------- business methods

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    setStringProperty(component, ATTR_LABEL, label);
    setStringProperty(component, ATTR_IMAGE, image);
    setStringProperty(component, ATTR_ACCESS_KEY, accessKey);
    setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey);
  }

  public void release() {
    super.release();
    label = null;
    image = null;
    accessKey = null;
    labelWithAccessKey = null;
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
}

