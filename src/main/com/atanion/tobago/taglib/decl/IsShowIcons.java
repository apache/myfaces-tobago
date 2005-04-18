package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:10:41 PM
 * User: bommel
 * $Id$
 */
public interface IsShowIcons {
  /**
   *  
   */
  @TagAttribute @UIComponentTagAttribute(type=Boolean.class, defaultValue="false")
  public void setShowIcons(String showIcons);
}
