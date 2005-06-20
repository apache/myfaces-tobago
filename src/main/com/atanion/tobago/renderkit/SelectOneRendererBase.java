/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 10:49:39.
  * $Id$
  */
package com.atanion.tobago.renderkit;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.context.ResourceManagerUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;
import java.io.IOException;

public abstract class SelectOneRendererBase extends InputRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(SelectOneRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UISelectOne uiSelectOne = (UISelectOne) component;

    String clientId = uiSelectOne.getClientId(facesContext);
    Object newValue = ((ServletRequest) facesContext.getExternalContext().getRequest())
        .getParameter(clientId);
    if (LOG.isDebugEnabled()) {
      LOG.debug("decode: key='" + clientId + "' value='" + newValue + "'");
    }
    Object convertedValue = null;
    if (newValue != null) {
      convertedValue = getConvertedValue(facesContext, component, newValue);
    }
    uiSelectOne.setValue(convertedValue);
    uiSelectOne.setValid(true);    
  }


  protected abstract void renderMain(FacesContext facesContext, UIComponent input,
      TobagoResponseWriter writer) throws IOException;

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    super.encodeEndTobago(facesContext, component);
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();


    renderMain(facesContext, component, writer);
  }

// ///////////////////////////////////////////// bean getter + setter

}

