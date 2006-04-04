/*
 * Copyright (c) 2006 Atanion GmbH, Germany
 * All rights reserved. Created 04.04.2006 16:32:54.
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.decl;

import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.UIComponentTagAttribute;

/**
 * $Id$
 */
@Deprecated
public interface HasDeprecatedHeight {
  /**
   *  The height for this component.
   */
  @TagAttribute @UIComponentTagAttribute()
  void setHeight(String height);
}
