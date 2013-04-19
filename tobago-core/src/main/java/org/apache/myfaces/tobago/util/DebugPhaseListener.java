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

package org.apache.myfaces.tobago.util;

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.Map;

public class DebugPhaseListener implements PhaseListener {

  private static final Logger LOG = LoggerFactory.getLogger(DebugPhaseListener.class);

  private static final String KEY = DebugPhaseListener.class.getName() + "_ID_";

  public void afterPhase(PhaseEvent phaseEvent) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final boolean productionMode = TobagoConfig.getInstance(facesContext).getProjectStage() == ProjectStage.Production;
    if (productionMode) {
      return;
    }

    if (LOG.isInfoEnabled()) {
      final Long end = System.currentTimeMillis();
      final Map<String, Object> map = facesContext.getExternalContext().getRequestMap();
      map.put(KEY + phaseEvent.getPhaseId().getOrdinal() + "E", end);

      if (LOG.isTraceEnabled()) {
        LOG.trace("After Phase :" + phaseEvent.getPhaseId() + " Time=" + end);
      }

      if (LOG.isDebugEnabled()) {
        final Long start = (Long) map.get(KEY + phaseEvent.getPhaseId().getOrdinal() + "S");
        LOG.debug("Phase " + phaseEvent.getPhaseId() + " needs " + (end - start + " milliseconds"));
      }

      if (phaseEvent.getPhaseId().getOrdinal() == 6) {
        if (LOG.isTraceEnabled()) {
          final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
          LOG.trace(" response Locale            = '" + response.getLocale() + "'");
          LOG.trace(" response ContentType       = '" + response.getContentType() + "'");
          LOG.trace(" response CharacterEncoding = '{" + response.getCharacterEncoding() + "}'");
        }

        final Long start = (Long) map.get(KEY + "1S");
        if (start != null) {
          LOG.info("Total response time : " + (end - start + " milliseconds"));
        }
      }
      for (Iterator iterator = facesContext.getClientIdsWithMessages(); iterator.hasNext();) {
        final String clientId = (String) iterator.next();

        for (Iterator messageIterator = facesContext.getMessages(clientId); messageIterator.hasNext();) {
          final FacesMessage msg = (FacesMessage) messageIterator.next();
          LOG.info(MessageFormat.format("Faces message found."
              + "\n  Component: {0} \n  Severity : {1}"
              + "\n  Summary  : {2} \n  Detail   : {3}",
              clientId, msg.getSeverity(), msg.getSummary(), msg.getDetail()));
        }
      }
    }
  }

  public void beforePhase(PhaseEvent phaseEvent) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final boolean productionMode = TobagoConfig.getInstance(facesContext).getProjectStage() == ProjectStage.Production;
    if (productionMode) {
      LOG.warn("DebugPhaseListener disabled, because the project stage is 'production'.");
      return;
    }

    if (LOG.isInfoEnabled()) {
      final PhaseId phaseId = phaseEvent.getPhaseId();
      if (LOG.isDebugEnabled() || phaseId.getOrdinal() == 1) {

        final ExternalContext externalContext = facesContext.getExternalContext();

        if (LOG.isTraceEnabled() && PhaseId.RESTORE_VIEW == phaseId) {
          // this is before restoreView

          final Object request = externalContext.getRequest();
          if (request instanceof HttpServletRequest) {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            LOG.trace("RequestURI = " + servletRequest.getRequestURI());
          }
          final Map headerMap = externalContext.getRequestHeaderMap();
          for (Object key : headerMap.keySet()) {
            LOG.trace("Header : '" + key + "' = '" + headerMap.get(key) + "'");
          }
          final Map parameterMap = externalContext.getRequestParameterMap();
          for (Object key : parameterMap.keySet()) {
            LOG.trace("Param  : '" + key + "' = '" + parameterMap.get(key) + "'");
          }
        }

        final Long start = System.currentTimeMillis();
        final Map<String, Object> map = externalContext.getRequestMap();
        map.put(KEY + phaseId.getOrdinal() + "S", start);

        if (LOG.isDebugEnabled()) {
          Long end = null;
          int ordinal = phaseId.getOrdinal();
          while (end == null && ordinal > 0) {
            end = (Long) map.get(KEY + --ordinal + "E");
          }
          if (end != null) {
            LOG.debug("Time between phases " + ordinal + " and " + phaseId.getOrdinal() + ": "
                + (start - end) + " milliseconds");
          }
        }
        if (LOG.isTraceEnabled()) {
          LOG.trace("Before Phase :" + phaseId + " Time=" + start);
        }
      }
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
