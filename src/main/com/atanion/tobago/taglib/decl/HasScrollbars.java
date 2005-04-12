package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasScrollbars {
  /**
   *  <![CDATA[
   *     
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setScrollbars(String scrollbars);
}
