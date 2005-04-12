package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:51:04 PM
 * User: bommel
 * $Id$
 */
public interface HasConverter {
  /**
   *  ConverterId of a registered converter.
   */
  @TagAttribute @UIComponentTagAttribute(type=String.class, expression=false)
  public void setConverter(String converter);
}
