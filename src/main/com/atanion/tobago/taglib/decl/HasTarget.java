package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasTarget {
  /**
   *  <![CDATA[
   * Name of a frame where the resource retrieved via this hyperlink is to be
   * displayed.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setTarget(String target);
}
