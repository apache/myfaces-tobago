package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasIconSize {
  /**
   * Size of button images, possible values are: small, big, off.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class, defaultValue="small")
  public void setIconSize(String size);
}
