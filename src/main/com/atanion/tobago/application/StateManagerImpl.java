/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.04.2004 18:47:43.
 * $Id$
 */
package com.atanion.tobago.application;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import java.io.IOException;

public class StateManagerImpl extends StateManager {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(StateManagerImpl.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public StateManager.SerializedView saveSerializedView(
      FacesContext facescontext) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  protected Object getTreeStructureToSave(FacesContext facescontext) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  protected Object getComponentStateToSave(FacesContext facescontext) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  public void writeState(
      FacesContext facescontext, StateManager.SerializedView serializedview)
      throws IOException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
  }

  public UIViewRoot restoreView(
      FacesContext facescontext, String s, String s1) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  protected UIViewRoot restoreTreeStructure(
      FacesContext facescontext, String s, String s1) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  protected void restoreComponentState(
      FacesContext facescontext, UIViewRoot uiviewroot, String s) {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
  }

// ///////////////////////////////////////////// bean getter + setter

}
