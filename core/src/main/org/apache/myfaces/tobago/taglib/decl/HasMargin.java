package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasMargin {
  /**
   *
   * Margin between container component and layouted children.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setMargin(String margin);
}
