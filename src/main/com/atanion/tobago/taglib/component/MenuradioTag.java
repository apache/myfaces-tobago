/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.09.2004 at 12:03:45.
  * $Id$
  */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.el.ConstantMethodBinding;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;
import javax.faces.application.Application;

public class MenuradioTag extends MenuTag {
// ----------------------------------------------------------------- attributes

  private String action;
  private String actionListener;
  private String type;
  private String value;
  private String immediate;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  public void release() {
    super.release();
    action = null;
    type = null;
    value = null;

  }

  protected void setProperties(UIComponent component) {
    super.setProperties(component);

    component.setRendererType(RENDERER_TYPE_MENUITEM);

   ComponentUtil.setStringProperty(component, ATTR_TYPE, type, getIterationHelper());

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
     ComponentUtil.setStringProperty(component, ATTR_ACTION_STRING, action, getIterationHelper());
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


    if (actionListener != null) {
      if (isValueReference(actionListener)) {
        Class arguments[] = {javax.faces.event.ActionEvent.class};
        MethodBinding binding
            = FacesContext.getCurrentInstance().getApplication().createMethodBinding(actionListener, arguments);
        ((UICommand)component).setActionListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (actionListener): " + actionListener);
      }
    }

    ComponentUtil.setStringProperty(component, ATTR_VALUE, value, getIterationHelper());
    ComponentUtil.setStringProperty(component, ATTR_MENU_TYPE, "menuRadio", getIterationHelper());
    ComponentUtil.setBooleanProperty(component, ATTR_IMMEDIATE, immediate, getIterationHelper());
  }

// ------------------------------------------------------------ getter + setter

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setActionListener(String actionListener) {
    this.actionListener = actionListener;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void setImmediate(String immediate) {
    this.immediate = immediate;
  }
}