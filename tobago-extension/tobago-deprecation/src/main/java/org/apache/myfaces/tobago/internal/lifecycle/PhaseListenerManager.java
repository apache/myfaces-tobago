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

package org.apache.myfaces.tobago.internal.lifecycle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import java.util.HashMap;
import java.util.Map;

/**
 * This class encapsulates the logic used to call PhaseListeners.  It was
 * needed because of issue 9 of the JSF 1.2 spec.  See section 11.3 for more
 * details.
 * <p/>
 * Not longer needed.
 *
 * @deprecated since Tobago 2.0.0
 */
@Deprecated
class PhaseListenerManager {

  private static final Logger LOG = LoggerFactory.getLogger(PhaseListenerManager.class);

  private Lifecycle lifecycle;
  private FacesContext facesContext;
  private PhaseListener[] phaseListeners;

  // Tracks success in the beforePhase.  Listeners that throw an exception
  // in beforePhase or were never called because a previous listener threw
  // an exception should not have its afterPhase called
  private Map<PhaseId, boolean[]> listenerSuccessMap = new HashMap<PhaseId, boolean[]>();

  /**
   * Creates a new instance of PhaseListenerManager
   */
  PhaseListenerManager(
      final Lifecycle lifecycle, final FacesContext facesContext, final PhaseListener[] phaseListeners) {
    this.lifecycle = lifecycle;
    this.facesContext = facesContext;
    this.phaseListeners = phaseListeners;
  }

  private boolean isListenerForThisPhase(final PhaseListener phaseListener, final PhaseId phaseId) {
    final int listenerPhaseId = phaseListener.getPhaseId().getOrdinal();
    return (listenerPhaseId == PhaseId.ANY_PHASE.getOrdinal()
        || listenerPhaseId == phaseId.getOrdinal());
  }

  void informPhaseListenersBefore(final PhaseId phaseId) {
    final boolean[] beforePhaseSuccess = new boolean[phaseListeners.length];
    listenerSuccessMap.put(phaseId, beforePhaseSuccess);

    if (phaseListeners.length == 0) {
      return;
    }
    final PhaseEvent event = new PhaseEvent(facesContext, phaseId, lifecycle);

    for (int i = 0; i < phaseListeners.length; i++) {
      final PhaseListener phaseListener = phaseListeners[i];
      if (isListenerForThisPhase(phaseListener, phaseId)) {
        try {
          phaseListener.beforePhase(event);
          beforePhaseSuccess[i] = true;
        } catch (final Exception e) {
          beforePhaseSuccess[i] = false; // redundant - for clarity
          LOG.error("Exception in PhaseListener " + phaseId.toString() + " beforePhase.", e);
          return;
        }
      }
    }
  }

  void informPhaseListenersAfter(final PhaseId phaseId) {
    final boolean[] beforePhaseSuccess = listenerSuccessMap.get(phaseId);

    if (phaseListeners.length == 0) {
      return;
    }
    final PhaseEvent event = new PhaseEvent(facesContext, phaseId, lifecycle);

    for (int i = phaseListeners.length - 1; i >= 0; i--) {
      final PhaseListener phaseListener = phaseListeners[i];
      if (isListenerForThisPhase(phaseListener, phaseId)
          && beforePhaseSuccess[i]) {
        try {
          phaseListener.afterPhase(event);
        } catch (final Exception e) {
          LOG.error("Exception in PhaseListener " + phaseId.toString() + " afterPhase", e);
        }
      }
    }

  }
}
