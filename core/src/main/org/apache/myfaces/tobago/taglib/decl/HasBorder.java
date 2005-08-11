package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasBorder {
  /**
   *
   *  Border size of this component.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setBorder(String border);
}
