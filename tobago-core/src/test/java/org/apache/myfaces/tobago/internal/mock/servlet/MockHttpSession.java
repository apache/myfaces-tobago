/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.mock.servlet;

import org.apache.commons.collections.iterators.IteratorEnumeration;
import org.apache.commons.lang.ArrayUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

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
    return ArrayUtils.EMPTY_STRING_ARRAY;
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
