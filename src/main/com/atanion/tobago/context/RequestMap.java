/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.01.2004 11:29:55.
 * $Id$
 */
package com.atanion.tobago.context;

import javax.servlet.ServletRequest;
import java.util.AbstractMap;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class RequestMap extends AbstractMap {

// //////////////////////////////////////////  /// constant

// ///////////////////////////////////////////// attribute

  private ServletRequest request;

// /////////////////////////////////////  //////// constructor

  public RequestMap(ServletRequest request) {
    this.request = request;
  }

// /////////////////////  //////////////////////// code

  public Set entrySet() {
    Set entries = new HashSet();
    String key;
    for (Enumeration e = request.getAttributeNames(); e.hasMoreElements();) {
      key = (String) e.nextElement();
      entries.add(new BaseMap.Entry(key, request.getAttribute(key)));
    }

    return entries;
  }

  public Object put(Object key, Object value) {
    String string = key.toString();
    Object oldValue = request.getAttribute(string);
    request.setAttribute(string, value);
    return oldValue;
  }

// ///////////////////////////////////////////// bean getter + setter

}
