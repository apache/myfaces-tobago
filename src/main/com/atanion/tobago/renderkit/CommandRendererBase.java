/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 14.04.2003 at 15:10:58.
  * $Id$
  */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class CommandRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CommandRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }
    String actionId = ComponentUtil.findPage(component).getActionId();
    if (LOG.isDebugEnabled()) {
      LOG.debug("actionId = '" + actionId + "'");
    }
    String clientId = component.getClientId(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("clientId = '" + clientId + "'");
    }
    if (actionId != null && actionId.equals(clientId)) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("queueEvent");
      }

      component.queueEvent(new ActionEvent(component));
//      ((UICommand) component).fireActionEvent(facesContext);

//      UIForm form = ComponentUtil.findForm(component);
//      if (form != null) {
//        form.setSubmitted(true);
//        if (LOG.isDebugEnabled()) {
//          LOG.debug("setting Form Active: " + form.getClientId(facesContext));
//        }
//      }
    }
  }

  public static String appendConfirmationScript(String onclick,
      UIComponent component, FacesContext facesContext) {
    ValueHolder confirmation
        = (ValueHolder) component.getFacet(FACET_CONFIRMATION);
    if (confirmation != null) {
      if (onclick != null) {
        onclick = "confirm('" + confirmation.getValue() + "') && " + onclick;
      } else {
        if (LOG.isWarnEnabled()) {
          LOG.warn("Facet confirm is not supported for this type of button. " +
              "id = '" + component.getClientId(facesContext) + "'");
        }
      }
    }
    return onclick;
  }

// ///////////////////////////////////////////// bean getter + setter

}
