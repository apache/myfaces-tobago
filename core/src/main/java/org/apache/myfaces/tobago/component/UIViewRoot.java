package org.apache.myfaces.tobago.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxResponseRenderer;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.apache.myfaces.tobago.util.ProcessValidationsCallback;
import org.apache.myfaces.tobago.util.TobagoCallback;
import org.apache.myfaces.tobago.util.UpdateModelValuesCallback;
import org.apache.myfaces.tobago.util.VariableResolverUtil;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
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

/*
 * User: weber
 * Date: Jun 13, 2005
 * Time: 5:19:31 PM
 */
public class UIViewRoot extends javax.faces.component.UIViewRoot implements InvokeOnComponent {

  private static final Log LOG = LogFactory.getLog(UIViewRoot.class);

  public static final int ANY_PHASE_ORDINAL = PhaseId.ANY_PHASE.getOrdinal();
  private static final TobagoCallback APPLY_REQUEST_VALUES_CALLBACK = new ApplyRequestValuesCallback();
  private static final ContextCallback PROCESS_VALIDATION_CALLBACK = new ProcessValidationsCallback();
  private static final ContextCallback UPDATE_MODEL_VALUES_CALLBACK = new UpdateModelValuesCallback();
  private List events = null;

  private int nextUniqueId;

  /**
   * <p>Create a new {@link UIViewRoot} instance with default property
   * values.</p>
   */
  public UIViewRoot() {
    super();
  }

  public void setLocale(Locale locale) {
    super.setLocale(locale);
    ClientProperties clientProperties = VariableResolverUtil.resolveClientProperties(getFacesContext());
    clientProperties.setLocale(locale);
  }

  public Object saveState(FacesContext facesContext) {
    Object[] state;
    if (FacesVersion.supports12()) {
      state = new Object[1];
    } else {
      state = new Object[2];
    }
    state[0] = super.saveState(facesContext);
    if (!FacesVersion.supports12()) {
      state[1] = nextUniqueId;
    }
    return state;
  }

  public void restoreState(FacesContext facesContext, Object o) {
    Object[] state = (Object[]) o;
    super.restoreState(facesContext, state[0]);
    if (!FacesVersion.supports12()) {
      nextUniqueId = (Integer) state[1];
    }
  }

  public void broadcastEventsForPhase(FacesContext context, PhaseId phaseId) {
    broadcastForPhase(phaseId);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
    }
  }

  public String createUniqueId() {
    if (FacesVersion.supports12()) {
      return super.createUniqueId();
    } else {
      ExternalContext extCtx = FacesContext.getCurrentInstance().getExternalContext();
      return extCtx.encodeNamespace(UNIQUE_ID_PREFIX + nextUniqueId++);
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

  public void queueEvent(FacesEvent event) {
    if (event == null) {
      throw new NullPointerException("event");
    }
    if (events == null) {
      events = new ArrayList();
    }
    events.add(event);
  }


  private void broadcastForPhase(PhaseId phaseId) {
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

          try {
            listiterator.remove();
          } catch (ConcurrentModificationException cme) {
            int eventIndex = listiterator.previousIndex();
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


  public void processDecodes(FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
     Map<String, UIComponent> ajaxComponents = AjaxUtils.parseAndStoreComponents(context);
    if (ajaxComponents != null) {
      // first decode the page
      AbstractUIPage page = ComponentUtils.findPage(context);
      page.decode(context);
      page.markSubmittedForm(context);
      if (context instanceof TobagoFacesContext) {
        ((TobagoFacesContext) context).setAjax(true);
      }
      // decode the action if actionComponent not inside one of the ajaxComponents
      // otherwise it is decoded there
      decodeActionComponent(context, page, ajaxComponents);

      // and all ajax components
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        if (context instanceof TobagoFacesContext) {
          ((TobagoFacesContext) context).setAjaxComponentId(entry.getKey());
        }
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

  private void decodeActionComponent(FacesContext facesContext, AbstractUIPage page, Map<String,
      UIComponent> ajaxComponents) {
    String actionId = page.getActionId();
    UIComponent actionComponent = null;
    if (actionId != null) {
      actionComponent = findComponent(actionId);
    }
    if (actionComponent == null) {
      return;
    }
    for (UIComponent ajaxComponent : ajaxComponents.values()) {
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


  public void processValidators(FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }

    Map<String, UIComponent> ajaxComponents = AjaxUtils.getAjaxComponents(context);
    if (ajaxComponents != null) {
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
        if (context instanceof TobagoFacesContext) {
          ((TobagoFacesContext) context).setAjaxComponentId(entry.getKey());
        }
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

  public void processUpdates(FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
     Map<String, UIComponent> ajaxComponents = AjaxUtils.getAjaxComponents(context);
    if (ajaxComponents != null) {
      for (Map.Entry<String, UIComponent> entry : ajaxComponents.entrySet()) {
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

  public void processApplication(FacesContext context) {
    if (context == null) {
      throw new NullPointerException("context");
    }
    broadcastForPhase(PhaseId.INVOKE_APPLICATION);
    if (context.getRenderResponse() || context.getResponseComplete()) {
      clearEvents();
    }
  }

  @Override
  public boolean getRendersChildren() {
    if (AjaxUtils.isAjaxRequest(FacesContext.getCurrentInstance())) {
      return true;
    } else {
      return super.getRendersChildren();
    }
  }

  @Override
  public void encodeChildren(FacesContext context) throws IOException {
    if (AjaxUtils.isAjaxRequest(context)) {
      new AjaxResponseRenderer().renderResponse(context);

    } else {
      super.encodeChildren(context);
    }
  }

  public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback)
      throws FacesException {
    return FacesUtils.invokeOnComponent(context, this, clientId, callback);
  }
}
