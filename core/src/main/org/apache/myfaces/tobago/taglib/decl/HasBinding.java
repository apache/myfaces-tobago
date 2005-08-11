package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

import javax.servlet.jsp.JspException;

/**
 * $Id$
 */
public interface HasBinding {
  /**
   * The value binding expression linking this
   * component to a property in a backing bean.
   */
  @TagAttribute @UIComponentTagAttribute(type="javax.faces.component.UIComponent")
  public void setBinding(String binding) throws JspException;
}
