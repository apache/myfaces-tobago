package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:48:07 PM
 * User: bommel
 * $Id$
 */
public interface IsInline {
  /**
   * Flag indicating this component should rendered as an inline element.
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean")
  public void setInline(String inline);
}
