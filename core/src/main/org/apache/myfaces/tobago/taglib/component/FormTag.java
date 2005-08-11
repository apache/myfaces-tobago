/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 18.02.2002, 19:23:17
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.HasId;
import org.apache.myfaces.tobago.apt.annotation.Tag;

@Tag(name="form")
public class FormTag extends TobagoBodyTag implements HasBinding, HasId {
// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UIForm.COMPONENT_TYPE;
  }
}

