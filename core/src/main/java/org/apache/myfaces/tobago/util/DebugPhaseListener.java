package org.apache.myfaces.tobago.util;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.Date;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: weber
 * Date: Dec 6, 2005
 * Time: 7:50:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class DebugPhaseListener implements PhaseListener {
  private static final Log LOG = LogFactory.getLog(DebugPhaseListener.class);
  private static final String KEY = DebugPhaseListener.class.getName() + "_ID_";
  public void afterPhase(PhaseEvent phaseEvent) {
    if (LOG.isInfoEnabled()) {
      Date end = new Date();
      Map map = FacesContext.getCurrentInstance().getExternalContext()
          .getRequestMap();
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
    }

  }

  public void beforePhase(PhaseEvent phaseEvent) {
    if (LOG.isInfoEnabled()) {
      Date start = null;
      Map map = null;
      if (LOG.isDebugEnabled() || phaseEvent.getPhaseId().getOrdinal() == 1) {
        start = new Date();
        map = FacesContext.getCurrentInstance().getExternalContext()
            .getRequestMap();
        map.put(KEY + phaseEvent.getPhaseId().getOrdinal() + "S", start);
      }

      if (LOG.isDebugEnabled()) {
        Date end = null;
        int ordinal = phaseEvent.getPhaseId().getOrdinal();
        while (end == null && ordinal > 0) {
          end = (Date) map.get(KEY + --ordinal + "E");
        }
        if (end != null) {
          LOG.debug("Time between phases " + ordinal + " and " + phaseEvent.getPhaseId().getOrdinal() + ": "
              + (start.getTime() - end.getTime()) + " milliseconds");
        }
      }
      if (LOG.isTraceEnabled()) {
        LOG.trace("Before Phase :" + phaseEvent.getPhaseId()
            + " Time=" + start.getTime());
      }
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.ANY_PHASE;
  }
}
