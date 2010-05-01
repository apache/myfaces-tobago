package org.apache.myfaces.tobago.util;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.application.FacesMessage;
import java.util.Date;
import java.util.Map;
import java.util.Iterator;
import java.text.MessageFormat;

/*
 * Date: Dec 6, 2005
 * Time: 7:50:25 PM
 */
public class DebugPhaseListener implements PhaseListener {
  private static final Logger LOG = LoggerFactory.getLogger(DebugPhaseListener.class);
  private static final String KEY = DebugPhaseListener.class.getName() + "_ID_";

  @SuppressWarnings("unchecked")
  public void afterPhase(PhaseEvent phaseEvent) {
    if (LOG.isInfoEnabled()) {
      Date end = new Date();
      FacesContext facesContext = phaseEvent.getFacesContext();
      Map map = facesContext.getExternalContext().getRequestMap();
      map.put(KEY + phaseEvent.getPhaseId().getOrdinal() + "E", end);

      if (LOG.isTraceEnabled()) {
        LOG.trace("After Phase :" + phaseEvent.getPhaseId()
            + " Time=" + end.getTime());
      }

      if (LOG.isDebugEnabled()) {
        Date start
            = (Date) map.get(KEY + phaseEvent.getPhaseId().getOrdinal() + "S");
        LOG.debug("Phase " + phaseEvent.getPhaseId() + " needs "
            + (end.getTime() - start.getTime() + " milliseconds"));
      }

      if (phaseEvent.getPhaseId().getOrdinal() == 6) {
        Date start = (Date) map.get(KEY + "1S");
        if (start != null) {
          LOG.info("Total response time : "
              + (end.getTime() - start.getTime() + " milliseconds"));
        }
      }
      for (Iterator iter = phaseEvent.getFacesContext().getClientIdsWithMessages(); iter.hasNext();) {
        String clientId = (String) iter.next();

        for (Iterator msgIter = facesContext.getMessages(clientId); msgIter.hasNext();) {
          FacesMessage msg = (FacesMessage) msgIter.next();
          LOG.info(MessageFormat.format("Faces message found."
              + "\n  Component: {0} \n  Severity : {1}"
              + "\n  Summary  : {2} \n  Detail   : {3}",
              new Object[]{clientId, msg.getSeverity(), msg.getSummary(), msg.getDetail()}));
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  public void beforePhase(PhaseEvent phaseEvent) {
    if (LOG.isInfoEnabled()) {
      Date start = null;
      Map map = null;
      PhaseId phaseId = phaseEvent.getPhaseId();
      if (LOG.isDebugEnabled() || phaseId.getOrdinal() == 1) {

        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();

        if (LOG.isTraceEnabled() && PhaseId.RESTORE_VIEW == phaseId) {
          // this is before restoreView
          Map params = externalContext.getRequestParameterMap();
          for (Object key : params.keySet()) {
            LOG.trace("Param : \"" + key + "\" = \"" + params.get(key) + "\"");
          }
        }

        start = new Date();
        map = externalContext.getRequestMap();
        map.put(KEY + phaseId.getOrdinal() + "S", start);
      }

      if (LOG.isDebugEnabled()) {
        Date end = null;
        int ordinal = phaseId.getOrdinal();
        while (end == null && ordinal > 0) {
          end = (Date) map.get(KEY + --ordinal + "E");
        }
        if (end != null) {
          LOG.debug("Time between phases " + ordinal + " and " + phaseId.getOrdinal() + ": "
              + (start.getTime() - end.getTime()) + " milliseconds");
        }
      }
      if (LOG.isTraceEnabled()) {
        LOG.trace("Before Phase :" + phaseId
            + " Time=" + start.getTime());
      }
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
