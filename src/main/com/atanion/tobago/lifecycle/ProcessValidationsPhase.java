/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 14, 2002 at 4:44:01 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.Iterator;

public class ProcessValidationsPhase extends Phase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ProcessValidationsPhase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext facesContext) throws FacesException {

    UIViewRoot root = facesContext.getViewRoot();

    if (root != null) {
      root.processValidators(facesContext);
    }

    if (facesContext.getMessages().hasNext()) {
      facesContext.renderResponse();
    }

    if (LOG.isDebugEnabled()) {
      for (Iterator i = facesContext.getMessages(); i.hasNext(); ) {
        FacesMessage message = ((FacesMessage)i.next());
        LOG.debug("MESSAGE summary: " + message.getSummary());
        LOG.debug("MESSAGE detail:  " + message.getDetail());
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
