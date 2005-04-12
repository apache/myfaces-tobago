package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:11:43 PM
 * User: bommel
 * $Id$
 */
public interface HasMarkup {
  /**
   * Indicate markup of this component.
   * Possible values are 'none', 'strong' and 'deleted'
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class)
//  @TagAttribute @UIComponentTagAttribute(type=String.class, defaultValue="none")
  public void setMarkup(String markup);
}
