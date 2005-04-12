package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasShowRowRange {
  /**
   *  <![CDATA[
   *    Flag indicating whether or not the range of displayed rows should
   *   rendered in the sheet's footer. Rendering this range also offers the
   *    capability to enter the index of the start row directly. <br />
   *    Valid values are <strong>left</strong>, <strong>center</strong>,
   *    <strong>right</strong> and <strong>none</strong>.
   *    The <strong>default</strong> is <code>none</code>.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setShowRowRange(String showRowRange);
}
