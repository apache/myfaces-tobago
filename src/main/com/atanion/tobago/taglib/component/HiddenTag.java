/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Tue May 14 18:55:49 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

/**
 * Renders a 'hidden' input element.
 */
@Tag(name="hidden", bodyContent="JSP=")
public class HiddenTag extends BeanTag
    implements HasId, HasBinding, HasValue {



  /**
   *  The component identifier for this component.
   *  This value must be unique within the closest
   *  parent component that is a naming container.
   */
  @TagAttribute(required=true)
  @UIComponentTagAttribute(type=String.class)
  public void setId(String id) {
    super.setId(id);
  }

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}
