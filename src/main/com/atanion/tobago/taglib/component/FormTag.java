/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import javax.faces.component.UIForm;

public class FormTag extends TobagoBodyTag {
// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIForm.COMPONENT_TYPE;
  }
}

