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

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SecretPhaseListener implements PhaseListener {

  private static final Logger LOG = LoggerFactory.getLogger(SecretPhaseListener.class);

  @Override
  public void afterPhase(final PhaseEvent event) {
    final FacesContext facesContext = event.getFacesContext();
    if (!facesContext.getResponseComplete()
        && facesContext.isPostback()
        && TobagoConfig.getInstance(facesContext).isCheckSessionSecret()
        && !Secret.check(facesContext)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Secret is invalid!");
      }
      facesContext.renderResponse();
    }
  }

  @Override
  public void beforePhase(final PhaseEvent event) {
  }

  @Override
  public PhaseId getPhaseId() {
    return PhaseId.RESTORE_VIEW;
  }
}
