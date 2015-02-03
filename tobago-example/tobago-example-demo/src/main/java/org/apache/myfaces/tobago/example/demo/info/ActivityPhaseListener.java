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

package org.apache.myfaces.tobago.example.demo.info;

import org.apache.deltaspike.jsf.api.listener.phase.JsfPhaseListener;
import org.apache.myfaces.tobago.ajax.AjaxUtils;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

@JsfPhaseListener
public class ActivityPhaseListener implements PhaseListener {

  @Inject
  private ActivityList activityList;

  public ActivityPhaseListener() {
  }

  public void beforePhase(final PhaseEvent event) {
    final FacesContext facesContext = event.getFacesContext();
    final String sessionId = ((HttpSession) facesContext.getExternalContext().getSession(true)).getId();

    if (AjaxUtils.isAjaxRequest(facesContext)) {
      activityList.executeAjaxRequest(sessionId);
    } else {
      activityList.executeJsfRequest(sessionId);
    }
  }

  public void afterPhase(final PhaseEvent event) {
  }

  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }
}
