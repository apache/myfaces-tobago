/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Tue May 14 18:55:49 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.util.annotation.Tag;

@Tag(name="hidden")
public class HiddenTag extends BeanTag
    implements HasId, HasBinding, HasValue
    {

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}
