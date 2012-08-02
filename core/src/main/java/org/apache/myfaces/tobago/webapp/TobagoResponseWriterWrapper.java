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

package org.apache.myfaces.tobago.webapp;

import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

/*
 * Date: May 12, 2007
 * Time: 8:20:51 PM
 */
public class TobagoResponseWriterWrapper extends TobagoResponseWriter {
  private ResponseWriter responseWriter;

  public TobagoResponseWriterWrapper(ResponseWriter responseWriter) {
    this.responseWriter = responseWriter;
  }

  public void startElement(String name, UIComponent component) throws IOException {
    responseWriter.startElement(name, component);
  }

  public void endElement(String name) throws IOException {
    responseWriter.endElement(name);
  }

  public void write(String string) throws IOException {
    responseWriter.write(string);
  }

  public void writeComment(Object comment) throws IOException {
    responseWriter.writeComment(comment);
  }

  public ResponseWriter cloneWithWriter(Writer writer) {
    return responseWriter.cloneWithWriter(writer);
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
    responseWriter.writeAttribute(name, value, null);
  }

  public void writeClassAttribute() throws IOException {
    responseWriter.writeAttribute(HtmlAttributes.CLASS, null, ATTR_STYLE_CLASS);
  }

  public void writeStyleAttribute() throws IOException {
    responseWriter.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);
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
}
