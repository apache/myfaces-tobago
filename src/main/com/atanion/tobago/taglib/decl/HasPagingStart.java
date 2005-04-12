package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasPagingStart {
  /**
   *  <![CDATA[
   *   Zero-relative row number of the first row to be displayed. 
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class)
  public void setPagingStart(String pagingStart);
}
