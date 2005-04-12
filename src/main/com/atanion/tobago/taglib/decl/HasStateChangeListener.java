package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasStateChangeListener {
  /**
   *  <![CDATA[
   * MethodBinding representing an stateChangeListener method that will be
   * notified when the state was changed by the user.
   * The expression must evaluate to a public method that takes an
   * StateChangeEvent parameter, with a return type of void.    
   *    ]]>
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setStateChangeListener(String stateChangeListener);
}
