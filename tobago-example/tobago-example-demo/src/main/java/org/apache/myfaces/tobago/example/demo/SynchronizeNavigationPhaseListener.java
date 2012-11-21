/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.example.demo;

import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

//todo @JsfPhaseListener
public class SynchronizeNavigationPhaseListener implements PhaseListener {

  public void beforePhase(PhaseEvent event) {
    if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
      synchronizeState();
    }
  }

  public void afterPhase(PhaseEvent event) {
    if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {
      synchronizeState();
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }

  private void synchronizeState() {
    // synchronizing current site with
    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIViewRoot viewRoot = facesContext.getViewRoot();
    // in case of direct links the ViewRoot is empty after "restore view".
    if (viewRoot != null && viewRoot.getChildCount() == 0) {
      String viewId = viewRoot.getViewId();
      NavigationTree navigation
          = (NavigationTree) VariableResolverUtils.resolveVariable(facesContext, "navigationTree");
      navigation.gotoNode(navigation.findByViewId(viewId));
    }
  }
}
