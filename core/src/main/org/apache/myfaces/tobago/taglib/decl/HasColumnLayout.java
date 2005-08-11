package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasColumnLayout {
  /**
   *
   * LayoutConstraints for column layout.
   * Semicolon separated list of layout tokens ('*', '&lt;x>*', '&lt;x>px' or '&lt;x>%').
   * Where '*' is equvalent to '1*'.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setColumns(String columns);
}
