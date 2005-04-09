/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 19.02.2002, 19:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasFocus;
import com.atanion.tobago.taglib.decl.HasOnchangeListener;

import javax.faces.component.UIComponent;

public abstract class InputTag extends BeanTag implements HasOnchangeListener, HasFocus {
// ----------------------------------------------------------------- attributes

  private String onchange;
  private String focus;
  private String accessKey;
  private String labelWithAccessKey;

// ----------------------------------------------------------- business methods

  public void release() {
    super.release();
    this.onchange = null;
    this.focus = null;
    accessKey = null;
    labelWithAccessKey = null;

  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_ONCHANGE, onchange, getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_FOCUS, focus, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
    provideLabel(component);

  }

// ------------------------------------------------------------ getter + setter

  public String getOnchange() {
    return onchange;
  }

  public void setOnchange(String onchange) {
    this.onchange = onchange;
  }

  public String getFocus() {
    return focus;
  }
  public void setFocus(String focus) {
    this.focus = focus;
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

