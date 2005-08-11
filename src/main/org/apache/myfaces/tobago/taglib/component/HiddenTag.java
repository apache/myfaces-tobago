/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * Created: Tue May 14 18:55:49 2002
 * $Id$
 */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIInput;

import javax.faces.component.UIComponent;

public class HiddenTag extends BeanTag
    implements org.apache.myfaces.tobago.taglib.decl.HiddenTag {

  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    ComponentUtil.setBooleanProperty(component, ATTR_INLINE, "true", null);
  }

  public String getComponentType() {
    return UIInput.COMPONENT_TYPE;
  }
}
