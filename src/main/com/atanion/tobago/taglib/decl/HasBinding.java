package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;
import javax.servlet.jsp.JspException;

/**
 * $Id$
 */
public interface HasBinding {
  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute @UIComponentTagAttribute(type=UIComponent.class)
  public void setBinding(String binding) throws JspException;
}
