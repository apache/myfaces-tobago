package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import java.util.List;
import java.util.Map;

/**
 * $Id$
 */
public interface HasStateChangeListenerType {
  /**
   * Fully qualified Java class name of a StateChangeListener to be
   *  created and registered.
   */
  @TagAttribute @UIComponentTagAttribute(type={List.class, Map.class, Object[].class})
  public void setType(String type);
}
