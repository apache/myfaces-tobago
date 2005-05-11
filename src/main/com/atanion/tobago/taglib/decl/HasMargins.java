package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

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
