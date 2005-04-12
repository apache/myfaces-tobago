package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

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
  @TagAttribute @UIComponentTagAttribute(type=String.class)
  public void setLabel(String label);
}
