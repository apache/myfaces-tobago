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

import org.apache.myfaces.tobago.internal.util.HtmlWriterHelper;
import org.apache.myfaces.tobago.internal.util.WriterHelper;

import jakarta.faces.context.ResponseWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Arrays;

public class HtmlResponseWriter extends TobagoResponseWriterBase {

  private static final String HTML_DOCTYPE = "<!DOCTYPE html>";

  private final WriterHelper helper;

  /**
   * @deprecated since 4.3.0
   */
  @Deprecated
  public HtmlResponseWriter(
      final Writer writer, final String contentType, final String characterEncoding) {
    this(writer, contentType, Charset.forName(characterEncoding));
  }

  public HtmlResponseWriter(
      final Writer writer, final String contentType, final Charset charset) {
    super(writer, contentType, charset);
    this.helper = new HtmlWriterHelper(writer, charset);
  }

  @Override
  public void write(final String string) throws IOException {
    writeInternal(getWriter(), string);
  }

  public final WriterHelper getHelper() {
    return helper;
  }

  @Override
  public void writeText(final Object text, final String property)
      throws IOException {
    closeOpenTag();
    final String value = findValue(text, property);
    helper.writeText(value);
  }

  @Override
  public void writeText(final char[] text, final int offset, final int length)
      throws IOException {
    closeOpenTag();
    helper.writeText(text, offset, length);
  }

  @Override
  public void write(final char[] cbuf, final int off, final int len) throws IOException {
    // Related to http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-696
    if (Arrays.equals(cbuf, XML_VERSION_1_0_ENCODING_UTF_8_CHARS)) {
      // drop
    } else {
      super.write(cbuf, off, len);
    }
  }

  @Override
  protected void closeEmptyTag() throws IOException {
    getWriter().write(">");
  }

  @Override
  protected void writerAttributeValue(final String value, final boolean escape) throws IOException {
    if (escape) {
      helper.writeAttributeValue(value);
    } else {
      getWriter().write(value);
    }
  }

  @Override
  public ResponseWriter cloneWithWriter(final Writer originalWriter) {
    return new HtmlResponseWriter(originalWriter, getContentType(), Charset.forName(getCharacterEncoding()));
  }

  @Override
  public void startDocument() throws IOException {
    getWriter().write(HTML_DOCTYPE);
    getWriter().write('\n');
  }

  @Override
  public void endElement(final String name) throws IOException {
    super.endElement(name);
  }

  @Override
  public void endDocument() throws IOException {
  }
}
