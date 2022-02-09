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

import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class TobagoResponseWriterBase extends TobagoResponseWriter {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoResponseWriterBase.class);

  protected static final Set<String> EMPTY_TAG = new HashSet<String>(Arrays.asList(
      HtmlElements.BR,
      HtmlElements.AREA,
      HtmlElements.LINK,
      HtmlElements.IMG,
      HtmlElements.PARAM,
      HtmlElements.HR,
      HtmlElements.INPUT,
      HtmlElements.COL,
      HtmlElements.BASE,
      HtmlElements.META));

  /**
   * @deprecated Since Tobago 1.5.3
   */
  @Deprecated
  public static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

  /**
   * @deprecated Since Tobago 1.5.3
   */
  @Deprecated
  public static final int XML_VERSION_1_0_ENCODING_UTF_8_LENGTH = XML_VERSION_1_0_ENCODING_UTF_8.length();

  protected static final char[] XML_VERSION_1_0_ENCODING_UTF_8_CHARS = XML_VERSION_1_0_ENCODING_UTF_8.toCharArray();

  private UIComponent component;

  private boolean startStillOpen;

  private final Writer writer;

  private final String contentType;

  private final String characterEncoding;

  protected TobagoResponseWriterBase(final Writer writer, final String contentType, final String characterEncoding) {
    this.writer = writer;
    this.contentType = contentType;
    this.characterEncoding = characterEncoding != null ? characterEncoding : "UTF-8";
  }

  protected final Writer getWriter() {
    return writer;
  }

  protected final UIComponent getComponent() {
    return component;
  }

  protected final void setComponent(final UIComponent component) {
    this.component = component;
  }

  protected final boolean isStartStillOpen() {
    return startStillOpen;
  }

  protected final void setStartStillOpen(final boolean startStillOpen) {
    this.startStillOpen = startStillOpen;
  }

  protected final String findValue(final Object value, final String property) {
    if (value != null) {
      return value instanceof String ? (String) value : value.toString();
    } else if (property != null) {
      if (component != null) {
        final Object object = component.getAttributes().get(property);
        if (object != null) {
          return object instanceof String ? (String) object : object.toString();
        } else {
          return null;
        }
      } else {
        final String trace = getCallingClassStackTraceElementString();
        LOG.error("Don't know what to do! "
            + "Property defined, but no component to get a value. "
            + trace.substring(trace.indexOf('(')));
        LOG.error("value = 'null'");
        LOG.error("property = '" + property + "'");
        return null;
      }
    } else {
      final String trace = getCallingClassStackTraceElementString();
      LOG.error("Don't know what to do! "
          + "No value and no property defined. "
          + trace.substring(trace.indexOf('(')));
      LOG.error("value = 'null'");
      LOG.error("property = 'null'");
      return null;
    }
  }

  public void write(final char[] cbuf, final int off, final int len)
      throws IOException {
    writer.write(cbuf, off, len);
  }

  @Override
  public void write(final String string) throws IOException {
    writeInternal(writer, string);
  }

  protected final void writeInternal(final Writer writerParameter, final String string) throws IOException {
    closeOpenTag();
    writerParameter.write(string);
  }

  @Override
  public void write(final int i) throws IOException {
    closeOpenTag();
    writer.write(i);
  }

  @Override
  public void write(final char[] chars) throws IOException {
    closeOpenTag();
    writer.write(chars);
  }

  @Override
  public void write(final String string, final int i, final int i1) throws IOException {
    closeOpenTag();
    writer.write(string, i, i1);
  }

  public void close() throws IOException {
    closeOpenTag();
    writer.close();
  }

  public void flush() throws IOException {
    /*
    From the api:
    Flush any ouput buffered by the output method to the underlying Writer or OutputStream.
    This method will not flush the underlying Writer or OutputStream;
    it simply clears any values buffered by this ResponseWriter.
     */
    closeOpenTag();
  }


  protected void closeOpenTag() throws IOException {
    if (startStillOpen) {
      writer.write("\n>");
      startStillOpen = false;
    }
  }

  public void startDocument() throws IOException {
    // nothing to do
  }

  public void endDocument() throws IOException {
    // nothing to do
  }

  public String getContentType() {
    return contentType;
  }

  public String getCharacterEncoding() {
    return characterEncoding;
  }

  public void startElement(final String name, final UIComponent currentComponent)
      throws IOException {
    startElementInternal(writer, name, currentComponent);
  }

  protected void startElementInternal(
      final Writer writerParameter, final String name, final UIComponent currentComponent)
      throws IOException {
    this.component = currentComponent;
//    closeOpenTag();
    if (startStillOpen) {
      writerParameter.write("\n>");
    }
    writerParameter.write("<");
    writerParameter.write(name);
    startStillOpen = true;
  }

  public void endElement(final String name) throws IOException {
    endElementInternal(writer, name);
  }

  public void writeComment(final Object obj) throws IOException {
    closeOpenTag();
    final String comment = obj.toString();
    write("<!--");
    write(comment);
    write("-->");
  }


  public void writeAttribute(final String name, final Object value, final String property)
      throws IOException {

    final String attribute = findValue(value, property);
    writeAttribute(name, attribute, true);
  }

  protected final String getCallingClassStackTraceElementString() {
    final StackTraceElement[] stackTrace = new Exception().getStackTrace();
    int i = 1;
    while (stackTrace[i].getClassName().contains("TobagoResponseWriter")) {
      i++;
    }
    return stackTrace[i].toString();
  }

  public void writeURIAttribute(final String name, final Object value, final String property)
      throws IOException {
    if (value != null) {
      final URI uri = URI.create(value.toString());
      writeAttribute(name, uri.toASCIIString(), property);
    }
  }

