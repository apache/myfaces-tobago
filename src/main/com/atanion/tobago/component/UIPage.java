/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 19, 2002 5:00:55 PM.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.model.PageState;
import com.atanion.tobago.model.PageStateImpl;
import com.atanion.util.collections.ListOrderedSet;
import com.atanion.util.KeyValuePair;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

public class UIPage extends UIForm {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UIPage.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Page";

  public static final String DEFAULT_STYLE = "style/style.css";

// ///////////////////////////////////////////// attribute

  private String formId;

  private String focusId;

  private String actionId;

  private List<KeyValuePair> postfields;

  private ListOrderedSet scriptFiles;

  private Set<String> scriptBlocks;

  private Set<String> styleFiles;

  private Set<String> styleBlocks;

  private Set<String> onloadScripts;


// ///////////////////////////////////////////// constructor

  public UIPage() {
    scriptFiles = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());

    scriptBlocks = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());

    styleFiles = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());
    styleFiles.add(DEFAULT_STYLE);

    styleBlocks = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());

    onloadScripts = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());
  }

// ///////////////////////////////////////////// code

  public void encodeBegin(FacesContext facesContext) throws IOException {
    UILayout.getLayout(this).layoutBegin(facesContext, this);
    super.encodeBegin(facesContext);
  }


  public void encodeChildren(FacesContext context) throws IOException {

  }

  public String getFormId(FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext)
          + TobagoConstants.SUBCOMPONENT_SEP + "form";
    }
    return formId;
  }

  public void processDecodes(FacesContext facesContext) {

    decode(facesContext);

    // reset old submitted state
    setSubmitted(false);

    // find the form of the action command and set submitted to it and all
    // children
    String actionId = getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
    }
    if (actionId == null) { // todo: check why this is needed for dateControl
      return;
    }

    UIComponent command = findComponent(actionId);

    // fixme: hotfix for UICommand inside of a sheet.
    while (command == null && actionId.indexOf(':') != -1) {
      actionId = StringUtils.substring(actionId, 0, actionId.lastIndexOf(':'));
      command = findComponent(actionId);
    }

    UIForm form = (UIForm) ComponentUtil.findForm(command);

    if (LOG.isDebugEnabled()) {
      LOG.debug(command);
      LOG.debug(form);
      ComponentUtil.debug(facesContext.getViewRoot(), 0);
      if (form != null) {
        LOG.debug(form.getClientId(facesContext));
      }
    }

    if (form != null) {
      form.setSubmitted(true);

      Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        kid.processDecodes(facesContext);
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("No form found! Rerender the view.");
      }
      facesContext.renderResponse();
    }

    getOnloadScripts().clear();
    getScriptBlocks().clear();
  }

  public List<KeyValuePair> getPostfields() {
    if (postfields == null) {
      postfields = new ArrayList<KeyValuePair>();
    }
    return postfields;
  }

  public void processUpdates(FacesContext context) {
    super.processUpdates(context);
    updatePageState(context);
  }

  public void updatePageState(FacesContext facesContext) {
    PageState state = getPageState(facesContext);
    if (state != null) {
      decodePageState(facesContext, state);
    }
  }

  private void decodePageState(FacesContext facesContext, PageState pageState) {
    String name = null;
    String value = null;
    try {
      name = getClientId(facesContext)
              + TobagoConstants.SUBCOMPONENT_SEP + "form-clientDimension";
      value = (String) facesContext.getExternalContext()
              .getRequestParameterMap().get(name);
      StringTokenizer tokenizer = new StringTokenizer(value, ";");
      int width = Integer.parseInt(tokenizer.nextToken());
      int height = Integer.parseInt(tokenizer.nextToken());
      pageState.setClientWidth(width);
      pageState.setClientHeight(height);
    } catch (Exception e) {
      LOG.error("Error in decoding state: value='" + value + "'", e);
    }
  }

  public PageState getPageState(FacesContext facesContext) {
    ValueBinding stateBinding = getValueBinding(TobagoConstants.ATTR_STATE);
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

  public ListOrderedSet getScriptFiles() {
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
}
