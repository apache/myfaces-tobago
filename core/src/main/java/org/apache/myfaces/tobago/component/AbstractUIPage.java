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

import org.apache.commons.collections.KeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.layout.Box;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.model.PageState;
import org.apache.myfaces.tobago.model.PageStateImpl;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataRequest;

import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public abstract class AbstractUIPage extends AbstractUIForm 
    implements OnComponentPopulated, InvokeOnComponent, LayoutContainer, DeprecatedDimension {

  private static final Log LOG = LogFactory.getLog(AbstractUIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";

  public static final String FORM_ACCEPT_CHARSET = "utf-8";

  private static final int DEFAULT_WIDTH = 1024;

  private static final int DEFAULT_HEIGHT = 768;

  private String formId;

  private String actionId;

  private Box actionPosition;

  private String defaultActionId;

  private List<KeyValue> postfields;

  @Override
  public boolean getRendersChildren() {
    return true;
  }

  public String getFormId(FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext) + SUBCOMPONENT_SEP + "form";
    }
    return formId;
  }

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {

    super.encodeBegin(facesContext);

    UILayout layoutManager = (UILayout) getLayoutManager();
    if (layoutManager != null) {
      layoutManager.encodeBegin(facesContext);
    } else {
      // todo: later: LOG.debug or remove
      LOG.warn("no layout manager found");
    }
  }

  @Override
  public void encodeChildren(FacesContext facesContext) throws IOException {

    UILayout layoutManager = (UILayout) getLayoutManager();
    if (layoutManager != null) {
      layoutManager.encodeChildren(facesContext);
    } else {
      // todo: later: LOG.debug or remove
      LOG.warn("no layout manager found");
      super.encodeChildren(facesContext);
    }
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {

    UILayout layoutManager = (UILayout) getLayoutManager();
    if (layoutManager != null) {
      layoutManager.encodeEnd(facesContext);
    } else {
      // todo: later: LOG.debug or remove
      LOG.warn("no layout manager found");
    }

    super.encodeEnd(facesContext);
  }

  @Override
  public void processDecodes(FacesContext facesContext) {

    checkTobagoRequest(facesContext);

    decode(facesContext);

    markSubmittedForm(facesContext);

    // invoke processDecodes() on children
    for (Iterator kids = getFacetsAndChildren(); kids.hasNext();) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(facesContext);
    }
  }

  public void markSubmittedForm(FacesContext facesContext) {
    // find the form of the action command and set submitted to it and all
    // children

    // reset old submitted state
    setSubmitted(false);

    String currentActionId = getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + currentActionId + "'");
    }

    UIComponent command = null;
    try { // todo: try bock should be removed, when tested, that the warning usually not accure.
      command = findComponent(currentActionId);
    } catch (Exception e) {
      LOG.warn("Should not happen!!! currentActionId='" + currentActionId + "'");
    }

    // TODO: remove this if block if prooven this never happens anymore
    if (command == null
        && currentActionId != null && currentActionId.matches(".*:\\d+:.*")) {
      // If currentActionId component was inside a sheet the id contains the
      // rowindex and is therefore not found here.
      // We do not need the row here because we want just to find the
      // related form, so removing the rowindex will help here.
      currentActionId = currentActionId.replaceAll(":\\d+:", ":");
      try {
        command = findComponent(currentActionId);
        LOG.info("command = \"" + command + "\"", new Exception());
      } catch (Exception e) {
        // ignore
      }
    }

    if (LOG.isTraceEnabled()) {
      LOG.trace(currentActionId);
      LOG.trace(command);
      LOG.trace(DebugUtils.toString(facesContext.getViewRoot(), 0));
    }

    if (command != null) {
      AbstractUIForm form = ComponentUtils.findForm(command);
      form.setSubmitted(true);

      if (LOG.isTraceEnabled()) {
        LOG.trace(form);
        LOG.trace(form.getClientId(facesContext));
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("Illegal actionId! Rerender the view.");
      }
      facesContext.renderResponse();
    }
  }

  private void checkTobagoRequest(FacesContext facesContext) {
    // multipart/form-data must use TobagoMultipartFormdataRequest
    String contentType = (String) facesContext.getExternalContext()
        .getRequestHeaderMap().get("content-type");
    if (contentType != null && contentType.startsWith("multipart/form-data")) {
      Object request = facesContext.getExternalContext().getRequest();
      boolean okay = false;
      if (request instanceof TobagoMultipartFormdataRequest) {
        okay = true;
      } else if (request instanceof HttpServletRequestWrapper) {
        ServletRequest wrappedRequest
            = ((HttpServletRequestWrapper) request).getRequest();
        if (wrappedRequest instanceof TobagoMultipartFormdataRequest) {
          okay = true;
        }
      }
      // TODO PortletRequest ??
      if (!okay) {
        LOG.error("Can't process multipart/form-data without TobagoRequest. "
            + "Please check the web.xml and define a TobagoMultipartFormdataFilter. "
            + "See documentation for <tc:file>");
        facesContext.addMessage(null, new FacesMessage("An error has occured!"));
      }
    }
  }

  public List<KeyValue> getPostfields() {
    if (postfields == null) {
      postfields = new ArrayList<KeyValue>();
    }
    return postfields;
  }

  @Override
  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updatePageState(context);
  }

  public void updatePageState(FacesContext facesContext) {
    PageState state = getPageState(facesContext);
    decodePageState(facesContext, state);
  }

  @SuppressWarnings("unchecked")
  private void decodePageState(FacesContext facesContext, PageState pageState) {
    String name;
    String value = null;
    try {
      name = getClientId(facesContext)
          + SUBCOMPONENT_SEP + "form-clientDimension";
      value = (String) facesContext.getExternalContext()
          .getRequestParameterMap().get(name);
      if (value != null) {
        StringTokenizer tokenizer = new StringTokenizer(value, ";");
        int width = Integer.parseInt(tokenizer.nextToken());
        int height = Integer.parseInt(tokenizer.nextToken());
        if (pageState != null) {
          pageState.setClientWidth(width);
          pageState.setClientHeight(height);
        }
        facesContext.getExternalContext().getRequestMap().put("tobago-page-clientDimension-width", width);
        facesContext.getExternalContext().getRequestMap().put("tobago-page-clientDimension-height", height);
      }
    } catch (Exception e) {
      LOG.error("Error in decoding state: value='" + value + "'", e);
    }
  }

  public PageState getPageState(FacesContext facesContext) {
    if (FacesUtils.hasValueBindingOrValueExpression(this, Attributes.STATE)) {
      PageState state = (PageState)
          FacesUtils.getValueFromValueBindingOrValueExpression(facesContext, this, Attributes.STATE);
      if (state == null) {
        state = new PageStateImpl();
        FacesUtils.setValueOfBindingOrExpression(facesContext, state, this, Attributes.STATE);
      }
      return state;
    } else {
      return null;
    }
  }

  public String getActionId() {
    return actionId;
  }

  public void setActionId(String actionId) {
    this.actionId = actionId;
  }

  public Box getActionPosition() {
    return actionPosition;
  }

  public void setActionPosition(Box actionPosition) {
    this.actionPosition = actionPosition;
  }

  public String getDefaultActionId() {
    return defaultActionId;
  }

  public void setDefaultActionId(String defaultActionId) {
    this.defaultActionId = defaultActionId;
  }

  protected Integer getWidthInternal() {
    Integer requestWidth =
        (Integer) FacesContext.getCurrentInstance().getExternalContext().
            getRequestMap().get("tobago-page-clientDimension-width");
    if (requestWidth != null) {
      return requestWidth;
    } else {
      return DEFAULT_WIDTH;
    }
  }


  protected Integer getHeightInternal() {
    Integer requestHeight =
        (Integer) FacesContext.getCurrentInstance().getExternalContext().
            getRequestMap().get("tobago-page-clientDimension-height");
    if (requestHeight != null) {
      return requestHeight;
    } else {
      return DEFAULT_HEIGHT;
    }
  }

  public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback)
      throws FacesException {
    return FacesUtils.invokeOnComponent(context, this, clientId, callback);
  }

  public void onComponentPopulated(FacesContext facesContext) {
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.GRID_LAYOUT, RendererTypes.GRID_LAYOUT));
    }
  }

  // LAYOUT Begin
  public List<LayoutComponent> getComponents() {
    return LayoutUtils.findLayoutChildren(this);
  }

  public LayoutManager getLayoutManager() {
    return (LayoutManager) getFacet(Facets.LAYOUT);
  }

  public void setLayoutManager(LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (UILayout) layoutManager);
  }

  // LAYOUT End

  public abstract Measure getWidth();

  public abstract Measure getHeight();
}
