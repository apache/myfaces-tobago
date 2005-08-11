/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

public class OutTag extends BeanTag implements
    org.apache.myfaces.tobago.taglib.decl.OutTag {
// ----------------------------------------------------------------- attributes

  private String escape = "true";
  private String markup;
  private String tip;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    escape = "true";
    markup = null;
    tip = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setBooleanProperty(component, ATTR_ESCAPE, escape, getIterationHelper());
    // TODO ???? SPAN ?
    ComponentUtil.setBooleanProperty(component, ATTR_CREATE_SPAN, "true", getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_MARKUP, markup, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_TIP, tip, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter


  public String getEscape() {
    return escape;
  }


  public void setEscape(String escape) {
    this.escape = escape;
  }

  public String getMarkup() {
    return markup;
  }

  public void setMarkup(String markup) {
    this.markup = markup;
  }

  public String getTip() {
    return tip;
  }

  public void setTip(String tip) {
    this.tip = tip;
  }
}
