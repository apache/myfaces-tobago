package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasShowPageRange {
  /**
   *  <![CDATA[
   *   Flag indicating whether and where the range pages should
   *    rendered in the sheet's footer. Rendering this range also offers the
   *    capability to enter the index displayed page directly.<br />
   *    Valid values are <strong>left</strong>, <strong>center</strong>,
   *    <strong>right</strong> and <strong>none</strong>.
   *    The <strong>default</strong> is <code>none</code>.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setShowPageRange(String showPageRange);
}
