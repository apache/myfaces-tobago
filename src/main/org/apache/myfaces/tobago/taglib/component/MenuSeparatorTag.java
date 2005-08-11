/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package org.apache.myfaces.tobago.taglib.component;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.taglib.decl.HasBinding;
import org.apache.myfaces.tobago.taglib.decl.IsRendered;
import org.apache.myfaces.tobago.apt.annotation.BodyContent;
import org.apache.myfaces.tobago.apt.annotation.Tag;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;

/**
 * Renders a separator.
 */
@Tag(name="menuSeparator", bodyContent=BodyContent.EMPTY)
public class MenuSeparatorTag extends TobagoTag
    implements IsRendered, HasBinding {

  public static final String MENU_TYPE = "menuSeparator";


  protected void setProperties(UIComponent component) {
    super.setProperties(component);
    component.setRendererType(null);
   ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, MENU_TYPE, getIterationHelper());
  }

  public String getComponentType() {
    return UIOutput.COMPONENT_TYPE;
  }
}