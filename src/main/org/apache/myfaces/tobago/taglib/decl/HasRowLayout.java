package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
  @TagAttribute @UIComponentTagAttribute()
  public void setRows(String rows);
}
