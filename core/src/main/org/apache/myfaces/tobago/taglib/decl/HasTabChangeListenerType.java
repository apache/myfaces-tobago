package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasTabChangeListenerType {
  /**
   * Fully qualified Java class name of a TabChangeListener to be
   *  created and registered.
   */
  @TagAttribute @UIComponentTagAttribute(type={"java.util.List", "java.util.Map", "java.lang.Object[]"})
  public void setType(String type);
}
