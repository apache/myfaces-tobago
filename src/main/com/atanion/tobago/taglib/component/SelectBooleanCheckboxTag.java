/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created on: 15.02.2002, 17:01:56
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabelAndAccessKey;
import com.atanion.tobago.taglib.decl.HasTip;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsInline;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UISelectBoolean;

/**
 * Renders a checkbox.
 */
@Tag(name="selectBooleanCheckbox")
public class SelectBooleanCheckboxTag extends InputTag
    implements HasIdBindingAndRendered, HasLabelAndAccessKey, HasValue, IsDisabled,
               IsInline, HasTip
    {

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UISelectBoolean.COMPONENT_TYPE;
  }
}
