/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 10:49:39.
  * $Id$
  */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.component.ComponentUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

public class SelectOneRendererBase extends InputRendererBase {

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
    if (newValue != null) {
      Object convertedValue = getConvertedValue(facesContext, component, newValue);
      uiSelectOne.setValue(convertedValue);
      uiSelectOne.setValid(true);
    } else {
      facesContext.addMessage(clientId, new FacesMessage("no value found", null));
      uiSelectOne.setValid(false);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

