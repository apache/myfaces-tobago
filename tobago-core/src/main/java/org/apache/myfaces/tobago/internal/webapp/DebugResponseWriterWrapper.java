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

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlTypes;
import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;
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

  private Stack<Object> stack = new Stack<Object>();

  private static final Logger LOG = LoggerFactory.getLogger(DebugResponseWriterWrapper.class);

  private final TobagoResponseWriter responseWriter;

  public DebugResponseWriterWrapper(final TobagoResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  public void write(final String string) throws IOException {
    responseWriter.write(string);
  }

  public void writeComment(final Object comment) throws IOException {
    String commentStr = comment.toString();
    if (commentStr.indexOf("--") > 0) {
      LOG.error("Comment must not contain the sequence '--', comment = '" + comment + "'.",
          new IllegalArgumentException());

      commentStr = StringUtils.replace(commentStr, "--", "++");
    }
    responseWriter.writeComment(commentStr);
  }

  public ResponseWriter cloneWithWriter(final Writer writer) {
    return new DebugResponseWriterWrapper((TobagoResponseWriter) responseWriter.cloneWithWriter(writer));
  }

  @Deprecated
  public void writeAttribute(final String name, final Object value, final String property) throws IOException {
    responseWriter.writeAttribute(name, value, property);
  }

  @Deprecated
  public void writeText(final Object text, final String property) throws IOException {
    responseWriter.writeText(text, property);
  }

  @Override
  public void flush() throws IOException {
    responseWriter.flush();
  }

  @Override
  public void writeAttribute(final MarkupLanguageAttributes name, final String value, final boolean escape)
      throws IOException {
    responseWriter.writeAttribute(name, value, escape);
  }

  @Override
  public void writeAttribute(final MarkupLanguageAttributes name, final HtmlTypes types) throws IOException {
    responseWriter.writeAttribute(name, types);
  }

  @Override
  public void writeURIAttribute(MarkupLanguageAttributes name, String string) throws IOException {
    responseWriter.writeURIAttribute(name, string);
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

  /**
   * @deprecated Should not be used, because it conflicts with CSP.
   */
  @Deprecated
  @Override
  public void writeJavascript(final String script) throws IOException {
    responseWriter.writeJavascript(script);
  }

  /**
   * @deprecated Should not be used, because it conflicts with CSP.
   */
  @Deprecated
  @Override
  public void endJavascript() throws IOException {
    responseWriter.endJavascript();
  }

  /**
   * @deprecated Should not be used, because it conflicts with CSP.
   */
  @Deprecated
  @Override
  public void startJavascript() throws IOException {
    responseWriter.startJavascript();
  }

  public void writeURIAttribute(final String name, final Object value, final String property) throws IOException {
    responseWriter.writeURIAttribute(name, value, property);
  }

  public void writeText(final char[] text, final int off, final int len) throws IOException {
    responseWriter.writeText(text, off, len);
  }

  public void write(final char[] chars, final int i, final int i1) throws IOException {
    responseWriter.write(chars, i, i1);
  }

  public void close() throws IOException {
    responseWriter.close();
  }

  @Override
  public void startElement(final String name, final UIComponent currentComponent) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("start element: '" + name + "'");
    }
    stack.push(name);
    responseWriter.startElement(name, currentComponent);
  }

  @Override
  public void startElement(final HtmlElements name) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("start element: '" + name + "'");
    }
    stack.push(name);
    responseWriter.startElement(name);
  }

  @Override
  public void endElement(final String name) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("end element: '" + name + "'");
    }
    Object top;
    try {
      top = stack.pop();
    } catch (final EmptyStackException e) {
      LOG.error("Failed to close element \"" + name + "\"!", e);
      top = "*** failure ***";
    }

    if (!top.equals(name)) {
      LOG.error("Element end with name='" + name + "' doesn't match with top element on the stack='" + top + "'.",
          new IllegalArgumentException());
    }
    responseWriter.endElement(name);
  }

  @Override
  public void endElement(final HtmlElements name) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("end element: '" + name + "'");
    }
    Object top;
    try {
      top = stack.pop();
    } catch (final EmptyStackException e) {
      LOG.error("Failed to close element \"" + name + "\"!", e);
      top = "*** failure ***";
    }

    if (!top.equals(name)) {
      LOG.error("Element end with name='" + name + "' doesn't match with top element on the stack='" + top + "'.",
          new IllegalArgumentException());
    }
    responseWriter.endElement(name);
  }
}
