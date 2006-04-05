package org.apache.myfaces.tobago.example.demo.overview;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * Created 02.06.2004 13:48:46.
 * $Id: SynchronizeNavigationPhaseListener.java 1226 2005-04-21 10:18:15 +0200 (Do, 21 Apr 2005) lofwyr $
 */

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SynchronizeNavigationPhaseListener implements PhaseListener {

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
