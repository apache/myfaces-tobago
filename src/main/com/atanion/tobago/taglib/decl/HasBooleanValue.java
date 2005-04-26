package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:51:36 PM
 * User: bommel
 * $Id$
 */
public interface HasBooleanValue {
  
  /**
   *  The current value of this component.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute(type=Boolean.class) 
  public void setValue(String value);
}
