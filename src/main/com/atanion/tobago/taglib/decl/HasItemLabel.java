package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import java.util.List;
import java.util.Map;

/**
 * $Id$
 */
public interface HasItemLabel {
  /**
   * Label to be displayed to the user for this option.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setItemLabel(String itemLabel);
}
