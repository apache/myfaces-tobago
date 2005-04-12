package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasActionListener {
  /**
   *  <![CDATA[
   *  MethodBinding representing an action listener method that will be
   * notified when this component is activated by the user.
   * The expression must evaluate to a public method that takes an ActionEvent
   * parameter, with a return type of void.
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setActionListener(String actionListener);
}
