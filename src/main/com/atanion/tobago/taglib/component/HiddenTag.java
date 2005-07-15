/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Tue May 14 18:55:49 2002
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.UIInput;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.taglib.decl.HasBinding;
import com.atanion.tobago.taglib.decl.HasId;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.util.annotation.Tag;
import com.atanion.util.annotation.TagAttribute;
import com.atanion.util.annotation.UIComponentTagAttribute;

import javax.faces.component.UIComponent;

/**
 * Renders a 'hidden' input element.
 */
@Tag(name="hidden")
public class HiddenTag extends BeanTag
    implements HasId, HasBinding, HasValue {

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setBooleanProperty(component, ATTR_INLINE, "true", null);
  }

  /**
   *  The component identifier for this component.
   *  This value must be unique within the closest
   *  parent component that is a naming container.
   */
//  @TagAttribute(required=true)
  @TagAttribute()
  @UIComponentTagAttribute()
  public void setId(String id) {
    super.setId(id);
  }

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}
