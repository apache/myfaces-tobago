package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasCellspacing {
  /**
   *  
   * Spacing between component and layout cell's   
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setCellspacing(String cellspacing);
}
