/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Tue May 14 18:55:49 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.TobagoConstants;

import javax.faces.component.UIInput;

public class HiddenTag extends BeanTag{

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}
