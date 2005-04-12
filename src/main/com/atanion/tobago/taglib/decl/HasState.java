package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasState {
  /**
   *  <![CDATA[
   * Component state saving object.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Object.class)
  public void setState(String state);
}
