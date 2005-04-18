package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:10:41 PM
 * User: bommel
 * $Id$
 */
public interface IsEscaped {
  /**
   * Flag indicating that characters that are
   * sensitive in HTML and XML markup must be escaped.
   * This flag is set to "true" by default.
   */
//  @TagAttribute @UIComponentTagAttribute(type=Boolean.class)
  @TagAttribute @UIComponentTagAttribute(type=Boolean.class, defaultValue="true")
  public void setEscape(String escape);
}
