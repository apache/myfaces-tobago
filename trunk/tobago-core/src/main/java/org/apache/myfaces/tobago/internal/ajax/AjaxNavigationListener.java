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

package org.apache.myfaces.tobago.internal.ajax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 * XXX this must be refactored, because of TOBAGO-1524 (see TOBAGO-1563)
 * @deprecated
 */
@Deprecated
public class AjaxNavigationListener implements PhaseListener {

  private static final Logger LOG = LoggerFactory.getLogger(AjaxNavigationListener.class);

  @Override
  public void afterPhase(final PhaseEvent phaseEvent) {
    final FacesContext facesContext = phaseEvent.getFacesContext();
    debug(facesContext);
    if (!facesContext.getResponseComplete()) {
      if (phaseEvent.getPhaseId() == PhaseId.RESTORE_VIEW) {
        AjaxNavigationState.afterRestoreView(facesContext);
      } else if (phaseEvent.getPhaseId() == PhaseId.INVOKE_APPLICATION) {
        AjaxNavigationState.afterInvokeApplication(facesContext);
      }
    }
    debug(facesContext);
  }

  @Override
  public void beforePhase(final PhaseEvent phaseEvent) {
    final FacesContext facesContext = phaseEvent.getFacesContext();
    debug(facesContext);
    if (!facesContext.getResponseComplete() && phaseEvent.getPhaseId() == PhaseId.RESTORE_VIEW) {
      AjaxNavigationState.beforeRestoreView(facesContext);
    }
    debug(facesContext);
  }

  private void debug(FacesContext facesContext) {
    if (LOG.isTraceEnabled()) {
      LOG.trace("### debug getRenderResponse = {}", facesContext.getRenderResponse());
      if (facesContext.getViewRoot() != null) {
        LOG.trace("### debug getViewId = {}", facesContext.getViewRoot().getViewId());
      }
    }
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }

}
