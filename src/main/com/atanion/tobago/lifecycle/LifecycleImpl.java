/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 12, 2002 at 3:47:53 PM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import com.atanion.util.SystemUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

public class LifecycleImpl extends Lifecycle {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(LifecycleImpl.class);

// ///////////////////////////////////////////// attribute


  private Phase[] phases = {
    new RestoreViewPhase(),
    new ApplyRequestValuesPhase(),
    new ProcessValidationsPhase(),
    new UpdateModelValuesPhase(),
    new InvokeApplicationPhase(),
    new RenderResponsePhase()
  };

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext facesContext) throws FacesException {

    Phase phase;
    for (int i = 0; i < phases.length - 1; i++) {
      phase = phases[i];

      phase.execute(facesContext);

      log(facesContext, phase);

      if (facesContext.getResponseComplete()) {
        return;
      }
      if (facesContext.getRenderResponse()) {
        break;
      }
    }
  }

  public void render(FacesContext facesContext) throws FacesException {
    Phase phase = phases[phases.length - 1];
    phase.execute(facesContext);
    log(facesContext, phase);
    if (LOG.isInfoEnabled()) {
      Runtime runtime = Runtime.getRuntime();
      LOG.info(
          "FREE: " + runtime.freeMemory() + " TOTAL: " +
          runtime.totalMemory() +
          " MAX: " +
          runtime.maxMemory());
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("*********************** facesContext.getViewRoot()");
      LOG.debug(facesContext.getViewRoot());
    }
  }


  public void removePhaseListener(PhaseListener phaselistener) {
    LOG.error("Not implemented yet"); // fixme jsfbeta
  }

  public PhaseListener[] getPhaseListeners() {
    LOG.error("Not implemented yet"); // fixme jsfbeta
    return new PhaseListener[0];
  }

  public void addPhaseListener(PhaseListener phaselistener) {
    LOG.error("Not implemented yet"); // fixme jsfbeta
  }

  private void log(FacesContext facesContext, Phase phase) {
    if (LOG.isInfoEnabled()) {
      LOG.info("result: " +
          (facesContext.getResponseComplete() ? "EXIT" :
          facesContext.getRenderResponse() ? "RENDER" : "NEXT")
          + " in " + SystemUtils.getPlainClassName(phase.getClass()));
    }
  }


// ///////////////////////////////////////////// bean getter + setter

}
