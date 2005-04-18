package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasScrollbars {
  /**
   *  <![CDATA[
   *  possible values are:
   *    'false' : no scrollbars should rendered
   *    'true'  : scrollbars should always rendered
   *    'auto'  : scrollbars should rendered when needed
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setScrollbars(String scrollbars);
}
