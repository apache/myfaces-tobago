/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 23.06.2004 16:12:21.
 * $Id$
 */
package com.atanion.tobago.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UIForm extends javax.faces.component.UIForm {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(UIForm.class);

  public static final String COMPONENT_TYPE = "com.atanion.tobago.Form";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void processDecodes(FacesContext facesContext) {

    // Process this component itself
    decode(facesContext);

    Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(facesContext);
    }
  }

  public void setSubmitted(boolean b) {
    super.setSubmitted(b);

    // set submitted for all subforms
    List<UIForm> collect = new ArrayList<UIForm>();
    ComponentUtil.findSubForms(collect, this);
    for (int i = 0; i < collect.size(); i++) {
      UIForm subForm = (UIForm) collect.get(i);
      subForm.setSubmitted(b);
    }
  }

  public void processValidators(FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processValidators for form: " + getClientId(facesContext));
    }
    if (!isSubmitted()) {
      List<UIForm> collect = new ArrayList<UIForm>();
      ComponentUtil.findSubForms(collect, this);
      for (int i = 0; i < collect.size(); i++) {
        UIForm subForm = (UIForm) collect.get(i);
        subForm.processValidators(facesContext);
      }
    } else {
      // Process all facets and children of this component
      Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        kid.processValidators(facesContext);
      }
    }
  }

  public void processUpdates(FacesContext facesContext) {
    // if we're not the submitted form, only process subforms.
    if (LOG.isDebugEnabled()) {
      LOG.debug("processUpdates for form: " + getClientId(facesContext));
    }
    if (!isSubmitted()) {
      List<UIForm> collect = new ArrayList<UIForm>();
      ComponentUtil.findSubForms(collect, this);
      for (UIForm subForm : collect) {
        subForm.processUpdates(facesContext);
      }
    } else {
      // Process all facets and children of this component
      Iterator kids = getFacetsAndChildren();
      while (kids.hasNext()) {
        UIComponent kid = (UIComponent) kids.next();
        kid.processUpdates(facesContext);
      }
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
