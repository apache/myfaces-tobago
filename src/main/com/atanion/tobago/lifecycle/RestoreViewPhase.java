/*
 * Copyright (c) 2002 Atanion GmbH, Germany
 * All rights reserved. Created Nov 19, 2002 at 8:43:54 AM.
 * $Id$
 */
package com.atanion.tobago.lifecycle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public class RestoreViewPhase extends Phase {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// constant

  private static Log log = LogFactory.getLog(RestoreViewPhase.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void execute(FacesContext facesContext) throws FacesException {
    handleEncoding(facesContext);

    String viewId = ((HttpServletRequest) facesContext.getExternalContext()
        .getRequest()).getPathInfo();
    ViewHandler viewHandler = facesContext.getApplication().getViewHandler();
    UIViewRoot viewRoot = viewHandler.restoreView(facesContext, viewId);
    if (viewRoot == null) {
      viewRoot = viewHandler.createView(facesContext, viewId);
    }
    facesContext.setViewRoot(viewRoot);
  }

  private void handleEncoding(FacesContext facesContext) {
    HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext()
        .getRequest();
    log.info(
        "request.getCharacterEncoding() = '" + request.getCharacterEncoding() +
        "'");
    try {
      if (request.getCharacterEncoding() == null) {
        request.setCharacterEncoding("UTF-8");
        log.info(
            "request.getCharacterEncoding() = '" +
            request.getCharacterEncoding() +
            "'");
      }
    } catch (UnsupportedEncodingException e) {
      log.error("" + e, e);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
