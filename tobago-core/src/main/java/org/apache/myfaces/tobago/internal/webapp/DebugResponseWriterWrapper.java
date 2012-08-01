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

package org.apache.myfaces.tobago.internal.webapp;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.EmptyStackException;
import java.util.Stack;

public class DebugResponseWriterWrapper extends TobagoResponseWriter {

  private Stack<String> stack = new Stack<String>();

  private static final Logger LOG = LoggerFactory.getLogger(DebugResponseWriterWrapper.class);

  private final TobagoResponseWriter responseWriter;

  public DebugResponseWriterWrapper(TobagoResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  public void write(String string) throws IOException {
    responseWriter.write(string);
  }

  public void writeComment(Object comment) throws IOException {
    String commentStr = comment.toString();
    if (commentStr.indexOf("--") > 0) {
      LOG.error("Comment must not contain the sequence '--', comment = '" + comment + "'.",
          new IllegalArgumentException());

      commentStr = StringUtils.replace(commentStr, "--", "++");
    }
    responseWriter.writeComment(commentStr);
  }

  public ResponseWriter cloneWithWriter(Writer writer) {
    return new DebugResponseWriterWrapper((TobagoResponseWriter) responseWriter.cloneWithWriter(writer));
  }

  @Deprecated
  public void writeAttribute(String name, Object value, String property) throws IOException {
    responseWriter.writeAttribute(name, value, property);
  }

  @Deprecated
  public void writeText(Object text, String property) throws IOException {
    responseWriter.writeText(text, property);
  }

  public void flush() throws IOException {
    responseWriter.flush();
  }

  public void writeAttribute(String name, String value, boolean escape) throws IOException {
    responseWriter.writeAttribute(name, value, escape);
  }

  @Override
  @Deprecated
  public String getStyleClasses() {
    return responseWriter.getStyleClasses();
  }

  /**
   * @deprecated since Tobago 1.5.0
   */
  @Deprecated
  public void writeClassAttribute() throws IOException {
    Deprecation.LOG.warn("Please use writeClassAttribute(org.apache.myfaces.tobago.renderkit.css.Classes)");
    responseWriter.writeAttribute(HtmlAttributes.CLASS, null, Attributes.STYLE_CLASS);
  }

  public String getContentType() {
    return responseWriter.getContentType();
  }

  public String getCharacterEncoding() {
    return responseWriter.getCharacterEncoding();
  }

  public void startDocument() throws IOException {
    responseWriter.startDocument();
  }

  public void endDocument() throws IOException {
    responseWriter.endDocument();
  }

  @Override
  public void writeJavascript(String script) throws IOException {
    responseWriter.writeJavascript(script);
  }

  @Override
  public void endJavascript() throws IOException {
    responseWriter.endJavascript();
  }

  @Override
  public void startJavascript() throws IOException {
    responseWriter.startJavascript();
  }

  public void writeURIAttribute(String name, Object value, String property) throws IOException {
    responseWriter.writeURIAttribute(name, value, property);
  }

  public void writeText(char[] text, int off, int len) throws IOException {
    responseWriter.writeText(text, off, len);
  }

  public void write(char[] chars, int i, int i1) throws IOException {
    responseWriter.write(chars, i, i1);
  }

  public void close() throws IOException {
    responseWriter.close();
  }

  @Override
  public void startElement(String name, UIComponent currentComponent)
      throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("start element: '" + name + "'");
    }
    stack.push(name);
    responseWriter.startElement(name, currentComponent);
  }

  @Override
  public void endElement(String name) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("end element: '" + name + "'");
    }
    String top = "";
    try {
      top = stack.pop();
    } catch (EmptyStackException e) {
      LOG.error("Failed to close element \"" + name + "\"!", e);
    }

    if (!top.equals(name)) {
      LOG.error("Element end with name='" + name + "' doesn't match with top element on the stack='" + top + "'.",
          new IllegalArgumentException());
    }
    responseWriter.endElement(name);
  }
}
