/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 15.01.2004 09:32:12.
 * $Id$
 */
package com.atanion.tobago.el;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodBindingImpl extends MethodBinding {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MethodBindingImpl.class);

// ///////////////////////////////////////////// attribute

  private Class params[];
  private String name;
  private String rawRef;
  private ValueBinding binding;

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

// ///////////////////////////////////////////// bean getter + setter


  public MethodBindingImpl(Application application, String ref, Class params[]) {
    if (application == null || ref == null) {
      throw new NullPointerException();
    }
    if (!ref.startsWith("#{") || !ref.endsWith("}")) {
      LOG.error(" Expression " + ref + " does not follow the syntax #{...}");
      throw new ReferenceSyntaxException(ref);
    }
    rawRef = ref;
    ref = ElUtil.clipReferenceBackets(ref);
    this.params = params;
    String vbRef;
    if (ref.endsWith("]")) {
      int left = ref.lastIndexOf("[");
      if (left < 0) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Expression syntax error: Missing '[' in " + ref);
        }
        throw new ReferenceSyntaxException(ref);
      }
      vbRef = "#{" + ref.substring(0, left) + "}";
      binding = application.createValueBinding(vbRef);
      name = ref.substring(left + 1);
      name = name.substring(0, name.length() - 1);
    } else {
      int period = ref.lastIndexOf(".");
      if (period < 0) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("Expression syntax error: Missing '.' in " + ref);
        }
        throw new ReferenceSyntaxException(ref);
      }
      vbRef = "#{" + ref.substring(0, period) + "}";
      binding = application.createValueBinding(vbRef);
      name = ref.substring(period + 1);
    }
    if (name.length() < 1) {
      if (LOG.isDebugEnabled()) {
        LOG.debug(
            "Expression syntax error: Missing name after period in:" + ref);
      }
      throw new ReferenceSyntaxException(ref);
    } else {
      return;
    }
  }

  public Object invoke(FacesContext context, Object params[])
      throws EvaluationException, MethodNotFoundException {
    Object base;
    Method method;
    if (context == null) {
      throw new NullPointerException();
    }
    base = binding.getValue(context);
    method = method(base);

    LOG.debug("params.length=" +  (params == null ? "null" : ("" + params.length)));
    LOG.debug("invoking: " + method.toString());

    try {
      return method.invoke(base, params);
    } catch (IllegalAccessException e) {
      throw new EvaluationException(e);
    } catch (InvocationTargetException e) {
      throw new EvaluationException(e.getTargetException());
    }
  }

  public Class getType(FacesContext context) {
    Object base = binding.getValue(context);
    Method method = method(base);
    Class returnType = method.getReturnType();
    if (LOG.isDebugEnabled()) {
      LOG.debug("Method return type:" + returnType.getName());
    }
    if ("void".equals(returnType.getName())) {
      return null;
    } else {
      return returnType;
    }
  }

  public String getExpressionString() {
    return rawRef;
  }

  private Method method(Object base) {
    Class clazz;
    if (null == base) {
      throw new MethodNotFoundException(name);
    }
    clazz = base.getClass();
    try {
      return clazz.getMethod(name, params);
    } catch (NoSuchMethodException e) {
      throw new MethodNotFoundException(name + ": " + e.getMessage());
    }
  }
}
