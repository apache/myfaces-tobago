/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 14.06.2004 19:22:26.
 * $Id$
 */
package com.atanion.tobago.event;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.context.ClientProperties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class ClientPropertiesPhaseListener implements PhaseListener {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(
      ClientPropertiesPhaseListener.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }

  public void beforePhase(PhaseEvent event) {
    ensureClientProperties(event);
  }

  public void afterPhase(PhaseEvent event) {
    ensureClientProperties(event);
  }

  private void ensureClientProperties(PhaseEvent event) {
    FacesContext facesContext = event.getFacesContext();
    UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot != null) {
      ClientProperties clientProperties = (ClientProperties)
          viewRoot.getAttributes().get(TobagoConstants.ATTR_CLIENT_PROPERTIES);
      if (clientProperties == null) {
        clientProperties = ClientProperties.getInstance(facesContext);
        viewRoot.getAttributes().put(
            TobagoConstants.ATTR_CLIENT_PROPERTIES, clientProperties);
      }
      if (LOG.isDebugEnabled()) {
        LOG.debug("clientProperties = '" + clientProperties + "'");
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
