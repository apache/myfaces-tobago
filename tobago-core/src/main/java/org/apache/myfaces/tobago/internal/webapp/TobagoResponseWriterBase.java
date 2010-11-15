package org.apache.myfaces.tobago.internal.webapp;

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

import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;
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

  private UIComponent component;

  private boolean startStillOpen;

  private final Writer writer;

  private final String contentType;

  private final String characterEncoding;

  protected TobagoResponseWriterBase(Writer writer, String contentType, String characterEncoding) {
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

  protected final void setComponent(UIComponent component) {
    this.component = component;
  }

  protected final boolean isStartStillOpen() {
    return startStillOpen;
  }

  protected final void setStartStillOpen(boolean startStillOpen) {
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
  public void write(String string) throws IOException {
    writeInternal(writer, string);
  }

  protected final void writeInternal(Writer writer, String string) throws IOException {
    closeOpenTag();
    writer.write(string);
  }

  @Override
  public void write(int i) throws IOException {
    closeOpenTag();
    writer.write(i);
  }

  @Override
  public void write(char[] chars) throws IOException {
    closeOpenTag();
    writer.write(chars);
  }

  @Override
  public void write(String string, int i, int i1) throws IOException {
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

  protected void startElementInternal(Writer writer, String name, UIComponent currentComponent)
      throws IOException {
    this.component = currentComponent;
//    closeOpenTag();
    if (startStillOpen) {
      writer.write("\n>");
    }
    writer.write("<");
    writer.write(name);
    startStillOpen = true;
  }

  public void endElement(final String name) throws IOException {
    endElementInternal(writer, name);
  }

  public void writeComment(final Object obj) throws IOException {
    closeOpenTag();
    String comment = obj.toString();
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
    while (stackTrace[i].getClassName().equals(this.getClass().getName())) {
      i++;
    }
    return stackTrace[i].toString();
  }

  public void writeURIAttribute(final String s, final Object obj, final String s1)
      throws IOException {
    LOG.error("Not implemented yet!");
  }

// interface TobagoResponseWriter //////////////////////////////////////////////////////////////////////////////////

  public void writeAttribute(final String name, final String value, final boolean escape)
      throws IOException {
    writeAttributeInternal(writer, name, value, escape);
  }

  @Override
  @Deprecated
  protected String getStyleClasses() {
    if (component == null) {
      return null;
    }
    StyleClasses clazz = (StyleClasses) component.getAttributes().get(Attributes.STYLE_CLASS);
    if (clazz != null) {
      return clazz.toString();
    }
    return null;
  }

  /**
   * @deprecated since Tobago 1.5.0
   */
  @Deprecated
  public void writeClassAttribute() throws IOException {
    Deprecation.LOG.warn("Please use writeClassAttribute(org.apache.myfaces.tobago.renderkit.css.Classes)");
    StyleClasses clazz = (StyleClasses) component.getAttributes().get(Attributes.STYLE_CLASS);
    if (clazz != null) {
      writeAttribute(HtmlAttributes.CLASS, clazz.toString(), false);
    }
  }


  protected void endElementInternal(Writer writer, String name) throws IOException {
    if (EMPTY_TAG.contains(name)) {
      closeEmptyTag();
    } else {
      if (startStillOpen) {
        writer.write("\n>");
      }
      writer.write("</");
      writer.write(name);
//      writer.write("\n>"); // FIXME: this makes problems with Tidy
      writer.write(">");
    }
    startStillOpen = false;
  }
  protected abstract void closeEmptyTag() throws IOException;

  protected void writeAttributeInternal(Writer writer, String name, String value, boolean escape)
      throws IOException {
    if (!startStillOpen) {
      String trace = getCallingClassStackTraceElementString();
      String error = "Cannot write attribute when start-tag not open. "
          + "name = '" + name + "' "
          + "value = '" + value + "' "
          + trace.substring(trace.indexOf('('));
      LOG.error(error);
      throw new IllegalStateException(error);
    }

    if (value != null) {
      writer.write(' ');
      writer.write(name);
      writer.write("=\"");
      writerAttributeValue(value, escape);
      writer.write('\"');
    }
  }
  protected abstract void writerAttributeValue(String value, boolean escape) throws IOException;


}

