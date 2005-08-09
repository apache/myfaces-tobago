package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
public interface IsImmediateCommand {
  /**
   *
   * Flag indicating that, if this component is activated by the user,
   * notifications should be delivered to interested listeners and actions
   * immediately (that is, during Apply Request Values phase) rather than
   * waiting until Invoke Application phase.
   *
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean")
  public void setImmediate(String immediate);
}
