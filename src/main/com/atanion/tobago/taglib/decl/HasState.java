package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

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
  @TagAttribute @UIComponentTagAttribute(type=Object.class)
  public void setState(String state);
}
