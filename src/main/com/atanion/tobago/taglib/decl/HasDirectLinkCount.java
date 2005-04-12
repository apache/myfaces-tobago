package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasDirectLinkCount {
  /**
   *  <![CDATA[
   *   The count of rendered direct paging links in the sheet's footer.<br />
   *    The <strong>default</strong> is 9.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=Integer.class)
  public void setDirectLinkCount(String directLinkCount);
}
