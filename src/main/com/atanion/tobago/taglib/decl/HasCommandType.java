package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasCommandType {
  /**
   *
   * Type of command component to create. Valid values are 'navigate', 'reset',
   * 'script' or 'submit'.
   * If not specified, or not a valid value,
   * the default value is 'submit' is used.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(defaultValue="submit")
  public void setType(String type);
}
