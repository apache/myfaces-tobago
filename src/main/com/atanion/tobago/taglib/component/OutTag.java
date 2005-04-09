/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasMarkup;
import com.atanion.tobago.taglib.decl.IsEscaped;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
/**
 * Renders a text
 */
@Tag(name="out")
public class OutTag extends BeanTag implements IsEscaped, HasMarkup {
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
