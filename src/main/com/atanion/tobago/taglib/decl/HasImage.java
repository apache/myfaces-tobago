package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasImage {
  /**
   *
   *  Url to an image to display.
   *
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setImage(String image);
}
