/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 09.01.2004 11:57:24.
 * $Id$
 */
package com.atanion.tobago.el;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;

public class ConstantMethodBinding extends MethodBinding {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private String outcome;

// ///////////////////////////////////////////// constructor

  public ConstantMethodBinding(String outcome) {
    this.outcome = outcome;
  }

// ///////////////////////////////////////////// code

  public Object invoke(FacesContext facescontext, Object aobj[])
      throws EvaluationException, MethodNotFoundException {
    return outcome;
  }

  public Class getType(FacesContext facescontext)
      throws MethodNotFoundException {
    return String.class;
  }

  public String getExpressionString() {
    return outcome;
  }

// ///////////////////////////////////////////// bean getter + setter

}
