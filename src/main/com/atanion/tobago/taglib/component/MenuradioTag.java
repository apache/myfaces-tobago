/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.el.ConstantMethodBinding;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;

public class MenuradioTag extends MenuTag {
// ----------------------------------------------------------------- attributes

  private String action;
  private String type;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    action = null;
    type = null;
  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    setStringProperty(component, ATTR_TYPE, type);

    String commandType;
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    if (type != null && isValueReference(type)) {
         commandType = (String)
             application.createValueBinding(type).getValue(facesContext);
    }
    else {
      commandType = type;
    }
    if (commandType != null && 
        (commandType.equals(COMMAND_TYPE_NAVIGATE)
        || commandType.equals(COMMAND_TYPE_RESET)
        || commandType.equals(COMMAND_TYPE_SCRIPT))) {
      setStringProperty(component, ATTR_ACTION_STRING, action);
    }
    else {
      if (action != null) {
        if (isValueReference(action)) {
          MethodBinding binding = application.createMethodBinding(action, null);
          ((UICommand)component).setAction(binding);
        } else {
          ((UICommand)component).setAction(new ConstantMethodBinding(action));
        }
      }
    }
    setStringProperty(component, ATTR_MENU_TYPE, "menuRadio");
  }

// ------------------------------------------------------------ getter + setter

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}