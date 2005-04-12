package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface IsShowHeader {
  /**
   *  <![CDATA[
   *    Flag indicating the header should rendered.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Boolean.class)
  public void setShowHeader(String showHeader);
}
