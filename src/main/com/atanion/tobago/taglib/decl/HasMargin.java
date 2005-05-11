package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasMargin {
  /**
   *
   * Margin between container component and layouted children.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setMargin(String margin);
}
