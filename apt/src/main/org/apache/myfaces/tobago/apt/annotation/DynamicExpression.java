package org.apache.myfaces.tobago.apt.annotation;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 27, 2005 5:08:45 PM
 * User: bommel
 * $Id: DynamicExpression.java,v 1.1 2005/05/11 15:20:34 bommel Exp $
 */
public enum DynamicExpression {

  VALUE_BINDING, METHOD_BINDING, NONE;

  public String toString() {
    switch (this) {
      case VALUE_BINDING:
        return "VB";
      case METHOD_BINDING:
        return "MB";
      case NONE:
        return "NONE";
    }
    throw new IllegalStateException("Unexpected DynamicExpression "+ name());
  }

}
