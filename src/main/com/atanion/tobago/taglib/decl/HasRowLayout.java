package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasRowLayout {
  /**
   *  <![CDATA[
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('<x>*', '<x>px', '<x>%' or 'fixed').
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setRows(String rows);
}
