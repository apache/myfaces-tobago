/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 09:26:24.
  * $Id$
  */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.UIPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class PageRendererBase extends RendererBase {

// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(PageRendererBase.class);

// ----------------------------------------------------------- business methods

  public void decode(FacesContext facesContext, UIComponent component) {
    UIPage page = (UIPage) component;
    String name = page.getClientId(facesContext)
        + SUBCOMPONENT_SEP + "form-action";
    String newActionId = (String) facesContext.getExternalContext()
        .getRequestParameterMap().get(name);
    page.setActionId(newActionId);
  }
}

