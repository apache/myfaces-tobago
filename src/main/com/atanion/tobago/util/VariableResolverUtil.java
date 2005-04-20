/*
 * Copyright (c) 2004 atanion GmbH, Germany. All rights reserved.
 * Created: 28.04.2004, 18:23:31
 * $Id$
 */
package com.atanion.tobago.util;

import javax.faces.context.FacesContext;
import javax.faces.application.Application;
import javax.faces.el.VariableResolver;

public class VariableResolverUtil {

  public static Object resolveVariable(String variable) {
    FacesContext context = FacesContext.getCurrentInstance();
    return resolveVariable(context, variable);
  }

  public static Object resolveVariable(FacesContext context, String variable) {
    Application application = context.getApplication();
    VariableResolver variableResolver = application.getVariableResolver();
    return variableResolver.resolveVariable(context, variable);
  }

}
