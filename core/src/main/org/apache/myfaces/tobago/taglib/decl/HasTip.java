package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasTip {
  /**
   *  Text value to display as tooltip.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setTip(String tip);
}
