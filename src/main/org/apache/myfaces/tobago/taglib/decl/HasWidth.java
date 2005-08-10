package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasWidth {
  /**
   *  The width for this component.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setWidth(String width);
}
