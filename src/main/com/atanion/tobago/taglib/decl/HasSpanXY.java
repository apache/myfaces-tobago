package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasSpanXY {
  /**
   *  <![CDATA[
   * 
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class)
  public void setSpanX(String span);
  /**
   *  <![CDATA[
   *
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class)
  public void setSpanY(String span);
}
