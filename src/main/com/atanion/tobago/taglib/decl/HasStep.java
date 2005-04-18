package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasStep {
  /**
   * <![CDATA[
   *  Index increments every iteration by this value.
   * ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class, defaultValue="1")
  public void setStep(String step);
}
