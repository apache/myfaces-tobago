package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasId {
  /**
   *  The component identifier for this component.
   *  This value must be unique within the closest
   *  parent component that is a naming container.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setId(String id);
}
