package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasTabChangeListenerType {
  /**
   * Fully qualified Java class name of a TabChangeListener to be
   *  created and registered.
   */
  @TagAttribute @UIComponentTagAttribute(type={"java.util.List", "java.util.Map", "java.lang.Object[]"})
  public void setType(String type);
}
