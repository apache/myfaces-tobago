package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasSelectable {
  /**
   * lag indicating whether or not this component should be render selectable items.
   *  Possible values are:
   *
   *  "multi" : a multisection tree is rendered
   *  "single" : a singlesection tree is rendered
   *
   *  For any other value or if this attribute is omited the items are not selectable.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class, defaultValue="off")
  public void setSelectable(String selectable);
}
