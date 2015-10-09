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

import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

public class TobagoResponseWriterWrapper extends TobagoResponseWriter {

  private ResponseWriter responseWriter;

  public TobagoResponseWriterWrapper(final ResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  public void startElement(final String name, final UIComponent component) throws IOException {
    responseWriter.startElement(name, component);
  }

  public void endElement(final String name) throws IOException {
    responseWriter.endElement(name);
  }


  public void write(final String string) throws IOException {
    responseWriter.write(string);
  }

  public void writeComment(final Object comment) throws IOException {
    responseWriter.writeComment(comment);
  }

  public ResponseWriter cloneWithWriter(final Writer writer) {
    return responseWriter.cloneWithWriter(writer);
  }

  @Deprecated
  public void writeAttribute(final String name, final Object value, final String property) throws IOException {
    responseWriter.writeAttribute(name, value, property);
  }

  @Deprecated
  public void writeText(final Object text, final String property) throws IOException {
    responseWriter.writeText(text, property);
  }

  public void flush() throws IOException {
    responseWriter.flush();
  }

  public void writeAttribute(final MarkupLanguageAttributes name, final String value, final boolean escape)
      throws IOException {
    responseWriter.writeAttribute(name.getValue(), value, null);
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

  public void writeURIAttribute(final String name, final Object value, final String property) throws IOException {
    responseWriter.writeURIAttribute(name, value, property);
  }

  @Override
  public void writeURIAttribute(MarkupLanguageAttributes name, String string) throws IOException {
    responseWriter.writeURIAttribute(name.getValue(), string, null);
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
}
