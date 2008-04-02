package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;


public interface HasRenderedPartially {
  /**
   * Indicate the partially rendered Components in a case of a submit.
   */
   @TagAttribute
   @UIComponentTagAttribute(type = "java.lang.String[]")
   void setRenderedPartially(String componentIds);
}
