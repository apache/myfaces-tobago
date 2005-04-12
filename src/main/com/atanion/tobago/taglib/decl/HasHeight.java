package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasHeight {
  /**
   *  The height for this component.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setHeight(String height);
}
