/*
 * Copyright (c) 2001 Atanion GmbH, Germany
 * All rights reserved.
 * $Id$
 */
package com.atanion.tobago.taglib.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.el.ConstantMethodBinding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.el.MethodBinding;
import javax.faces.context.FacesContext;

public abstract class CommandTag extends TobagoBodyTag {

// /////////////////////////////////////////// constants

  private static final Log LOG = LogFactory.getLog(CommandTag.class);

// /////////////////////////////////////////// attributes

  private String action;
  private String actionListener;

  private String commandName;
  private String type;

  private boolean defaultCommand;
  private boolean immediate;

// /////////////////////////////////////////// constructors

// /////////////////////////////////////////// code

  public String getComponentType() {
    return UICommand.COMPONENT_TYPE;
  }

  protected void setProperties(UIComponent component) {
    UICommand command = (UICommand) component;
    super.setProperties(component);

    setProperty(component, TobagoConstants.ATTR_COMMAND_NAME, commandName);
    setProperty(component, TobagoConstants.ATTR_TYPE, type);
    setProperty(component, TobagoConstants.ATTR_DEFAULT_COMMAND, defaultCommand);
    if (immediate) {
      command.setImmediate(true);
    }

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

// /////////////////////////////////////////// bean getter + setter

  public void setCommandName(String commandName) {
    this.commandName = commandName;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public void setActionListener(String actionListener) {
    this.actionListener = actionListener;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setDefaultCommand(boolean defaultCommand) {
    this.defaultCommand = defaultCommand;
  }

  public void setImmediate(boolean immediate) {
    this.immediate = immediate;
  }
}
