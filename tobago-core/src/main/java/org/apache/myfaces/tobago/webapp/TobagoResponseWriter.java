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

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.FontAwesomeIconEncoder;
import org.apache.myfaces.tobago.renderkit.css.IconEncoder;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlTypes;
import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * <p>
 * This provides an alternative ResponseWriter interfaces, which allows optimizations.
 * E. g. some attributes needed to to be escaped.
 * </p>
 */
public abstract class TobagoResponseWriter extends ResponseWriter {

  private static final CssItem[] NO_CSS_ITEMS = new CssItem[0];

  private IconEncoder iconEncoder = new FontAwesomeIconEncoder();

  // same as in ResponseWriter

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Deprecated
  @Override
  public abstract void startElement(String name, UIComponent component) throws IOException;

  public abstract void startElement(HtmlElements name) throws IOException;

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
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
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Override
  @Deprecated
  public abstract void writeAttribute(String name, Object value, final String property) throws IOException;

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Override
  @Deprecated
  public abstract void writeURIAttribute(String name, Object value, final String property) throws IOException;

  /**
   * @deprecated Should not directly called via this interface. There is be a special method which might be better.
   */
  @Override
  @Deprecated
  public abstract void writeText(Object text, String property) throws IOException;

  @Override
  public abstract void flush() throws IOException;

  // others (not from ResponseWriter)

  /**
   * Writes a string attribute. The renderer may set escape=false to switch of escaping of the string,
   * if it is not necessary.
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
   * Write the class attribute. The value will not escaped.
   * @param classes The abstract representation of the css class string, normally created by the renderer.
   */
  public void writeClassAttribute(final Classes classes) throws IOException {
    final String stringValue = classes.getStringValue();
    if (StringUtils.isNotBlank(stringValue)) {
      writeAttribute(HtmlAttributes.CLASS, stringValue, false);
    }
  }

  public void writeClassAttribute(final CssItem first) throws IOException {
    writeClassAttribute(first, null, NO_CSS_ITEMS);
  }

  public void writeClassAttribute(final CssItem first, final CssItem second) throws IOException {
    writeClassAttribute(first, second, NO_CSS_ITEMS);
  }

  public void writeClassAttribute(final CssItem first, final CssItem second, final CssItem... others)
      throws IOException {
    StringBuilder builder = new StringBuilder();
    if (first != null) {
      builder.append(first.getName());
      builder.append(' ');
    }
    if (second != null) {
      builder.append(second.getName());
      builder.append(' ');
    }
    for (CssItem other : others) {
      if (other != null) {
        builder.append(other.getName());
        builder.append(' ');
      }
    }
    if (builder.length() > 0) {
      writeAttribute(HtmlAttributes.CLASS, builder.deleteCharAt(builder.length() - 1).toString(), false);
    }
  }

  public void writeClassAttribute(final Classes classes, final CssItem... others) throws IOException {
    StringBuilder builder = new StringBuilder(classes.getStringValue());
    for (CssItem other : others) {
      if (other != null) {
        builder.append(' ');
        builder.append(other.getName());
      }
    }
    writeAttribute(HtmlAttributes.CLASS, builder.toString(), false);
  }

  /**
   * Write the style attribute. The value may be escaped (depending of the content).
   */
  public void writeStyleAttribute(final Style style) throws IOException {
    if (style != null) {
      final String json = style.encodeJson();
      if (json.length() > 2) { // empty "{}" needs not to be written
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        writeAttribute(
            DataAttributes.STYLE, json, style.needsToBeEscaped() || AjaxUtils.isAjaxRequest(facesContext));
        // in case of AJAX we need to escape the " as long we use
        // org.apache.myfaces.tobago.internal.webapp.JsonResponseWriter
      }
    }
  }

  /**
   * Write the style attribute. The value will not escaped.
   * @deprecated since 1.5.0, use writeStyleAttribute(Style) instead.
   */
  @Deprecated
  public void writeStyleAttribute(final String style) throws IOException {
    writeAttribute(HtmlAttributes.STYLE, style, false);
  }

  /**
   * Writes an supported icon.
   */
  public void writeIcon(final Icons icon, final CssItem... cssItems) throws IOException {
    writeIcon(icon, null, cssItems);
  }

  /**
   * Writes an supported icon with explicit style information.
   */
  public void writeIcon(final Icons icon, final Style style, final CssItem... cssItems) throws IOException {
    iconEncoder.encode(this, icon, style, cssItems);
  }

  /**
   * @deprecated Should not be used, because it conflicts with CSP.
   */
  @Deprecated
  public void writeJavascript(final String script) throws IOException {
    startJavascript();
    write(script);
    endJavascript();
  }

  /**
   * @deprecated Should not be used, because it conflicts with CSP.
   */
  @Deprecated
  public void endJavascript() throws IOException {
//    write("\n// -->\n"); // todo: for XHMTL we may need
    endElement(HtmlElements.SCRIPT);
  }

  /**
   * @deprecated Should not be used, because it conflicts with CSP.
   */
  @Deprecated
  public void startJavascript() throws IOException {
    startElement(HtmlElements.SCRIPT);
    writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
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
      characterEncoding = "UTF-8";
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
