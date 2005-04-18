package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasRows {
  /**
   *  The row count for this component.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setRows(String rows);
}
