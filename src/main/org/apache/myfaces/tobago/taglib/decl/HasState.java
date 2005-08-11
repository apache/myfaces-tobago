package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasState {
  /**
   *
   * <strong>ValueBindingExpression</strong> pointing to a object to save the
   * component's state.
   *
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Object")
  public void setState(String state);
}
