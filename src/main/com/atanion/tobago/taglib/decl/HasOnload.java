package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasOnload {
  /**
   * A script function which is invoked during onLoad Handler on client. 
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setOnload(String function);
}
