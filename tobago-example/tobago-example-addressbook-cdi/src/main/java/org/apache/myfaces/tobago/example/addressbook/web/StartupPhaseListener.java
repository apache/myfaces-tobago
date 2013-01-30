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

package org.apache.myfaces.tobago.example.addressbook.web;

import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.io.IOException;

public class StartupPhaseListener implements PhaseListener {

  private static final Logger LOG = LoggerFactory.getLogger(StartupPhaseListener.class);
  public static final String LOGGED_IN = StartupPhaseListener.class.getName() + ".LOGGED_IN";
  public static final String PRINCIPAL = StartupPhaseListener.class.getName() + ".PRINCIPAL";

  public PhaseId getPhaseId() {
    return PhaseId.RESTORE_VIEW;
  }

  public void beforePhase(PhaseEvent event) {

    FacesContext facesContext = event.getFacesContext();
    ExternalContext externalContext = facesContext.getExternalContext();
    String pathInfo = externalContext.getRequestPathInfo();
    if (LOG.isDebugEnabled()) {
      LOG.debug("externalContext.getRequestPathInfo() = '" + pathInfo + "'");
    }

    if (pathInfo.equals("/error.xhtml") || // todo: not nice, find a declarative way.
        pathInfo.startsWith("/auth/")) {
      Object session = externalContext.getSession(false);
      if (session != null) {
        externalContext.getSessionMap().put(LOGGED_IN, Boolean.FALSE);
      }
      return; // nothing to do.
    }

    Boolean loggedIn = (Boolean) // todo: not nice to get this object directly from the session
        externalContext.getSessionMap().get(LOGGED_IN);

    if (!BooleanUtils.toBoolean(loggedIn)) {
      try {
        externalContext.getSessionMap().put(LOGGED_IN, Boolean.TRUE);
        String forward = externalContext.getRequestContextPath() + "/faces/addressbook/start.xhtml";
        externalContext.redirect(externalContext.encodeResourceURL(forward));
      } catch (Exception e) {
        LOG.error("", e);
        String forward = externalContext.getRequestContextPath() + "/error.xhtml";
        try {
          externalContext.redirect(externalContext.encodeResourceURL(forward));
        } catch (IOException e2) {
          LOG.error("", e2);
          throw new FacesException("Can't redirect to errorpage '" + forward + "'");
        }
      }
    }
  }

  public void afterPhase(PhaseEvent event) {
  }
}
