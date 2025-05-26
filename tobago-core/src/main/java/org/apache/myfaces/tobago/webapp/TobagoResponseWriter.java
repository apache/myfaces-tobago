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

import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlTypes;
import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;

import jakarta.faces.application.ProjectStage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * <p> This provides an alternative ResponseWriter interfaces, which allows optimizations. E.g. some attributes needed
 * to to be escaped. </p>
 */
public abstract class TobagoResponseWriter extends ResponseWriter {

  // same as in ResponseWriter

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Deprecated
  @Override
  public abstract void startElement(String name, UIComponent component) throws IOException;

  public abstract void startElement(HtmlElements name, UIComponent component) throws IOException;

  public abstract void startElement(HtmlElements name) throws IOException;

  /**
   * @deprecated This should not be called directly via this interface. A specific method is available that might
   * be more suitable.
   */
  @Deprecated
  @Override
  public abstract void endElement(String name) throws IOException;

  public abstract void endElement(HtmlElements name) throws IOException;

  @Override
  public abstract void write(String string) throws IOException;

  @Override
  public abstract void writeComment(Object comment) throws IOException;

  @Override
  public abstract ResponseWriter cloneWithWriter(Writer writer);

  /**
   * @deprecated This should not be called directly via this interface. A specific method is available that might
   * be more suitable.
   */
  @Override
  @Deprecated(since = "1.0.11", forRemoval = true)
  public abstract void writeAttribute(String name, Object value, String property) throws IOException;

  /**
   * @deprecated This should not be called directly via this interface. A specific method is available that might
   * be more suitable.
   */
  @Override
  @Deprecated(since = "1.0.11", forRemoval = true)
  public abstract void writeURIAttribute(String name, Object value, String property) throws IOException;

  /**
   * @deprecated This should not be called directly via this interface. A specific method is available that might
   * be more suitable.
   */
  @Override
  @Deprecated
  public abstract void writeText(Object text, String property) throws IOException;

  @Override
  public abstract void flush() throws IOException;

  // others (not from ResponseWriter)

  /**
   * Writes a string attribute. The renderer may set escape=false to switch of escaping of the string, if it is not
   * necessary.
   */
  public abstract void writeAttribute(MarkupLanguageAttributes name, String string, boolean escape) throws IOException;

  public abstract void writeAttribute(MarkupLanguageAttributes name, HtmlTypes type) throws IOException;

  /**
   * Writes a string attribute URL encoded.
   */
  public abstract void writeURIAttribute(MarkupLanguageAttributes name, String string) throws IOException;

  /**
   * Writes a boolean attribute. The value will not escaped.
   */
  public void writeAttribute(final MarkupLanguageAttributes name, final boolean on) throws IOException {
    if (on) {
      writeAttribute(name, name.getValue(), false);
    }
  }

  /**
   * Writes a {@link Integer} attribute, if the value is not {@code null}. The value will not be escaped.
   */
  public void writeAttribute(final MarkupLanguageAttributes name, final Integer number) throws IOException {
    if (number != null) {
      writeAttribute(name, Integer.toString(number), false);
    }
  }

  /**
   * Writes a {@link Long} attribute, if the value is not {@code null}. The value will not be escaped.
   */
  public void writeAttribute(final MarkupLanguageAttributes name, final Long number) throws IOException {
    if (number != null) {
      writeAttribute(name, Long.toString(number), false);
    }
  }

  /**
   * Write the id attribute. The value will not escaped.
   */
  public void writeIdAttribute(final String id) throws IOException {
    writeAttribute(HtmlAttributes.ID, id, false);
  }

  /**
   * Write the name attribute. The value will not escaped.
   */
  public void writeNameAttribute(final String name) throws IOException {
    writeAttribute(HtmlAttributes.NAME, name, false);
  }

  /**
   * Write the command map data attribute.
   *
   * @deprecated Use {@link
   * org.apache.myfaces.tobago.renderkit.RendererBase#encodeBehavior} instead.
   */
  @Deprecated(since = "5.0.0", forRemoval = true)
  public void writeCommandMapAttribute(final String map) throws IOException {
    if (!FacesContext.getCurrentInstance().isProjectStage(ProjectStage.Production)) {
      Deprecation.LOG.error("No longer supported. Data: {}", map);
    }
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public void writeClassAttribute(final CssItem... first) throws IOException {
    writeClassAttribute(null, null, null, null, null, first);
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public void writeClassAttribute(final CssItem first, final CssItem[] second, final CssItem... third)
      throws IOException {
    writeClassAttribute(first, second, null, null, null, third);
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public void writeClassAttribute(
      final CssItem first, final CssItem[] second, final CssItem[] third,
      final CssItem... fourth) throws IOException {
    writeClassAttribute(first, second, third, null, null, fourth);
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public void writeClassAttribute(
      final CssItem first, final CssItem[] second, final CssItem[] third,
      final CssItem[] fourth, final CssItem... fifth) throws IOException {
    writeClassAttribute(first, second, third, fourth, null, fifth);
  }

  public void writeClassAttribute(
      final CssItem first, final CssItem[] second, final CssItem[] third,
      final CssItem[] fourth, final CssItem[] fifth, final CssItem... sixth)
      throws IOException {
    final StringBuilder builder = new StringBuilder();
    boolean render = false;
    if (first != null) {
      builder.append(first.getName());
      builder.append(' ');
      render = true;
    }
    if (second != null) {
      render |= writeCssItem(builder, second);
    }
    if (third != null) {
      render |= writeCssItem(builder, third);
    }
    if (fourth != null) {
      render |= writeCssItem(builder, fourth);
    }
    if (fifth != null) {
      render |= writeCssItem(builder, fifth);
    }
    if (sixth != null) {
      render |= writeCssItem(builder, sixth);
    }
    if (render) {
      writeAttribute(HtmlAttributes.CLASS, builder.deleteCharAt(builder.length() - 1).toString(), false);
    }
  }

  private boolean writeCssItem(final StringBuilder builder, final CssItem... cssItems) {
    boolean render = false;
    for (final CssItem cssItem : cssItems) {
      if (cssItem != null && !"".equals(cssItem.getName())) {
        builder.append(cssItem.getName());
        builder.append(' ');
        render = true;
      }
    }
    return render;
  }

  /**
   * Write text content. The text will be escaped.
   */
  public void writeText(final String text) throws IOException {
    writeText(text, null);
  }

  public String getContentTypeWithCharSet() {
    String contentType = getContentType();
    if (contentType == null) {
      contentType = "text/html";
    }
    String characterEncoding = getCharacterEncoding();
    if (characterEncoding == null) {
      characterEncoding = StandardCharsets.UTF_8.name();
    }

    return contentType + "; charset=" + characterEncoding;
  }

  @Override
  public void startCDATA() throws IOException {
    write("<![CDATA[");
  }

  @Override
  public void endCDATA() throws IOException {
    write("]]>");
  }

//  protected abstract void writeNewline() throws IOException;
}
