package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasTip {
  /**
   *  Text value to display as tooltip.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setTip(String tip);
}
