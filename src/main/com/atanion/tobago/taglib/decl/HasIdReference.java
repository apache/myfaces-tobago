package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasIdReference {
  /**
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setIdReference(String id);
}
