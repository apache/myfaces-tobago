/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.el.ConstantMethodBinding;
import com.atanion.tobago.component.ComponentUtil;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;

public abstract class CommandTag extends TobagoBodyTag {
// ----------------------------------------------------------------- attributes

  private String action;
  private String actionListener;

  private String commandName;
  private String type;

  private String defaultCommand;
  private String immediate;

// ----------------------------------------------------------- business methods

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    UICommand command = (UICommand) component;
    super.setProperties(component);

   ComponentUtil.setStringProperty(component, ATTR_COMMAND_NAME, commandName);
   ComponentUtil.setStringProperty(component, ATTR_TYPE, type);
   ComponentUtil.setBooleanProperty(component, ATTR_DEFAULT_COMMAND, defaultCommand);
   ComponentUtil.setBooleanProperty(component, ATTR_IMMEDIATE, immediate);

    if (actionListener != null) {
      if (isValueReference(actionListener)) {
        Class arguments[] = {javax.faces.event.ActionEvent.class};
        MethodBinding binding
            = FacesContext.getCurrentInstance().getApplication().createMethodBinding(actionListener, arguments);
        command.setActionListener(binding);
      } else {
        throw new IllegalArgumentException(
            "Must be a valueReference (actionListener): " + actionListener);
      }
    }
    
    if (action != null) {
      if (isValueReference(action)) {
        MethodBinding binding = FacesContext.getCurrentInstance().getApplication().createMethodBinding(action,
            null);
        command.setAction(binding);
      } else {
        command.setAction(new ConstantMethodBinding(action));
      }
    }
  }

  public void release() {
    super.release();
    action= null;
    actionListener= null;
    commandName= null;
    type = null;
    defaultCommand = null;
    immediate = null;
  }

// ------------------------------------------------------------ getter + setter

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getActionListener() {
    return actionListener;
  }

  public void setActionListener(String actionListener) {
    this.actionListener = actionListener;
  }

  public String getCommandName() {
    return commandName;
  }

  public void setCommandName(String commandName) {
    this.commandName = commandName;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getDefaultCommand() {
    return defaultCommand;
  }

  public void setDefaultCommand(String defaultCommand) {
    this.defaultCommand = defaultCommand;
  }

  public String getImmediate() {
    return immediate;
  }

  public void setImmediate(String immediate) {
    this.immediate = immediate;
  }
}

