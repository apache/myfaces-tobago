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

package org.apache.myfaces.tobago.lifecycle;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.webapp.Secret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public class SecretPhaseListener implements PhaseListener {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void afterPhase(final PhaseEvent event) {
    final FacesContext facesContext = event.getFacesContext();
    final TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    if (!facesContext.getResponseComplete()
        && facesContext.isPostback()
        && tobagoConfig.isCheckSessionSecret()
        && !check(facesContext)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Secret is invalid!");
      }
      facesContext.renderResponse(); // this ends the normal lifecycle
    }
  }

  /**
   * Checks that the request contains a parameter {@link org.apache.myfaces.tobago.webapp.Secret#KEY} which is equals to
   * a secret value in the session.
   */
  private boolean check(final FacesContext facesContext) {
    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    final String secretFromRequest = requestParameterMap.get(Secret.KEY);
    final Secret secret = Secret.getInstance(facesContext);
    return secret.check(secretFromRequest);
  }

  @Override
  public void beforePhase(final PhaseEvent event) {
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.RESTORE_VIEW;
  }
}
