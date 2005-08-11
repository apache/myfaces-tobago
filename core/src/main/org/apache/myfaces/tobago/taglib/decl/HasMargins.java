package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasMargins {
  /**
   *
   * Top margin between container component and layouted children.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setMarginTop(String margin);
  /**
   *
   * Right margin between container component and layouted children.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setMarginRight(String margin);
  /**
   *  
   * Bottom margin between container component and layouted children.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setMarginBottom(String margin);
  /**
   *
   * Left margin between container component and layouted children.    
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setMarginLeft(String margin);
}
