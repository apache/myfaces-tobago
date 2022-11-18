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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;

import java.lang.invoke.MethodHandles;

// XXX @Inject in PhaseListener doesn't work with Quarkus
public class ActivityPhaseListener implements PhaseListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Inject
  private ActivityList activityList;

  @Override
  public void beforePhase(final PhaseEvent event) {

    if (activityList == null) {
      LOG.warn("The activityList was not set by CDI");
      activityList = CDI.current().select(ActivityList.class).get();
    }

    final FacesContext facesContext = event.getFacesContext();
    final String sessionId = ((HttpSession) facesContext.getExternalContext().getSession(true)).getId();

    if (facesContext.getPartialViewContext().isAjaxRequest()) {
      LOG.debug("AJAX request for sessionId='{}'", sessionId);
      activityList.executeAjaxRequest(sessionId);
    } else {
      LOG.debug("Normal request for sessionId='{}'", sessionId);
      activityList.executeJsfRequest(sessionId);
    }
  }

  @Override
  public void afterPhase(final PhaseEvent event) {
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }
}
