package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasRenderRange {
  /**
   *  Range of items to render.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setRenderRange(String range);
}
