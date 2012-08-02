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

package org.apache.myfaces.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.ResourceManagerImpl;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

public class UIViewRoot extends javax.faces.component.UIViewRoot {

  private static final Log LOG = LogFactory.getLog(UIViewRoot.class);

  private static final String EVENT_LIST_KEY = UIViewRoot.class.getName() + ".EventList";
  public static final int ANY_PHASE_ORDINAL = PhaseId.ANY_PHASE.getOrdinal();

  private ResourceManagerImpl.CacheKey rendererCacheKey;

  private ClientProperties clientProperties;

  /**
   * <p>Create a new {@link UIViewRoot} instance with default property
   * values.</p>
   */
  public UIViewRoot() {
    super();
    updateRendererCachePrefix();
  }

  public ClientProperties getClientProperties() {
    return clientProperties;
  }

  public void setClientProperties(final ClientProperties clientProperties) {
    this.clientProperties = clientProperties;
    updateRendererCachePrefix();
  }

  @Override
  public void setLocale(final Locale locale) {
    super.setLocale(locale);
    updateRendererCachePrefix();
  }

  public ResourceManagerImpl.CacheKey getRendererCacheKey() {
    return rendererCacheKey;
  }


  public void updateRendererCachePrefix() {
    rendererCacheKey = ResourceManagerImpl.getRendererCacheKey(
        clientProperties != null ? clientProperties.getId() : "null", getLocale());
//    LOG.info("updateRendererCachePrefix :" + rendererCachePrefix);
  }

  public void broadcastEventsForPhase(final FacesContext context, final PhaseId phaseId) {
    broadcastForPhase(phaseId);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents(context);
    }
  }

// -----------------------------------------------------------------------------
// -----------------------------------------------------------------------------
//
//  The following code is copied from myfaces implementation!
//  In suns jsf-api 1.1.01 are the events not cleared if renderResponse is true
//  after processUpdates, seems to be a bug. This is fixed at least in
//  Nightly Snapshot from 15.08.2005, but not in stable yet.
//  Events are private member of UIViewRoot, so we have to copy anny code
//  accessing them.
//
  // TODO: remove if fixed in stable release!

  @Override
  public void queueEvent(final FacesEvent event) {
    if (event == null) {
      throw new NullPointerException("event");
    }
    getEvents(FacesContext.getCurrentInstance(), true).add(event);
  }


  private void broadcastForPhase(final PhaseId phaseId) {
    List<FacesEvent> events = getEvents(FacesContext.getCurrentInstance(), false);
    if (events == null) {
      return;
    }

    boolean abort = false;

    int phaseIdOrdinal = phaseId.getOrdinal();
    for (ListIterator listiterator = events.listIterator(); listiterator.hasNext();) {
      FacesEvent event = (FacesEvent) listiterator.next();
      int ordinal = event.getPhaseId().getOrdinal();
      if (ordinal == ANY_PHASE_ORDINAL
          || ordinal == phaseIdOrdinal) {
        UIComponent source = event.getComponent();
        try {
          source.broadcast(event);
        } catch (FacesException e) {
          Throwable fe = e;
          while (fe != null) {
            if (fe instanceof AbortProcessingException) {
              if (LOG.isTraceEnabled()) {
                LOG.trace("AbortProcessingException caught!");
              }
              // abort event processing
              // Page 3-30 of JSF 1.1 spec: "Throw an AbortProcessingException, to tell the JSF implementation
              //  that no further broadcast of this event, or any further events, should take place."
              abort = true;
              break;
            }
            fe = fe.getCause();
          }
          if (!abort) {
            throw e;
          } else {
            break;
          }
        } finally {
          listiterator.remove();
        }
      }
    }

    if (abort) {
      // TODO: abort processing of any event of any phase or just of any event of the current phase???
      clearEvents(FacesContext.getCurrentInstance());
    }
  }


  private void clearEvents(final FacesContext context) {
    context.getExternalContext().getRequestMap().remove(EVENT_LIST_KEY);
  }


  @Override
  public void processDecodes(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    super.processDecodes(context);
    broadcastForPhase(PhaseId.APPLY_REQUEST_VALUES);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents(context);
    }
  }

  @Override
  public void processValidators(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    super.processValidators(context);
    broadcastForPhase(PhaseId.PROCESS_VALIDATIONS);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents(context);
    }
  }

  @Override
  public void processUpdates(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    super.processUpdates(context);
    broadcastForPhase(PhaseId.UPDATE_MODEL_VALUES);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents(context);
    }
  }

  @Override
  public void processApplication(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    broadcastForPhase(PhaseId.INVOKE_APPLICATION);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents(context);
    }
  }

  private List<FacesEvent> getEvents(final FacesContext context, boolean create) {
    final Map requestMap = context.getExternalContext().getRequestMap();
    List<FacesEvent> events = (List<FacesEvent>) requestMap.get(EVENT_LIST_KEY);
    if (events == null && create) {
      events = new ArrayList<FacesEvent>();
      requestMap.put(EVENT_LIST_KEY, events);
    }
    return events;
  }
}
