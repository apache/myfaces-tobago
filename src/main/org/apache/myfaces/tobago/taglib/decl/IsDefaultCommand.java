package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 3:10:41 PM
 * User: bommel
 * $Id$
 */
public interface IsDefaultCommand {
  /**
   *
   */
  @TagAttribute @UIComponentTagAttribute(type="java.lang.Boolean", defaultValue="false")
  public void setDefaultCommand(String defaultCommand);
}
