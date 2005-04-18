package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasBasename {
  /**
   *  Base name of the resource bundle to be loaded.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setBasename(String name);
}
