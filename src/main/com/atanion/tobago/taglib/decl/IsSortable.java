package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface IsSortable {
  /**
   *  <![CDATA[
   *  Flag indicating whether or not this column is sortable.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Boolean.class)
  public void setSortable(String sortable);
}
