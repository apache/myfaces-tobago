/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.04.2004 14:06:40.
 * $Id$
 */
package com.atanion.tobago.el;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.faces.el.PropertyResolver;
import javax.faces.el.EvaluationException;
import javax.faces.el.PropertyNotFoundException;

public class PropertyResolverImpl extends PropertyResolver {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(PropertyResolverImpl.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public Object getValue(Object obj, Object obj1) throws EvaluationException,
      PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  public Object getValue(Object obj, int i) throws EvaluationException,
      PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  public void setValue(Object obj, Object obj1, Object obj2)
      throws EvaluationException, PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
  }

  public void setValue(Object obj, int i, Object obj1)
      throws EvaluationException, PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
  }

  public boolean isReadOnly(Object obj, Object obj1)
      throws EvaluationException, PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return false;
  }

  public boolean isReadOnly(Object obj, int i) throws EvaluationException,
      PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return false;
  }

  public Class getType(Object obj, Object obj1) throws EvaluationException,
      PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

  public Class getType(Object obj, int i) throws EvaluationException,
      PropertyNotFoundException {
    LOG.error("This method isn't already implemented, and should not be called!"); //fixme
    return null;
  }

// ///////////////////////////////////////////// bean getter + setter

}
