package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasId {
  /**
   *  The component identifier for this component.
   *  This value must be unique within the closest
   *  parent component that is a naming container.
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setId(String id);
}
