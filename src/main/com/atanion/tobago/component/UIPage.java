/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 19, 2002 5:00:55 PM.
 * $Id$
 */
package com.atanion.tobago.component;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.TobagoResourceSet;
import com.atanion.util.collections.ListOrderedSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class UIPage extends UIForm {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(UIPage.class);

  public static final String COMPONENT_TYPE="com.atanion.tobago.Page";

  public static final String DEFAULT_STYLE = "style.css";

// ///////////////////////////////////////////// attribute

  private String formId;

  private String focusId;

  private String actionId;

  private List postfields;

  private TobagoResourceSet scriptFiles;

  private Set scriptBlocks;

  private Set styleFiles;

  private Set styleBlocks;

  private Set onloadScripts;

// ///////////////////////////////////////////// constructor

  public UIPage() {
    scriptFiles = new TobagoResourceSet();//ListOrderedSet.decorate(new HashSet());

    scriptBlocks = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());

    styleFiles = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());
    styleFiles.add(DEFAULT_STYLE);

    styleBlocks = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());

    onloadScripts = new ListOrderedSet();//ListOrderedSet.decorate(new HashSet());
  }

// ///////////////////////////////////////////// code

  public String getFormId(FacesContext facesContext) {
    if (formId == null) {
      formId = getClientId(facesContext)
          + TobagoConstants.SUBCOMPONENT_SEP + "form";
    }
    return formId;
  }

  public void processDecodes(FacesContext facesContext) {

    if (!ComponentUtil.isOutputOnly(this)) {
      decode(facesContext);
    }

    for (Iterator i = getFacetsAndChildren(); i.hasNext();) {
      ((UIComponent) i.next()).processDecodes(facesContext);
    }
  }

  public void storeFocusId(String id) {
    if (getFocusId() != null) {
      LOG.warn("local focusId = \"" + getFocusId() + "\" ignore new value \""
          + id + "\"");
    } else {
      setFocusId(id);
    }
  }

  public List getPostfields() {
    if (postfields == null) {
      postfields = new ArrayList();
    }
    return postfields;
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

  public TobagoResourceSet getScriptFiles() {
    return scriptFiles;
  }

  public Set getScriptBlocks() {
    return scriptBlocks;
  }

  public Set getStyleFiles() {
    return styleFiles;
  }

  public Set getStyleBlocks() {
    return styleBlocks;
  }

  public Set getOnloadScripts() {
    return onloadScripts;
  }
}
