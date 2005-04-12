package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasFor {
  /**
   *  Id of the component, this is related to. 
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setFor(String _for);
}
