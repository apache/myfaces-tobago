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

import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

//todo @JsfPhaseListener
public class SynchronizeNavigationPhaseListener implements PhaseListener {

  @Override
  public void beforePhase(final PhaseEvent event) {
    if (PhaseId.RENDER_RESPONSE.equals(event.getPhaseId())) {
      synchronizeState();
    }
  }

  @Override
  public void afterPhase(final PhaseEvent event) {
    if (PhaseId.RESTORE_VIEW.equals(event.getPhaseId())) {
      synchronizeState();
    }
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }

  private void synchronizeState() {
    // synchronizing current site with
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIViewRoot viewRoot = facesContext.getViewRoot();
    // in case of direct links the ViewRoot is empty after "restore view".
    if (viewRoot != null && ComponentUtils.findChild(viewRoot, AbstractUIPage.class) == null) {
      final String viewId = viewRoot.getViewId();
      if (viewId != null && viewId.startsWith("/content")) {
        final NavigationTree navigation
            = (NavigationTree) VariableResolverUtils.resolveVariable(facesContext, "navigationTree");
        navigation.gotoNode(navigation.findByViewId(viewId));
      }
    }
  }
}
