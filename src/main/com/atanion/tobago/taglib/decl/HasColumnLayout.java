package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasColumnLayout {
  /**
   *
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('&lt;x>*', '&lt;x>px' or '&lt;x>%').     
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setColumns(String columns);
}
