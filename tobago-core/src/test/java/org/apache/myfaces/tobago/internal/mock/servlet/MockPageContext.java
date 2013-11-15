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

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class MockPageContext extends PageContext {

  private ServletRequest request = null;
  private JspWriter out = new MockJspWriter(new PrintWriter(System.out));
  private ServletContext servletContext = new MockServletContext();
  private Map attributes = new HashMap();


  public MockPageContext() {
    request = new MockHttpServletRequest();
  }

  public MockPageContext(final ServletRequest servletRequest) {
    this.request = servletRequest;
  }

  public Object findAttribute(final String s) {
    return null;
  }

  public void forward(final String s) throws ServletException, IOException {
  }

  public Object getAttribute(final String s) {
    return attributes.get(s);
  }

  public Object getAttribute(final String name, final int scope) {
    switch (scope) {
      case PageContext.REQUEST_SCOPE:
        return getRequest().getAttribute(name);
      // other not supported
      default:
        return null;
    }
  }

  public Enumeration getAttributeNamesInScope(final int i) {
    return null;
  }

  public int getAttributesScope(final String s) {
    return 0;
  }

  public Exception getException() {
    return null;
  }

  public JspWriter getOut() {
    return out;
  }

  public Object getPage() {
    return null;
  }

  public ServletRequest getRequest() {
    return request;
  }

  public ServletResponse getResponse() {
    return new MockHttpServletResponse();
  }

  public ServletConfig getServletConfig() {
    return null;
  }

  public ServletContext getServletContext() {
    return servletContext;
  }

  public HttpSession getSession() {
    return null;
  }

  public void handlePageException(final Exception e) throws ServletException, IOException {
  }

  public void handlePageException(final Throwable e) {
  }

  public void include(final String s) throws ServletException, IOException {
  }

  public void initialize(
      final Servlet servlet, final ServletRequest request, final ServletResponse response, final String s,
      final boolean b, final int i,
      final boolean b1) throws IOException, IllegalStateException, IllegalArgumentException {
  }

  public void release() {
  }

  public void removeAttribute(final String s) {
    attributes.remove(s);
  }

  public void removeAttribute(final String s, final int i) {
  }

  public void setAttribute(final String s, final Object o) {
    attributes.put(s, o);
  }

  public void setAttribute(final String s, final Object o, final int i) {
    switch (i) {
      case PageContext.REQUEST_SCOPE:
        getRequest().setAttribute(s, o);
        break;
      default:
        // other not supported
    }
  }

  public void include(final String reference, final boolean b) throws ServletException, IOException {
  }

  public ExpressionEvaluator getExpressionEvaluator() {
    return null;
  }

  public VariableResolver getVariableResolver() {
    return null;
  }

  public ELContext getELContext() {
    return null;
  }
}
