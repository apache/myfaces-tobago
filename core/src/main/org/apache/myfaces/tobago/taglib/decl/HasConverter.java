package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.DynamicExpression;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

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
  @TagAttribute @UIComponentTagAttribute(expression=DynamicExpression.NONE)
  public void setConverter(String converter);
}
