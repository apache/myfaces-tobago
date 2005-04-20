/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasConverter;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.util.annotation.BodyContentDescription;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
/**
 * Renders a text
 */
@Tag(name="out")
@BodyContentDescription(anyTagOf="f:converter|f:convertNumber|f:convertDateTime|...")
public class OutTag extends BeanTag
    implements HasIdBindingAndRendered, HasConverter, IsInline, HasTip
               // todo: remove interface HasValue, use annotations at setter
               , HasValue
     {
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
  /**
   *  The current value of this component.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setValue(String value) {
    super.setValue(value);
  }


  public String getEscape() {
    return escape;
  }


  /**
   * Flag indicating that characters that are
   * sensitive in HTML and XML markup must be escaped.
   * This flag is set to "true" by default.
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Boolean.class, defaultValue="true")
  public void setEscape(String escape) {
    this.escape = escape;
  }

  public String getMarkup() {
    return markup;
  }

  /**
   * Indicate markup of this component.
   * Possible values are 'none', 'strong' and 'deleted'
   */
  @TagAttribute
  @UIComponentTagAttribute(type=String.class, defaultValue="none")
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
