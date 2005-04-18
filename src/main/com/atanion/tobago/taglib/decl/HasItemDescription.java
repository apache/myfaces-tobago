package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import java.util.List;
import java.util.Map;

/**
 * $Id$
 */
public interface HasItemDescription {
  /**
   * Description of this option, for use in development tools.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setItemDescription(String itemDescription);
}
