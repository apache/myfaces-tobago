/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created Dec 3, 2002 at 6:32:52 PM.
  * $Id$
  */
package com.atanion.tobago.event;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

public class TobagoActionEvent extends ActionEvent {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TobagoActionEvent.class);

// ///////////////////////////////////////////// attribute

  protected Map parameter;
  String commandName;

// ///////////////////////////////////////////// constructor

  public TobagoActionEvent(UIComponent source, String commandName) {
    super(source);
    this.commandName = commandName;
  }

  /** @deprecated */
  public String getActionCommand() {
    LOG.warn("fixme: jsfbeta"); // fixme: jsfbeta
    return commandName;
  }

  public String getParameter(String key) {
    if (parameter != null) {
      return (String) parameter.get(key);
    } else {
      return null;
    }
  }

  public void setParameter(String key, String value) {
    if (parameter == null) {
      parameter = new HashMap();
    }
    parameter.put(key, value);
  }

// ///////////////////////////////////////////// code



// ///////////////////////////////////////////// bean getter + setter
}

