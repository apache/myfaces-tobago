package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasBegin {
  /**
   * <![CDATA[
   *  Index at which the iteration begins.
   * ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class, defaultValue="0")
  public void setBegin(String begin);
}
