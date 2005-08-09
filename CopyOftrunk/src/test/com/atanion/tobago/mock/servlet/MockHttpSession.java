/*
 * Copyright (c) 2004 Atanion GmbH, Germany
 * All rights reserved. Created 25.08.2004 10:59:15.
 * $Id: MockHttpSession.java,v 1.1.1.1 2004/08/27 13:02:11 lofwyr Exp $
 */
package com.atanion.tobago.mock.servlet;

import org.apache.commons.collections.iterators.IteratorEnumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.servlet.ServletContext;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

public class MockHttpSession implements HttpSession {

  private Map attributes = new HashMap();

  public Object getAttribute(String s) {
    return attributes.get(s);
  }

  public Enumeration getAttributeNames() {
    return (new IteratorEnumeration(attributes.keySet().iterator()));
  }

  public long getCreationTime() {
    return 0;
  }

  public String getId() {
    return null;
  }

  public long getLastAccessedTime() {
    return 0;
  }

  public int getMaxInactiveInterval() {
    return 0;
  }

  /** @deprecated */
  public HttpSessionContext getSessionContext() {
    return null;
  }

  public Object getValue(String s) {
    return null;
  }

  public String[] getValueNames() {
    return new String[0];
  }

  public void invalidate() {
  }

  public boolean isNew() {
    return false;
  }

  public void putValue(String s, Object o) {
  }

  public void removeAttribute(String s) {
    attributes.remove(s);
  }

  public void removeValue(String s) {
  }

  public void setAttribute(String s, Object o) {
    attributes.put(s, o);
  }

  public void setMaxInactiveInterval(int i) {
  }

  public ServletContext getServletContext() {
    return null;
  }
}