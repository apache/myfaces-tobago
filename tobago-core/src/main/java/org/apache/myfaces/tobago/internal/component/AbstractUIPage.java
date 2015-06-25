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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.component.DeprecatedDimension;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxResponseRenderer;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.internal.webapp.TobagoMultipartFormdataRequest;
import org.apache.myfaces.tobago.layout.Box;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.ApplyRequestValuesCallback;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.apache.myfaces.tobago.util.ProcessValidationsCallback;
import org.apache.myfaces.tobago.util.TobagoCallback;
import org.apache.myfaces.tobago.util.UpdateModelValuesCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractUIPage extends AbstractUIForm
    implements OnComponentPopulated, LayoutContainer, DeprecatedDimension {

  private static final Logger LOG = LoggerFactory.getLogger(AbstractUIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";

  public static final String FORM_ACCEPT_CHARSET = "utf-8";

  private static final TobagoCallback APPLY_REQUEST_VALUES_CALLBACK = new ApplyRequestValuesCallback();
  private static final ContextCallback PROCESS_VALIDATION_CALLBACK = new ProcessValidationsCallback();
  private static final ContextCallback UPDATE_MODEL_VALUES_CALLBACK = new UpdateModelValuesCallback();

  private String formId;

  private String actionId;

  private Box actionPosition;

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public String getFormId(final FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext) + ComponentUtils.SUB_SEPARATOR + "form";
    }
    return formId;
  }

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {
    if (!AjaxUtils.isAjaxRequest(facesContext)) {
      super.encodeBegin(facesContext);
      final AbstractUILayoutBase layoutManager = (AbstractUILayoutBase) getLayoutManager();
      if (layoutManager != null) {
        layoutManager.encodeBegin(facesContext);
      }
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {
    if (AjaxUtils.isAjaxRequest(facesContext)) {
      new AjaxResponseRenderer().renderResponse(facesContext);
    } else {
      final AbstractUILayoutBase layoutManager = (AbstractUILayoutBase) getLayoutManager();
      if (layoutManager != null) {
        layoutManager.encodeChildren(facesContext);
      } else {
        super.encodeChildren(facesContext);
      }
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {
    if (!AjaxUtils.isAjaxRequest(facesContext)) {
      final AbstractUILayoutBase layoutManager = (AbstractUILayoutBase) getLayoutManager();
      if (layoutManager != null) {
        layoutManager.encodeEnd(facesContext);
      }
      super.encodeEnd(facesContext);
    }
    if (LOG.isTraceEnabled()) {
      LOG.trace(DebugUtils.toString(this.getParent(), 0));
    }
  }

  private void processDecodes0(final FacesContext facesContext) {

    checkTobagoRequest(facesContext);

    decode(facesContext);

    markSubmittedForm(facesContext);

    // invoke processDecodes() on children
    for (final Iterator kids = getFacetsAndChildren(); kids.hasNext();) {
      final UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(facesContext);
    }
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
      processDecodes0(context);
    }
  }

  private void decodeActionComponent(
      final FacesContext facesContext, final AbstractUIPage page, final Map<String,
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
  }

  public void markSubmittedForm(final FacesContext facesContext) {
    // find the form of the action command and set submitted to it and all
    // children

    // reset old submitted state
    setSubmitted(false);

    String currentActionId = getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + currentActionId + "'");
    }

    final UIViewRoot viewRoot = facesContext.getViewRoot();
    UIComponent command = viewRoot.findComponent(currentActionId);

    // TODO: remove this if block if proven this never happens anymore
    if (command == null
        && currentActionId != null && currentActionId.matches(".*:\\d+:.*")) {
      // If currentActionId component was inside a sheet the id contains the
      // rowIndex and is therefore not found here.
      // We do not need the row here because we want just to find the
      // related form, so removing the rowIndex will help here.
      currentActionId = currentActionId.replaceAll(":\\d+:", ":");
      try {
        command = viewRoot.findComponent(currentActionId);
        //LOG.info("command = \"" + command + "\"", new Exception());
      } catch (final Exception e) {
        // ignore
      }
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace(currentActionId);
      LOG.trace("command:{}", command);
      LOG.trace(DebugUtils.toString(viewRoot, 0));
    }

    if (command != null) {
      final AbstractUIForm form = ComponentUtils.findForm(command);
      form.setSubmitted(true);

      if (LOG.isTraceEnabled()) {
        LOG.trace("form:{}", form);
        LOG.trace(form.getClientId(facesContext));
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Illegal actionId! Render response...");
      }
      facesContext.renderResponse();
    }
  }

  private void checkTobagoRequest(final FacesContext facesContext) {
    // multipart/form-data must use TobagoMultipartFormdataRequest
    final String contentType = facesContext.getExternalContext().getRequestHeaderMap().get("content-type");
    if (contentType != null && contentType.startsWith("multipart/form-data")) {
      final Object request = facesContext.getExternalContext().getRequest();
      boolean okay = false;
      if (request instanceof TobagoMultipartFormdataRequest) {
        okay = true;
      } else if (request instanceof HttpServletRequestWrapper) {
        final ServletRequest wrappedRequest = ((HttpServletRequestWrapper) request).getRequest();
        if (wrappedRequest instanceof TobagoMultipartFormdataRequest) {
          okay = true;
        }
      }
      // TODO PortletRequest ??
      if (!okay) {
        LOG.error("Can't process multipart/form-data without TobagoRequest. "
            + "Please check the web.xml and define a TobagoMultipartFormdataFilter. "
            + "See documentation for <tc:file>");
        facesContext.addMessage(null, new FacesMessage("An error has occurred!"));
      }
    }
  }

  public String getActionId() {
    return actionId;
  }

  public void setActionId(final String actionId) {
    this.actionId = actionId;
  }

  public Box getActionPosition() {
    return actionPosition;
  }

  public void setActionPosition(final Box actionPosition) {
    this.actionPosition = actionPosition;
  }

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
/*
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.GRID_LAYOUT, RendererTypes.GRID_LAYOUT, parent));
    }
*/
  }

  public LayoutManager getLayoutManager() {
    final UIComponent facet = getFacet(Facets.LAYOUT);
    if (facet == null) {
      return null;
    } else if (facet instanceof LayoutManager) {
      return (LayoutManager) facet;
    } else {
      return (LayoutManager) ComponentUtils.findChild(facet, AbstractUILayoutBase.class);
    }
  }

  public void setLayoutManager(final LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
  }

  public boolean isLayoutChildren() {
    return isRendered();
  }

  public abstract Measure getWidth();

  public abstract Measure getHeight();
}
