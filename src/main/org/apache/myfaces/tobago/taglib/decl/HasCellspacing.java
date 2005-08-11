package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasCellspacing {
  /**
   *  
   * Spacing between component and layout cell's   
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setCellspacing(String cellspacing);
}
