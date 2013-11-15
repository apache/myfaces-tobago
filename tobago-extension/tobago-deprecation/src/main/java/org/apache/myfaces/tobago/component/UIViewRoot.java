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

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxResponseRenderer;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.apache.myfaces.tobago.util.ProcessValidationsCallback;
import org.apache.myfaces.tobago.util.TobagoCallback;
import org.apache.myfaces.tobago.util.UpdateModelValuesCallback;
import org.apache.myfaces.tobago.util.VariableResolverUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;

/**
 * @deprecated since 2.0.0
 */
public class UIViewRoot extends javax.faces.component.UIViewRoot implements InvokeOnComponent {

  private static final Logger LOG = LoggerFactory.getLogger(UIViewRoot.class);

  private static final TobagoCallback APPLY_REQUEST_VALUES_CALLBACK = new ApplyRequestValuesCallback();
  private static final ContextCallback PROCESS_VALIDATION_CALLBACK = new ProcessValidationsCallback();
  private static final ContextCallback UPDATE_MODEL_VALUES_CALLBACK = new UpdateModelValuesCallback();

  private List<FacesEvent> events;

// XXX check if TOBAGO-811 is still an issue
//  private int nextUniqueId;

  @Override
  public void setLocale(final Locale locale) {
    super.setLocale(locale);
    final ClientProperties clientProperties = VariableResolverUtils.resolveClientProperties(getFacesContext());
    clientProperties.setLocale(locale);
    clientProperties.updateUserAgent(getFacesContext());
  }

/*
// XXX check if TOBAGO-811 is still an issue

  @Override
  public Object saveState(FacesContext facesContext) {
    if (FacesVersion.supports12()) {
      return super.saveState(facesContext);
    } else {
      Object[] state = new Object[2];
      state[0] = super.saveState(facesContext);
      state[1] = nextUniqueId;
      return state;
    }
  }

  @Override
  public void restoreState(FacesContext facesContext, Object o) {
    if (FacesVersion.supports12()) {
      super.restoreState(facesContext, o);
    } else {
      Object[] state = (Object[]) o;
      super.restoreState(facesContext, state[0]);
      nextUniqueId = (Integer) state[1];
    }
  }

  @Override
  public String createUniqueId() {
    if (FacesVersion.supports12()) {
      return super.createUniqueId();
    } else {
      ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
      return extCtx.encodeNamespace(UNIQUE_ID_PREFIX + nextUniqueId++);
    }
  }
*/

  // XXX begin of JSF 2.0 like code

  public void broadcastEventsForPhase(final FacesContext context, final PhaseId phaseId) {
    broadcastForPhase(phaseId);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
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
  // TODO: remove if fixed in stable release! In 1.1_02 this seems to be fixed.

  public void queueEvent(final FacesEvent event) {
    if (event == null) {
      throw new NullPointerException("event");
    }
    if (events == null) {
      events = new ArrayList<FacesEvent>();
    }
    events.add(event);
  }


  private void broadcastForPhase(final PhaseId phaseId) {
    if (events == null) {
      return;
    }

    boolean abort = false;

    final int phaseIdOrdinal = phaseId.getOrdinal();
    for (final ListIterator<FacesEvent> listiterator = events.listIterator(); listiterator.hasNext();) {
      final FacesEvent event = listiterator.next();
      final int ordinal = event.getPhaseId().getOrdinal();
      if (ordinal == PhaseId.ANY_PHASE.getOrdinal() || ordinal == phaseIdOrdinal) {
        final UIComponent source = event.getComponent();
        try {
          source.broadcast(event);
        } catch (final FacesException e) {
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

          try {
            listiterator.remove();
          } catch (final ConcurrentModificationException cme) {
            final int eventIndex = listiterator.previousIndex();
            events.remove(eventIndex);
            //listiterator = events.listIterator();
          }
        }
      }
    }

    if (abort) {
      // TODO: abort processing of any event of any phase or just of any event of the current phase???
      clearEvents();
    }
  }


  private void clearEvents() {
    events = null;
  }


  @Override
  public void processDecodes(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.parseAndStoreComponents(context);
    if (ajaxComponents != null) {
      // first decode the page
      final AbstractUIPage page = ComponentUtils.findPage(context);
      page.decode(context);
      page.markSubmittedForm(context);
      FacesContextUtils.setAjax(context, true);

      // decode the action if actionComponent not inside one of the ajaxComponents
      // otherwise it is decoded there
      decodeActionComponent(context, page, ajaxComponents);

      // and all ajax components
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        FacesContextUtils.setAjaxComponentId(context, entry.getKey());
        invokeOnComponent(context, entry.getKey(), APPLY_REQUEST_VALUES_CALLBACK);
      }
    } else {
      super.processDecodes(context);
    }
    broadcastForPhase(PhaseId.APPLY_REQUEST_VALUES);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
    }
  }

  private void decodeActionComponent(final FacesContext facesContext, final AbstractUIPage page, final Map<String,
      UIComponent> ajaxComponents) {
    final String actionId = page.getActionId();
    UIComponent actionComponent = null;
    if (actionId != null) {
      actionComponent = findComponent(actionId);
      if (actionComponent == null && FacesVersion.supports20() && FacesVersion.isMyfaces()) {
        final String bugActionId = actionId.replaceAll(":\\d+:", ":");
        try {
          actionComponent = findComponent(bugActionId);
          //LOG.info("command = \"" + actionComponent + "\"", new Exception());
        } catch (final Exception e) {
          // ignore
        }
      }
    }
    if (actionComponent == null) {
      return;
    }
    for (final UIComponent ajaxComponent : ajaxComponents.values()) {
      UIComponent component = actionComponent;
      while (component != null) {
        if (component == ajaxComponent) {
          return;
        }
        component = component.getParent();
      }
    }
    invokeOnComponent(facesContext, actionId, APPLY_REQUEST_VALUES_CALLBACK);
  }


  @Override
  public void processValidators(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }

    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(context);
    if (ajaxComponents != null) {
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        FacesContextUtils.setAjaxComponentId(context, entry.getKey());
        invokeOnComponent(context, entry.getKey(), PROCESS_VALIDATION_CALLBACK);
      }
    } else {
      super.processValidators(context);
    }
    broadcastForPhase(PhaseId.PROCESS_VALIDATIONS);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
    }
  }

  @Override
  public void processUpdates(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    final Map<String, UIComponent> ajaxComponents = AjaxInternalUtils.getAjaxComponents(context);
    if (ajaxComponents != null) {
      for (final Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        invokeOnComponent(context, entry.getKey(), UPDATE_MODEL_VALUES_CALLBACK);
      }
    } else {
      super.processUpdates(context);
    }
    broadcastForPhase(PhaseId.UPDATE_MODEL_VALUES);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
    }
  }

  @Override
  public void processApplication(final FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    broadcastForPhase(PhaseId.INVOKE_APPLICATION);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
    }
  }

  // XXX end of JSF 2.0 like code

  @Override
  public boolean getRendersChildren() {
    if (AjaxUtils.isAjaxRequest(FacesContext.getCurrentInstance())) {
      return true;
    } else {
      return super.getRendersChildren();
    }
  }

  @Override
  public void encodeChildren(final FacesContext context) throws IOException {
    if (AjaxUtils.isAjaxRequest(context)) {
      new AjaxResponseRenderer().renderResponse(context);

    } else {
      super.encodeChildren(context);
    }
  }

  @Override
  public boolean invokeOnComponent(final FacesContext context, final String clientId, final ContextCallback callback)
      throws FacesException {
    return ComponentUtils.invokeOnComponent(context, this, clientId, callback);
  }
}
