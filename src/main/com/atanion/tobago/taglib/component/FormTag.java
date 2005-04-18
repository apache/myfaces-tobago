/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIForm;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.util.annotation.Tag;

@Tag(name="form")
public class FormTag extends TobagoBodyTag implements HasBinding, HasId{
// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIForm.COMPONENT_TYPE;
  }
}

