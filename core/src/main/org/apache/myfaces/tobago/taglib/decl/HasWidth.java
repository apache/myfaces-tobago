package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasWidth {
  /**
   *  The width for this component.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setWidth(String width);
}
