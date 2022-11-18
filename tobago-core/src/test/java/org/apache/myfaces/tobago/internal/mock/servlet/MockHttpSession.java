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

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionContext;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MockHttpSession implements HttpSession {

  private Map<String, Object> attributes = new HashMap<>();

  @Override
  public Object getAttribute(final String s) {
    return attributes.get(s);
  }

  @Override
  public Enumeration<String> getAttributeNames() {
    return new Vector(attributes.keySet()).elements();
  }

  @Override
  public long getCreationTime() {
    return 0;
  }

  @Override
  public String getId() {
    return null;
  }

  @Override
  public long getLastAccessedTime() {
    return 0;
  }

  @Override
  public int getMaxInactiveInterval() {
    return 0;
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Override
  public HttpSessionContext getSessionContext() {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Override
  public Object getValue(final String s) {
    return null;
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Override
  public String[] getValueNames() {
    return new String[0];
  }

  @Override
  public void invalidate() {
  }

  @Override
  public boolean isNew() {
    return false;
  }

  /**
   * @deprecated
   */
  @Deprecated
  @Override
  public void putValue(final String s, final Object o) {
  }

  @Override
  public void removeAttribute(final String s) {
    attributes.remove(s);
  }

  @Override
  public void removeValue(final String s) {
  }

  @Override
  public void setAttribute(final String s, final Object o) {
    attributes.put(s, o);
  }

  @Override
  public void setMaxInactiveInterval(final int i) {
  }

  @Override
  public ServletContext getServletContext() {
    return null;
  }
}
