/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 20.10.2004 11:21:16.
 * $Id$
 */
package org.apache.myfaces.tobago.el;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.el.VariableResolver;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

public class VariableResolverImpl extends VariableResolver {

  private static final Log LOG = LogFactory.getLog(VariableResolverImpl.class);

  private VariableResolver base;
  private UserWrapper userWrapper;

  public VariableResolverImpl(VariableResolver base) {
    LOG.info("Hiding ri base implemation: " + base);
    this.base = base;
  }

  public Object resolveVariable(FacesContext facesContext, String name)
      throws EvaluationException {

    if ("user".equals(name)) {
      // todo: optimize me: put it in request?
      if (LOG.isDebugEnabled()) {
        LOG.debug("resolving: " + name);
      }
      if (userWrapper == null) {
        userWrapper = new UserWrapper();
      }
      return userWrapper;
    } else {
      return base.resolveVariable(facesContext, name);
    }
  }
}
