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

import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.util.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 *
 * Not longer needed.
 *
 * @deprecated since Tobago 2.0.0
 */
@Deprecated
public class TobagoLifecycle extends Lifecycle {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoLifecycle.class);

  public static final String VIEW_ROOT_KEY = TobagoLifecycle.class.getName() + ".VIEW_ROOT_KEY";
  public static final String FACES_MESSAGES_KEY = TobagoLifecycle.class.getName() + ".FACES_MESSAGES_KEY";

  private PhaseExecutor[] lifecycleExecutors;
  private PhaseExecutor renderExecutor;

  private final List<PhaseListener> phaseListenerList = new ArrayList<PhaseListener>();

  /**
   * Lazy cache for returning phaseListenerList as an Array.
   */
  private PhaseListener[] phaseListenerArray = null;

  public TobagoLifecycle() {
    // hide from public access
    lifecycleExecutors = new PhaseExecutor[]{
        new RestoreViewExecutor(),
        new ApplyRequestValuesExecutor(),
        new ProcessValidationsExecutor(),
        new UpdateModelValuesExecutor(),
        new InvokeApplicationExecutor()
    };

    renderExecutor = new RenderResponseExecutor();
  }

  public void execute(final FacesContext context) throws FacesException {

    final PhaseListenerManager phaseListenerMgr = new PhaseListenerManager(this, context, getPhaseListeners());

    // At very first ensure the requestEncoding, this MUST done before
    // accessing request parameters, which can occur in custom phaseListeners.
    RequestUtils.ensureEncoding(context);

    for (final PhaseExecutor executor : lifecycleExecutors) {
      if (executePhase(context, executor, phaseListenerMgr)) {
        return;
      }
    }
  }

  private boolean executePhase(
      final FacesContext facesContext, final PhaseExecutor executor, final PhaseListenerManager phaseListenerMgr)
      throws FacesException {

    boolean skipFurtherProcessing = false;
    if (LOG.isTraceEnabled()) {
      LOG.trace("entering " + executor.getPhase() + " in " + TobagoLifecycle.class.getName());
    }

    try {
      phaseListenerMgr.informPhaseListenersBefore(executor.getPhase());

      if (isResponseComplete(facesContext, executor.getPhase(), true)) {
        // have to return right away
        return true;
      }
      if (shouldRenderResponse(facesContext, executor.getPhase(), true)) {
        skipFurtherProcessing = true;
      }

      if (executor.execute(facesContext)) {
        return true;
      }
    } finally {
      phaseListenerMgr.informPhaseListenersAfter(executor.getPhase());
    }


    if (isResponseComplete(facesContext, executor.getPhase(), false)
        || shouldRenderResponse(facesContext, executor.getPhase(), false)) {
      // since this phase is completed we don't need to return right away even if the response is completed
      skipFurtherProcessing = true;
    }

    if (!skipFurtherProcessing && LOG.isTraceEnabled()) {
      LOG.trace("exiting " + executor.getPhase() + " in " + TobagoLifecycle.class.getName());
    }

    return skipFurtherProcessing;
  }

  public void render(final FacesContext facesContext) throws FacesException {
    // if the response is complete we should not be invoking the phase listeners
    if (isResponseComplete(facesContext, renderExecutor.getPhase(), true)) {
      return;
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace("entering " + renderExecutor.getPhase() + " in " + TobagoLifecycle.class.getName());
    }

    final PhaseListenerManager phaseListenerMgr = new PhaseListenerManager(this, facesContext, getPhaseListeners());

    try {
      phaseListenerMgr.informPhaseListenersBefore(renderExecutor.getPhase());
      // also possible that one of the listeners completed the response
      if (isResponseComplete(facesContext, renderExecutor.getPhase(), true)) {
        return;
      }
      renderExecutor.execute(facesContext);
    } finally {
      phaseListenerMgr.informPhaseListenersAfter(renderExecutor.getPhase());
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace(DebugUtils.toString(facesContext.getViewRoot(), 0));
      LOG.trace("exiting " + renderExecutor.getPhase() + " in " + TobagoLifecycle.class.getName());
    }

  }

  private boolean isResponseComplete(final FacesContext facesContext, final PhaseId phase, final boolean before) {
    boolean flag = false;
    if (facesContext.getResponseComplete()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("exiting from lifecycle.execute in " + phase
            + " because getResponseComplete is true from one of the "
            + (before ? "before" : "after") + " listeners");
      }
      flag = true;
    }
    return flag;
  }

  private boolean shouldRenderResponse(final FacesContext facesContext, final PhaseId phase, final boolean before) {
    boolean flag = false;
    if (facesContext.getRenderResponse()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("exiting from lifecycle.execute in " + phase
            + " because getRenderResponse is true from one of the "
            + (before ? "before" : "after") + " listeners");
      }
      flag = true;
    }
    return flag;
  }

  public void addPhaseListener(final PhaseListener phaseListener) {
    if (phaseListener == null) {
      throw new NullPointerException("PhaseListener must not be null.");
    }
    synchronized (phaseListenerList) {
      phaseListenerList.add(phaseListener);
      phaseListenerArray = null; // reset lazy cache array
    }
  }

  public void removePhaseListener(final PhaseListener phaseListener) {
    if (phaseListener == null) {
      throw new NullPointerException("PhaseListener must not be null.");
    }
    synchronized (phaseListenerList) {
      phaseListenerList.remove(phaseListener);
      phaseListenerArray = null; // reset lazy cache array
    }
  }

  public PhaseListener[] getPhaseListeners() {
    synchronized (phaseListenerList) {
      // (re)build lazy cache array if necessary
      if (phaseListenerArray == null) {
        phaseListenerArray = phaseListenerList.toArray(new PhaseListener[phaseListenerList.size()]);
      }
      return phaseListenerArray;
    }
  }
}
