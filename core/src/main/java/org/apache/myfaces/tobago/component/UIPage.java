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

import org.apache.commons.collections.KeyValue;
import org.apache.commons.collections.list.SetUniqueList;
import org.apache.commons.collections.set.ListOrderedSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_APPLICATION_ICON;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FOCUS_ID;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.SUBCOMPONENT_SEP;
import org.apache.myfaces.tobago.layout.Box;
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

public class UIPage extends UIForm {

  private static final Log LOG = LogFactory.getLog(UIPage.class);

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Page";
  public static final String ENCTYPE_KEY = UIPanel.class.getName() + ".enctype";

  public static final String DEFAULT_STYLE = "style/style.css";

  private static final int DEFAULT_WIDTH = 1024;

  private static final int DEFAULT_HEIGHT = 768;

  private String formId;

  private String focusId;

  private String actionId;

  private Box actionPosition;

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

  private Set<UIPopup> popups;

  private Integer width;

  private Integer height;

  private String applicationIcon;

  @SuppressWarnings("unchecked")
  public UIPage() {
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
      LOG.trace(ComponentUtil.toString(facesContext.getViewRoot(), 0));
    }

    if (command != null) {
      UIForm form = ComponentUtil.findForm(command);
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
    if (focusId != null) {
      return focusId;
    }
    ValueBinding vb = getValueBinding(ATTR_FOCUS_ID);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
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

  public Set<UIPopup> getPopups() {
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
              getRequestMap().get("tobago-page-clientDimension-width");
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

  public String getApplicationIcon() {
    if (applicationIcon != null) {
      return applicationIcon;
    }
    ValueBinding vb = getValueBinding(ATTR_APPLICATION_ICON);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setApplicationIcon(String applicationIcon) {
    this.applicationIcon = applicationIcon;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    this.width = (Integer) values[1];
    this.height = (Integer) values[2];
    this.focusId = (String) values[3];
    this.applicationIcon = (String) values[4];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[5];
    values[0] = super.saveState(context);
    values[1] = width;
    values[2] = height;
    values[3] = focusId;
    values[4] = applicationIcon;
    return values;
  }
}
