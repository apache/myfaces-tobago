/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 10.04.2003 16:35:35.
 * $Id$
 */
package com.atanion.tobago.context;

import javax.faces.FacesException;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class FacesContextFactoryImpl extends javax.faces.context.FacesContextFactory {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public FacesContext getFacesContext(
      Object context, Object request, Object response, Lifecycle lifecycle)
      throws FacesException {

    ExternalContext externalContext = new ExternalContextImpl(
        (ServletContext)context,
        (ServletRequest)request,
        (ServletResponse)response);
    TobagoContext tobagoContext = new TobagoContext(externalContext, lifecycle);
    return tobagoContext;
  }

// ///////////////////////////////////////////// bean getter + setter

}
