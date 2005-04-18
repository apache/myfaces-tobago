package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasEnd {
  /**
   * <![CDATA[
   *  Index at which the iteration stops.
   *  Defaults to <code>items.length()</code>.
   * ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class, defaultValue="items.lenght()")
  public void setEnd(String end);
}
