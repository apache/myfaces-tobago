package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasAlign {
  /**
   *  <![CDATA[
   *  Alignment of this column. 
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setAlign(String Align);
}
