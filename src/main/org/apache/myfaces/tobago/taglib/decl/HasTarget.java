package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasTarget {
  /**
   *
   * Name of a frame where the resource retrieved via this hyperlink is to be
   * displayed.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setTarget(String target);
}
