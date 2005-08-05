package com.atanion.util.annotation;

/**
 * Copyright (c) 2003 Atanion GmbH, Germany. All rights reserved.
 * Created: Apr 20, 2005 5:08:01 PM
 * User: bommel
 * $Id: BodyContent.java,v 1.3 2005/05/11 15:20:34 bommel Exp $
 */
public enum BodyContent {

  JSP, EMPTY, TAGDEPENDENT;

  public String toString(){
    switch (this) {
      case JSP: return "JSP";
      case EMPTY: return "empty";
      case TAGDEPENDENT: return "tagdependent";
    }
    throw new IllegalStateException("Unexpected BodyContent "+name());
  }
}
