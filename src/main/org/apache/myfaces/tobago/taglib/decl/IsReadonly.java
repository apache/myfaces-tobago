package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:48:45 PM
 * User: bommel
 * $Id$
 */
public interface IsReadonly {
  /**
   *  Flag indicating that this component will prohibit changes by the user. 
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean")
  public void setReadonly(String readonly);
}
