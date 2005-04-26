package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasRowLayout {
  /**
   *
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('&lt;x>*', '&lt;x>px', '&lt;x>%' or 'fixed').
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setRows(String rows);
}
