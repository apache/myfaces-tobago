package org.apache.myfaces.tobago.mock.faces;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

public class MockResponseWriter extends ResponseWriter {

  private Writer writer;
  private String encoding;

  public MockResponseWriter(Writer writer, String encoding) {
    this.writer = writer;
    this.encoding = encoding;
  }


  // ---------------------------------------------------------- Writer Methods


  public void close() throws IOException {
    writer.close();
  }


  public void flush() throws IOException {
    writer.flush();
  }


  public void write(char c) throws IOException {
    writer.write(c);
  }


  public void write(char[] cbuf, int off, int len) throws IOException {
    writer.write(cbuf, off, len);
  }


  public void write(int c) throws IOException {
    writer.write(c);
  }


  public void write(String s) throws IOException {
    writer.write(s);
  }


  public void write(String s, int off, int len) throws IOException {
    writer.write(s, off, len);
  }


  // --------------------------------------------------- ResponseWrter Methods

  public String getContentType() {
    return "text/html";
  }

  public String getCharacterEncoding() {
    return (this.encoding);
  }


  public void startDocument() throws IOException {
    throw new UnsupportedOperationException();
  }


  public void endDocument() throws IOException {
    throw new UnsupportedOperationException();
  }


  public void startElement(String name,
      UIComponent component) throws IOException {
    throw new UnsupportedOperationException();
  }


  public void endElement(String name) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void writeAttribute(String name, Object value,
      String componentPropertyName) throws IOException {
    throw new UnsupportedOperationException();
  }


  public void writeURIAttribute(String name, Object value, String componentPropertyName) throws IOException {
    throw new UnsupportedOperationException();
  }


  public void writeComment(Object comment) throws IOException {
    throw new UnsupportedOperationException();
  }


  public void writeText(Object text, String componentProperty) throws IOException {
    throw new UnsupportedOperationException();
  }

  public void writeText(char[] text, int off, int len) throws IOException {
    throw new UnsupportedOperationException();
  }


  public ResponseWriter cloneWithWriter(Writer writer) {
    throw new UnsupportedOperationException();
  }


}
