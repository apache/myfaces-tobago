/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 09:26:24.
  * $Id$
  */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.component.UIPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class PageRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(PageRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public void decode(FacesContext facesContext, UIComponent uiComponent) {

    HttpServletRequest servletRequest
        = (HttpServletRequest) facesContext.getExternalContext().getRequest();
    String name = uiComponent.getClientId(facesContext)
        + SUBCOMPONENT_SEP + "form-action";
    String newActionId = servletRequest.getParameter(name);
    ((UIPage) uiComponent).setActionId(newActionId);
  }

// ///////////////////////////////////////////// bean getter + setter

}

