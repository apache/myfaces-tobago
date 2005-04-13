package com.atanion.tobago.taglib.decl;

import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 9, 2005 2:30:20 PM
 * User: bommel
 * $Id$
 */
public interface HasPassword {
  /**
   * Is rendered as password, so you will not see the typed charakters.
   * @param password
   */
  @UIComponentTagAttribute(type = Boolean.class)
  @TagAttribute void setPassword(String password);
}
