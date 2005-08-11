package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasHeight {
  /**
   *  The height for this component.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setHeight(String height);
}
