/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.04.2004 18:49:37.
 * $Id$
 */
package com.atanion.tobago.el;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class VariableResolverImpl extends VariableResolver {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(VariableResolverImpl.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public Object resolveVariable(FacesContext facesContext, String name)
      throws EvaluationException {
    LOG.debug("resolve: name='" + name + "'");
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

// ///////////////////////////////////////////// bean getter + setter

}
