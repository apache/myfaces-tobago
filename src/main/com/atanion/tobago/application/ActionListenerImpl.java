/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 17.06.2003 16:41:34.
 * Id: $
 */
package com.atanion.tobago.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.component.ActionSource;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;

/**
 * jsf7.1.1
 */
public class ActionListenerImpl implements ActionListener {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(ActionListenerImpl.class);

// ///////////////////////////////////////////// attribute

// //////////////////// ///////////////////////// constructor

// ///////////////////////////////////////////// code

  public PhaseId getPhaseId() {
    return PhaseId.INVOKE_APPLICATION;
  }

  public void processAction(ActionEvent actionEvent)
      throws AbortProcessingException {

    // todo: implement 7.1.1
    ActionSource source = (ActionSource) actionEvent.getComponent();
    FacesContext context = FacesContext.getCurrentInstance();
    Application application = context.getApplication();
    String outcome = null;
    String fromAction = null;
    MethodBinding binding = source.getAction();
    if (binding != null) {
      outcome = (String)binding.invoke(context, null);
      fromAction = binding.getExpressionString();
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("outcome    " + outcome);
      LOG.debug("binding    " + binding);
      LOG.debug("fromAction " + fromAction);
    }
    NavigationHandler navHandler = application.getNavigationHandler();
    navHandler.handleNavigation(context, fromAction, outcome);
  }

// ///////////////////////////////////////////// bean getter + setter

}
