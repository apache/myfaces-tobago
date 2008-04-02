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
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.model.PageState;
import org.apache.myfaces.tobago.model.PageStateImpl;
import org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataRequest;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.util.ComponentUtil;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.ContextCallback;
import javax.faces.context.FacesContext;
import javax.faces.FacesException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public abstract class AbstractUIPage extends AbstractUIForm implements InvokeOnComponent {

  private static final Log LOG = LogFactory.getLog(AbstractUIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";
  public static final String ENCTYPE_KEY = AbstractUIPage.class.getName() + ".enctype";

  public static final String DEFAULT_STYLE = "style/style.css";

  private static final int DEFAULT_WIDTH = 1024;

  private static final int DEFAULT_HEIGHT = 768;

  private String formId;

  private String actionId;

  private String defaultActionId;

  private List<KeyValue> postfields;

  private SetUniqueList scriptFiles;

  private Set<String> scriptBlocks;

  private Set<String> styleFiles;

  private Set<String> styleBlocks;

  private Set<String> onloadScripts;

  private Set<String> onunloadScripts;

  private Set<String> onexitScripts;

  private Set<String> onsubmitScripts;

  private Set<AbstractUIPopup> popups;


  @SuppressWarnings("unchecked")
  public AbstractUIPage() {
    scriptFiles = SetUniqueList.decorate(new ArrayList());
    scriptBlocks = new ListOrderedSet();
    styleFiles = new ListOrderedSet();
    styleFiles.add(DEFAULT_STYLE);
    styleBlocks = new ListOrderedSet();
    onloadScripts = new ListOrderedSet();
    onunloadScripts = new ListOrderedSet();
    onexitScripts = new ListOrderedSet();
    onsubmitScripts = new ListOrderedSet();
    popups = new ListOrderedSet();
  }

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {
    // TODO change this should be renamed to DimensionUtils.prepare!!!
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }


  @Override
  public void encodeChildren(FacesContext context) throws IOException {
  }

  public String getFormId(FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext)
          + SUBCOMPONENT_SEP + "form";
    }
    return formId;
  }

  @Override
  public void processDecodes(FacesContext facesContext) {

    checkTobagoRequest(facesContext);

    decode(facesContext);

    clearScriptsAndPopups();

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
    try {
      command = findComponent(currentActionId);
    } catch (Exception e) {
      // ignore
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
      AbstractUIForm form = ComponentUtil.findForm(command);
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

  private void clearScriptsAndPopups() {
    // clear script Set's
    getOnloadScripts().clear();
    getOnunloadScripts().clear();
    getOnexitScripts().clear();
    getScriptBlocks().clear();
    getPopups().clear();
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
    if (FacesUtils.hasValueBindingOrValueExpression(this, ATTR_STATE)) {
      PageState state = (PageState)
          FacesUtils.getValueFromValueBindingOrValueExpression(facesContext, this, ATTR_STATE);
      if (state == null) {
        state = new PageStateImpl();
        FacesUtils.setValueOfBindingOrExpression(facesContext, state, this, ATTR_STATE);
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

  public String getDefaultActionId() {
    return defaultActionId;
  }

  public void setDefaultActionId(String defaultActionId) {
    this.defaultActionId = defaultActionId;
  }

  @SuppressWarnings("unchecked")
  public List<String> getScriptFiles() {
    return scriptFiles;
  }

  public Set<String> getScriptBlocks() {
    return scriptBlocks;
  }

  public Set<String> getStyleFiles() {
    return styleFiles;
  }

  public Set<String> getStyleBlocks() {
    return styleBlocks;
  }

  public Set<String> getOnloadScripts() {
    return onloadScripts;
  }

  public Set<String> getOnunloadScripts() {
    return onunloadScripts;
  }

  public Set<String> getOnexitScripts() {
    return onexitScripts;
  }

  public Set<String> getOnsubmitScripts() {
    return onsubmitScripts;
  }

  public Set<AbstractUIPopup> getPopups() {
    return popups;
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
}
