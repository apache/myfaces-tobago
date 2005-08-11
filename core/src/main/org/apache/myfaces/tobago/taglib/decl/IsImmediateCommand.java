package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