// interface TobagoResponseWriter //////////////////////////////////////////////////////////////////////////////////

  public void writeAttribute(final String name, final String value, final boolean escape)
      throws IOException {
    writeAttributeInternal(writer, name, value, escape);
  }

  /**
   * @deprecated Since Tobago 2.0.0
   */
  @Override
  @Deprecated
  public String getStyleClasses() {
    if (component == null) {
      return null;
    }
    Deprecation.LOG.error("Can't get style classes.");
    return null;
  }

  /**
   * @deprecated since Tobago 1.5.0
   */
  @Deprecated
  public void writeClassAttribute() throws IOException {
    Deprecation.LOG.error("Please use writeClassAttribute(org.apache.myfaces.tobago.renderkit.css.Classes)");
  }


  protected void endElementInternal(final Writer writerParameter, final String name) throws IOException {
    if (EMPTY_TAG.contains(name)) {
      closeEmptyTag();
    } else {
      if (startStillOpen) {
        writerParameter.write("\n>");
      }
      writerParameter.write("</");
      writerParameter.write(name);
//      writer.write("\n>"); // FIXME: this makes problems with Tidy
      writerParameter.write(">");
    }
    startStillOpen = false;
  }

  protected abstract void closeEmptyTag() throws IOException;

  protected void writeAttributeInternal(
      final Writer writerParameter, final String name, final String value, final boolean escape)
      throws IOException {
    if (!startStillOpen) {
      final String trace = getCallingClassStackTraceElementString();
      final String error = "Cannot write attribute when start-tag not open. "
          + "name = '" + name + "' "
          + "value = '" + value + "' "
          + trace.substring(trace.indexOf('('));
      LOG.error(error);
      throw new IllegalStateException(error);
    }

    if (value != null) {
      writerParameter.write(' ');
      writerParameter.write(name);
      writerParameter.write("='");
      writerAttributeValue(value, escape);
      writerParameter.write('\'');
    }
  }

  protected abstract void writerAttributeValue(String value, boolean escape) throws IOException;


}

