/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 20.10.2004 11:21:16.
 * $Id$
 */
package com.atanion.tobago.el;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class VariableResolverImpl extends VariableResolver {

  private static final Log LOG = LogFactory.getLog(VariableResolverImpl.class);

  private VariableResolver base;

  public VariableResolverImpl(VariableResolver base) {
    LOG.info("Hiding ri base implemation: " + base);
    this.base = base;
  }

  public Object resolveVariable(FacesContext facesContext, String name)
      throws EvaluationException {
    LOG.info("resolving: " + name);

    if ("user".equals(name)) {
      // todo: optimize me: not a new object every time
      // todo: but keep "thread save"
      return new UserWrapper();
    } else {
      return base.resolveVariable(facesContext, name);
    }
  }
}
