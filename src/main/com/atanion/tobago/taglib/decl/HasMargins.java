package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasMargins {
  /**
   *  <![CDATA[
   * Top margin between container component and layouted children.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setMarginTop(String margin);
  /**
   *  <![CDATA[
   * Right margin between container component and layouted children.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setMarginRight(String margin);
  /**
   *  <![CDATA[
   * Bottom margin between container component and layouted children.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setMarginBottom(String margin);
  /**
   *  <![CDATA[
   * Left margin between container component and layouted children.    
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setMarginLeft(String margin);
}
