package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasPagingLength {
  /**
   *  <![CDATA[
   *   The number of rows to display, starting with the one identified by the
   *   "pageingStart" property.  
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class)
  public void setPagingLength(String pagingLength);
}
