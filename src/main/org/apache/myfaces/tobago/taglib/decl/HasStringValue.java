package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:51:36 PM
 * User: bommel
 * $Id$
 */
public interface HasStringValue {
  
  /**
   *  The current value of this component.
   *
   */
  @TagAttribute
  @UIComponentTagAttribute()
  void setValue(String value);
}
