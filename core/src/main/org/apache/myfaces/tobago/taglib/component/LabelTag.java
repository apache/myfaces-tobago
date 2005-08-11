/*
 * Copyright (c) 2002 Atanion GmbH, Germany. All rights reserved.
 * Created: 23.07.2002 19:33:37
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class LabelTag extends BeanTag implements
    org.apache.myfaces.tobago.taglib.decl.LabelTag {

  // TODO ?? _for ?
  private String _for;
  private String labelWithAccessKey;
  private String accessKey;
  private String tip;


  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public String getFor() {
    return _for;
  }

  public void release() {
    super.release();
    accessKey = null;
    labelWithAccessKey = null;
    tip = null;
    _for = null;
  }

  public void setFor(String _for) {
    this._for = _for;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setStringProperty(
        component, ATTR_FOR, _for, getIterationHelper());
    ComponentUtil.setStringProperty(
        component, ATTR_ACCESS_KEY, accessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component,
        ATTR_LABEL_WITH_ACCESS_KEY, labelWithAccessKey, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip,
        getIterationHelper());
  }

  public String getLabelWithAccessKey() {
    return labelWithAccessKey;
  }

  public void setLabelWithAccessKey(String labelWithAccessKey) {
    this.labelWithAccessKey = labelWithAccessKey;
  }

  public String getAccessKey() {
    return accessKey;
  }

  public void setAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}

