package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasFor {
  /**
   *  Id of the component, this is related to. 
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setFor(String _for);
}
