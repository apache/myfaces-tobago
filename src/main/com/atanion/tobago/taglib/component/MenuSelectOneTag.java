/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UICommand;
import com.atanion.tobago.taglib.decl.HasAction;
import com.atanion.tobago.taglib.decl.HasCommandType;
import com.atanion.tobago.taglib.decl.HasIdBindingAndRendered;
import com.atanion.tobago.taglib.decl.HasLabel;
import com.atanion.tobago.taglib.decl.HasValue;
import com.atanion.tobago.taglib.decl.IsDisabled;
import com.atanion.tobago.taglib.decl.IsImmediateCommand;
import com.atanion.util.annotation.Tag;

import javax.faces.component.UIComponent;

/**
 * Renders a submenu with select one items.
 */
@Tag(name="menuradio")
public class MenuSelectOneTag extends CommandTag
    implements HasIdBindingAndRendered, HasLabel, IsDisabled, HasAction, HasCommandType, HasValue,
               IsImmediateCommand {

  public static final String COMMAND_TYPE = "commandSelectOne";


// ----------------------------------------------------------------- attributes

  private String value;

// ----------------------------------------------------------- business methods


  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    value = null;

  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUCOMMAND);

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_COMMAND_TYPE, COMMAND_TYPE, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter



  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}