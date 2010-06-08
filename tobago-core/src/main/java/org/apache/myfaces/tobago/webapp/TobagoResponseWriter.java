package org.apache.myfaces.tobago.webapp;

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

import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * This provides an alternative ResponseWriter interfaces, which allows optimizations.
 * E. g. some attributes needed to to be escaped.
 * <p/>
 * Date: 08.05.2007 13:51:43
 */
public abstract class TobagoResponseWriter extends ResponseWriter {

  // same as in ResponseWriter

  @Override
  public abstract void startElement(String name, UIComponent component) throws IOException;

  /**
   * @deprecated Use {@link #startElement(String, UIComponent) startElement(name, null)} instead.
   */
  @Deprecated
  public void startElement(String name) throws IOException {
    startElement(name, null);
  }

  @Override
  public abstract void endElement(String name) throws IOException;
    
  public abstract void write(String string) throws IOException;

  @Override
  public abstract void writeComment(Object comment) throws IOException;

  @Override
  public abstract ResponseWriter cloneWithWriter(Writer writer);

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Deprecated
  public abstract void writeAttribute(String name, Object value, final String property) throws IOException;

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Deprecated
  public abstract void writeText(Object text, String property) throws IOException;

  @Override
  public abstract void flush() throws IOException;

  // others (not from ResponseWriter)

  /**
   * Writes a string attribute. The renderer may set escape=false to switch of escaping of the string,
   * if it is not necessary.
   */
  public abstract void writeAttribute(String name, String string, boolean escape) throws IOException;

  /**
   * Writes a boolean attribute. The value will not escaped.
   */
  public void writeAttribute(String name, boolean on) throws IOException {
    if (on) {
      writeAttribute(name, name, false);
    }
  }

  /**
   * Writes a integer attribute. The value will not escaped.
   */
  public void writeAttribute(String name, int number) throws IOException {
    writeAttribute(name, Integer.toString(number), false);
  }

  /**
   * Writes a propery as attribute. The value will be escaped.
   */
  public void writeAttributeFromComponent(String name, String property) throws IOException {
    writeAttribute(name, null, property);
  }

  /**
   * Write the id attribute. The value will not escaped.
   */
  public void writeIdAttribute(String id) throws IOException {
    writeAttribute(HtmlAttributes.ID, id, false);
  }

  /**
   * Write the name attribute. The value will not escaped.
   */
  public void writeNameAttribute(String name) throws IOException {
    writeAttribute(HtmlAttributes.NAME, name, false);
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public void writeClassAttribute(String cssClass) throws IOException {
    writeAttribute(HtmlAttributes.CLASS, cssClass, false);
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public void writeClassAttribute(StyleClasses styleClasses) throws IOException {
    writeAttribute(HtmlAttributes.CLASS, styleClasses.toString(), false);
  }

  /**
   * Write the class attribute. The value will not escaped.
   */
  public abstract void writeClassAttribute() throws IOException;

  /**
   * Write the style attribute. The value will not escaped.
   */
  public void writeStyleAttribute(Style style) throws IOException {
    if (style != null) {
      writeAttribute(HtmlAttributes.STYLE, style.encode(), false);
    }
  }

  /**
   * Write the style attribute. The value will not escaped.
   * @deprecated since 1.5.0, use writeStyleAttribute(Style) instead.
   */
  @Deprecated
  public void writeStyleAttribute(String style) throws IOException {
    writeAttribute(HtmlAttributes.STYLE, style, false);
  }

  public void writeJavascript(String script) throws IOException {
    startJavascript();
    write(script);
    endJavascript();
  }

  public void endJavascript() throws IOException {
    write("\n// -->\n");
    endElement(HtmlConstants.SCRIPT);
  }

  public void startJavascript() throws IOException {
    startElement(HtmlConstants.SCRIPT, null);
    writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
    write("\n<!--\n");
  }

  /**
   * Write text content. The text will be escaped.
   */
  public void writeText(String text) throws IOException {
    writeText(text, null);
  }

  /**
   * Writes a property as text. The text will be escaped.
   */
  public void writeTextFromComponent(String property) throws IOException {
    writeText(null, property);
  }

  public String getContentTypeWithCharSet() {
    String contentType = getContentType();
    if (contentType == null) {
      contentType = "text/html";
    }
    String characterEncoding = getCharacterEncoding();
    if (characterEncoding == null) {
      characterEncoding = "UTF-8";
    }

    StringBuilder builder = new StringBuilder(contentType);
    builder.append("; charset=");
    builder.append(characterEncoding);
    return builder.toString();

  }
}
