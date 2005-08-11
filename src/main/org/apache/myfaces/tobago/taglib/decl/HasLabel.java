package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:33:53 PM
 * User: bommel
 * $Id$
 */
public interface HasLabel {
  /**
   *   Text value to display as label. Overwritten by 'labelWithAccessKey'
   */
  @TagAttribute @UIComponentTagAttribute()
  public void setLabel(String label);
}
