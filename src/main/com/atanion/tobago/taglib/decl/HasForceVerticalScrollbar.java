package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasForceVerticalScrollbar {
  /**
   *  <![CDATA[
   * Flag indicating whether or not this sheet should reserve space for
   *      vertical toolbar when calculating column width's.<br>
   *      Possible values are: <pre>
   *      'auto'  : sheet try to estimate the need of scrollbar,
   *                this is the default.
   *      'true'  : space for scroolbar is reserved.
   *      'false' : no space is reserved.
   *      </pre>    
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setForceVerticalScrollbar(String forceVerticalScrollbar);
}
