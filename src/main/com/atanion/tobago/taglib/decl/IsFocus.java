package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:16:44 PM
 * User: bommel
 * $Id$
 */
public interface IsFocus {
  /**
   * Flag indicating this component should recieve the focus.
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean")
  public void setFocus(String focus);
}
