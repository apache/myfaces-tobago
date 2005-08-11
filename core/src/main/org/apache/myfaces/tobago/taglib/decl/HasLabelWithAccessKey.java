package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasLabelWithAccessKey {

  /**
   * Text value to display as label. Overwites 'label'.
   * If text contains an underscore the next character overwrites 'accesskey'.
   */
  @TagAttribute @UIComponentTagAttribute()
      public void setLabelWithAccessKey(String key);


  /**
   * Character used as accesskey. Overwritten by 'labelWithAccessKey'.
   */
//  @TagAttribute @UIComponentTagAttribute(type = String.class)
  @TagAttribute @UIComponentTagAttribute(type={"java.lang.String", "java.lang.Character"})
      public void setAccessKey(String key);
}
