package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasImage {
  /**
   *
   *  Url to an image to display.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setImage(String image);
}
