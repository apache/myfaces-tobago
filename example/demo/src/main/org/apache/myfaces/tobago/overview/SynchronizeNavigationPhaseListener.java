/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 02.06.2004 13:48:46.
 * $Id: SynchronizeNavigationPhaseListener.java 1226 2005-04-21 10:18:15 +0200 (Do, 21 Apr 2005) lofwyr $
 */
package org.apache.myfaces.tobago.overview;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SynchronizeNavigationPhaseListener implements PhaseListener {

// ----------------------------------------------------------------- interfaces

// ---------------------------- interface PhaseListener

  public void beforePhase(PhaseEvent event) {
  }

  public void afterPhase(PhaseEvent event) {
    // synchronizing direct links with controller
    FacesContext facesContext = FacesContext.getCurrentInstance();
    String viewId = facesContext.getViewRoot().getViewId();
    String navigationBeanName;
    if (viewId.indexOf("overview") > -1) {
      navigationBeanName = "overviewNavigation";
    } else if (viewId.indexOf("mini-howto") > -1) {
      navigationBeanName = "miniHowtoNavigation";
    } else {
      // other pages
      return;
    }
    PresentationController navigation = PresentationController
        .getCurrentInstance(facesContext, navigationBeanName);
    navigation.navigate(viewId);
  }

  public PhaseId getPhaseId() {
    return PhaseId.RESTORE_VIEW;
  }
}
