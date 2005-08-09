package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface IsRendered {
  /**
   *  Flag indicating whether or not this component should be rendered
   * (during Render Response Phase), or processed on any subsequent form submit.
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="true")
  public void setRendered(String rendered);
}
