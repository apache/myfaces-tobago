package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasShowDirectLinks {
  /**
   *  <![CDATA[
   *   Flag indicating whether or not a range of direct paging links should be
   *   rendered in the sheet's footer.<br />
   *    Valid values are <strong>left</strong>, <strong>center</strong>,
   *    <strong>right</strong> and <strong>none</strong>.
   *    The <strong>default</strong> is <code>none</code>.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setShowDirectLinks(String showDirectLinks);
}
