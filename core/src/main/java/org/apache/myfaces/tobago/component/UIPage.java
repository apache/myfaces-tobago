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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.model.PageState;
import org.apache.myfaces.tobago.model.PageStateImpl;
import org.apache.myfaces.tobago.webapp.TobagoMultipartFormdataRequest;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class UIPage extends UIForm {

  private static final Log LOG = LogFactory.getLog(UIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";

  public static final String DEFAULT_STYLE = "style/style.css";

  private static final int DEFAULT_WIDTH = 1024;

  private static final int DEFAULT_HEIGHT = 768;

  private String formId;

  private String focusId;

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

  private List<UIPopup> popups;

  private Integer width;

  private Integer height;

  public UIPage() {
    scriptFiles = SetUniqueList.decorate(new ArrayList());
    scriptBlocks = new ListOrderedSet();
    styleFiles = new ListOrderedSet();
    styleFiles.add(DEFAULT_STYLE);
    styleBlocks = new ListOrderedSet();
    onloadScripts = new ListOrderedSet();
    onunloadScripts = new ListOrderedSet();
    onexitScripts = new ListOrderedSet();
    popups = new ArrayList<UIPopup>();
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

    decode(facesContext);

    // reset old submitted state
    setSubmitted(false);

    // clear script Set's
    getOnloadScripts().clear();
    getOnunloadScripts().clear();
    getOnexitScripts().clear();
    getScriptBlocks().clear();
    getPopups().clear();

    // find the form of the action command and set submitted to it and all
    // children
    String currentActionId = getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + currentActionId + "'");
    }
    if (currentActionId == null) { // TODO: check why this is needed for dateControl
      return;
    }

    UIComponent command = null;
    try {
      command = findComponent(currentActionId);
    } catch (IllegalArgumentException e) {
      // FIXME: hotfix for TOBAGO-43
    }

    // FIXME: hotfix for UICommand inside of a sheet.
    while (command == null && currentActionId.indexOf(':') != -1) {
      currentActionId = StringUtils.substring(currentActionId, 0, currentActionId.lastIndexOf(':'));
      command = findComponent(currentActionId);
    }

    UIForm form = ComponentUtil.findForm(command);

    if (LOG.isDebugEnabled()) {
      LOG.debug(command);
      LOG.debug(form);
      LOG.debug(ComponentUtil.toString(facesContext.getViewRoot(), 0));
      if (form != null) {
        LOG.debug(form.getClientId(facesContext));
      }
    }

    Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(facesContext);
    }

    if (form != null) {
      form.setSubmitted(true);
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No form found! Rerender the view.");
      }
      facesContext.renderResponse();
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
          facesContext.getExternalContext().getRequestMap().put("tobago-page-clientDimension-with", width);
          facesContext.getExternalContext().getRequestMap().put("tobago-page-clientDimension-height", height);
        }
    } catch (Exception e) {
      LOG.error("Error in decoding state: value='" + value + "'", e);
    }
  }

  public PageState getPageState(FacesContext facesContext) {
    ValueBinding stateBinding = getValueBinding(ATTR_STATE);
    if (stateBinding != null) {
      PageState state = (PageState) stateBinding.getValue(facesContext);
      if (state == null) {
        state = new PageStateImpl();
        stateBinding.setValue(facesContext, state);
      }
      return state;
    } else {
      return null;
    }
  }

// ///////////////////////////////////////////// bean getter + setter

  public String getFocusId() {
    return focusId;
  }

  public void setFocusId(String focusId) {
    this.focusId = focusId;
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

  public List<UIPopup> getPopups() {
    return popups;
  }

  public Integer getWidth() {
    if (width != null) {
      return width;
    }
    ValueBinding vb = getValueBinding(ATTR_WIDTH);
    if (vb != null) {
      return (Integer) vb.getValue(getFacesContext());
    } else {
      Integer requestWidth =
          (Integer) FacesContext.getCurrentInstance().getExternalContext().
              getRequestMap().get("tobago-page-clientDimension-with");
      if (requestWidth != null) {
        return requestWidth;
      } else {
        return DEFAULT_WIDTH;
      }
    }
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    if (height != null) {
      return height;
    }
    ValueBinding vb = getValueBinding(ATTR_HEIGHT);
    if (vb != null) {
      return (Integer) vb.getValue(getFacesContext());
    } else {
      Integer requestHeight =
          (Integer) FacesContext.getCurrentInstance().getExternalContext().
              getRequestMap().get("tobago-page-clientDimension-height");
      if (requestHeight != null) {
        return requestHeight;
      } else {
        return DEFAULT_HEIGHT;
      }
    }
  }

  public void setHeight(Integer height) {
    this.height = height;
  }
}
