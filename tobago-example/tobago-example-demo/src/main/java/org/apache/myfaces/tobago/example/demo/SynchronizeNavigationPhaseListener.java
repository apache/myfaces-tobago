package org.apache.myfaces.tobago.example.demo;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.component.UIViewRoot;
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
    UIViewRoot viewRoot = facesContext.getViewRoot();
    if (viewRoot.getChildCount() == 0) { // in case of direct links the ViewRoot is empty after "restore view".
      String viewId = viewRoot.getViewId();
      Navigation navigation = (Navigation) VariableResolverUtils.resolveVariable(facesContext, "navigation");
      navigation.updateMarker(viewId);
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.RESTORE_VIEW;
  }
}
