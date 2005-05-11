package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface HasAction {
  /**
   *
   * Action to invoke when clicked.
   * Depends on 'type' attribute:
   * If type is NOT 'navigate', 'reset' or 'script' this must be a
   * MethodBinding representing the application action to invoke when
   * this component is activated by the user.
   * The expression must evaluate to a public method that takes no parameters,
   * and returns a String (the logical outcome) which is passed to the
   * NavigationHandler for this application.
   *
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setAction(String action);
}
