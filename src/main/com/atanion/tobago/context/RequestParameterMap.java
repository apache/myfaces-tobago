/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 09.01.2004 11:28:20.
 * $Id$
 */
package com.atanion.tobago.context;

import javax.servlet.ServletRequest;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

class RequestParameterMap extends BaseMap {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

  private ServletRequest request;

// ///////////////////////////////////////////// constructor

  RequestParameterMap(ServletRequest request) {
    this.request = request;
  }

// ///////////////////////////////////////////// code

  public Object get(Object key) {
//    if (key == "com.sun.faces.IMMUTABLE") {
//      return "com.sun.faces.IMMUTABLE";
//    } else {
      return request.getParameter(key.toString());
//    }
  }

  public Set entrySet() {
    Set entries = new HashSet();
    for (Enumeration e = request.getParameterNames(); e.hasMoreElements();) {
      String paramName = (String) e.nextElement();
      entries.add(
          new BaseMap.Entry(paramName, request.getParameter(paramName)));
    }

    return entries;
  }

  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof RequestParameterMap)) {
      return false;
    } else {
      return super.equals(obj);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}
