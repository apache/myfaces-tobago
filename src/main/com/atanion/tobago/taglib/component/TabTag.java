/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created 19.08.2002 at 16:07:10.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.IsRendered;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 * renders a tab within a tabgroup.
 */
@Tag(name="tab")
public class TabTag extends TobagoBodyTag
    implements   HasIdBindingAndRendered, HasLabelAndAccessKey, HasTip
     {

// ----------------------------------------------------------- business methods


  private String label;
  private String accessKey;
  private String labelWithAccessKey;
  private String tip;


  public String getComponentType() {
    return UIPanel.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(component, ATTR_LABEL, label, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip,
        getIterationHelper());
  }

  public void release() {
    super.release();
    label = null;
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
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

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}

