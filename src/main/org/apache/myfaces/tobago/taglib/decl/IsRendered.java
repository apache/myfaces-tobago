package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
