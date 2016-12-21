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
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;

public abstract class TobagoResponseWriterBase extends TobagoResponseWriter {

  private static final Logger LOG = LoggerFactory.getLogger(TobagoResponseWriterBase.class);

  protected static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

  protected static final char[] XML_VERSION_1_0_ENCODING_UTF_8_CHARS = XML_VERSION_1_0_ENCODING_UTF_8.toCharArray();

  private int i = 0;

  private int inlineStack = 0;

  private UIComponent component;

  private boolean startStillOpen;

  private final Writer writer;

  private final String contentType;

  private final String characterEncoding;

  private final boolean ajax;

  protected TobagoResponseWriterBase(final Writer writer, final String contentType, final String characterEncoding) {
    this.writer = writer;
    this.contentType = contentType;
    this.characterEncoding = characterEncoding != null ? characterEncoding : "UTF-8";
    this.ajax = FacesContext.getCurrentInstance().getPartialViewContext().isPartialRequest();
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
        LOG.warn("Don't know what to do! "
            + "Property defined, but no component to get a value. (value=null, property='" + property + "') "
            + trace.substring(trace.indexOf('(')));
        return null;
      }
    } else {
      final String trace = getCallingClassStackTraceElementString();
      LOG.warn("Don't know what to do! "
          + "No value and no property defined. (value=null, property=null)"
          + trace.substring(trace.indexOf('(')));
      return null;
    }
  }

  @Override
  public void write(final char[] cbuf, final int off, final int len)
      throws IOException {
    writer.write(cbuf, off, len);
  }

  @Override
  public void write(final String string) throws IOException {
    writeInternal(writer, string);
  }

  protected final void writeInternal(final Writer writer, final String string) throws IOException {
    closeOpenTag();
    writer.write(string);
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

  @Override
  public void close() throws IOException {
    closeOpenTag();
    writer.close();
  }

  @Override
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
      writer.write(">");
      startStillOpen = false;
    }
  }

  @Override
  public void startDocument() throws IOException {
    // nothing to do
  }

  @Override
  public void endDocument() throws IOException {
    // nothing to do
  }

  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public String getCharacterEncoding() {
    return characterEncoding;
  }

  @Override
  public void startElement(final String name, final UIComponent currentComponent) throws IOException {
    final boolean inline = HtmlElements.isInline(name);
    if (inline) {
      inlineStack++;
    }
    this.component = currentComponent;
    startElementInternal(writer, name, HtmlElements.isInline(name));
  }

  @Override
  public void startElement(final HtmlElements name) throws IOException {
    final boolean inline = name.isInline();
    if (inline) {
      inlineStack++;
    }
    startElementInternal(writer, name.getValue(), name.isInline());
    if (!name.isVoid()) {
      i++;
    }
  }

  protected void startElementInternal(final Writer writer, final String name, final boolean inline)
      throws IOException {
//    closeOpenTag();
    if (startStillOpen) {
      writer.write(">");
    }
    if (!ajax && inlineStack <= 1) {
      writer.write("\n");
      writer.write(StringUtils.repeat("  ", i));
    }
    writer.write("<");
    writer.write(name);
    startStillOpen = true;
  }

  @Override
  public void endElement(final String name) throws IOException {
    final boolean inline = HtmlElements.isInline(name);
    if (HtmlElements.isVoid(name)) {
      closeEmptyTag();
    } else {
      endElementInternal(writer, name, inline);
    }
    startStillOpen = false;
    if (inline) {
      inlineStack--;
      assert inlineStack >= 0;
    }
  }

  @Override
  public void endElement(final HtmlElements name) throws IOException {
    final boolean inline = name.isInline();
    if (name.isVoid()) {
      closeEmptyTag();
    } else {
      if (!name.isVoid()) {
        i--;
      }
      endElementInternal(writer, name.getValue(), inline);
    }
    startStillOpen = false;
    if (inline) {
      inlineStack--;
      assert inlineStack >= 0;
    }
  }

  @Override
  public void writeComment(final Object obj) throws IOException {
    closeOpenTag();
    final String comment = obj.toString();
    if (!ajax) {
      writer.write("\n");
      writer.write(StringUtils.repeat("  ", i));
    }
    write("<!--");
    write(comment);
    write("-->");
  }

  @Override
  @Deprecated
  public void writeAttribute(final String name, final Object value, final String property)
      throws IOException {

    final String attribute = findValue(value, property);
    writeAttribute(new MarkupLanguageAttributes() {
      @Override
      public String getValue() {
          return name;
      }
    }, attribute, true);
  }

  protected final String getCallingClassStackTraceElementString() {
    final StackTraceElement[] stackTrace = new Exception().getStackTrace();
    int i = 1;
    while (stackTrace[i].getClassName().contains("ResponseWriter")) {
      i++;
    }
    return stackTrace[i].toString();
  }

  @Override
  public void writeURIAttribute(final String name, final Object value, final String property)
      throws IOException {
    if (value != null) {
      final URI uri = URI.create(value.toString());
      writeAttribute(name, uri.toASCIIString(), property);
    }
  }

// interface TobagoResponseWriter //////////////////////////////////////////////////////////////////////////////////

  @Override
  public void writeAttribute(final MarkupLanguageAttributes name, final String value, final boolean escape)
      throws IOException {
    writeAttributeInternal(writer, name, value, escape);
  }

  @Override
  public void writeAttribute(final MarkupLanguageAttributes name, final HtmlTypes types) throws IOException {
    writeAttributeInternal(writer, name, types.getValue(), false);
  }

  @Override
  public void writeURIAttribute(final MarkupLanguageAttributes name, final String value)
      throws IOException {
    if (value != null) {
      final URI uri = URI.create(value);
      writeAttribute(name, uri.toASCIIString(), true);
    }
  }

  protected void endElementInternal(final Writer writer, final String name, final boolean inline) throws IOException {
    if (startStillOpen) {
      writer.write(">");
    }
    if (inline || ajax) {
      writer.write("</");
    } else {
      writer.write("\n" + StringUtils.repeat("  ", i) + "</");
    }
    writer.write(name);
    writer.write(">");
  }

  protected abstract void closeEmptyTag() throws IOException;

  protected void writeAttributeInternal(
      final Writer writer, final MarkupLanguageAttributes name, final String value, final boolean escape)
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
      writer.write(' ');
      writer.write(name.getValue());
      writer.write("='");
      writerAttributeValue(value, escape);
      writer.write('\'');
    }
  }
  protected abstract void writerAttributeValue(String value, boolean escape) throws IOException;


}

