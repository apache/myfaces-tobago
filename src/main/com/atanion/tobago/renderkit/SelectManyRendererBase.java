/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 15.04.2003 at 10:17:34.
  * $Id$
  */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.component.ComponentUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.servlet.ServletRequest;

public class SelectManyRendererBase extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(
      SelectManyRendererBase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    UISelectMany uiSelectMany = (UISelectMany) component;

    String newValue[] = ((ServletRequest) facesContext.getExternalContext()
        .getRequest())
        .getParameterValues(uiSelectMany.getClientId(facesContext));
    if (LOG.isDebugEnabled()) {
      LOG.debug("decode: key='" + component.getClientId(facesContext)
          + "' value='" + newValue + "'");
      LOG.debug("size ... '" + (newValue != null ? newValue.length : -1) + "'");
      if (newValue != null) {
        for (int i = 0; i < newValue.length; i++) {
          if (LOG.isDebugEnabled()) {
            LOG.debug("newValue[i] = '" + newValue[i] + "'");
          }
        }
      }
    }

    if (newValue == null) {
      newValue = new String[0]; // because no selection will not submitted by browsers
    }
    uiSelectMany.setValue(newValue);
    uiSelectMany.setValid(true);
  }

// ///////////////////////////////////////////// bean getter + setter

}

