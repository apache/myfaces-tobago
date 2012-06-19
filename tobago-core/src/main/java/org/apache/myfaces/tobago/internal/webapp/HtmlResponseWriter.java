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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.util.FastStringWriter;
import org.apache.myfaces.tobago.internal.util.HtmlWriterUtils;
import org.apache.myfaces.tobago.internal.util.JsonWriterUtils;
import org.apache.myfaces.tobago.internal.util.WriterUtils;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.FacesVersion;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Map;

public class HtmlResponseWriter extends TobagoResponseWriterBase {

  private static final String HTML_DOCTYPE =
      "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">";

  private final WriterUtils helper;
  private FastStringWriter javascriptWriter;
  private boolean javascriptMode;

  public HtmlResponseWriter(
      Writer writer, String contentType, String characterEncoding) {
    super(writer, contentType, characterEncoding);
    if ("application/json".equals(contentType)) {
      this.helper = new JsonWriterUtils(writer, characterEncoding);
    } else {
      this.helper = new HtmlWriterUtils(writer, characterEncoding);
    }
    this.javascriptWriter = new FastStringWriter();
  }

  @Override
  public void endJavascript() throws IOException {
    javascriptMode = false;
  }

  @Override
  public void startJavascript() throws IOException {
    javascriptMode = true;
  }

  @Override
  public void write(String string) throws IOException {
    if (javascriptMode) {
      writeJavascript(string);
    } else {
      writeInternal(getWriter(), string);
    }
  }

  @Override
  public void writeJavascript(String script) throws IOException {
    writeInternal(javascriptWriter, script);
  }

  public String getJavascript() {
    return javascriptWriter.toString();
  }

  public final WriterUtils getHelper() {
    return helper;
  }

  public void writeText(final Object text, final String property)
      throws IOException {
    closeOpenTag();
    final String value = findValue(text, property);
    helper.writeText(value);
  }

  public void writeText(final char[] text, final int offset, final int length)
      throws IOException {
    closeOpenTag();
    helper.writeText(text, offset, length);
  }

  @Override
  public void write(final char[] cbuf, final int off, final int len) throws IOException {
    // Related to http://java.net/jira/browse/JAVASERVERFACES_SPEC_PUBLIC-696
    if (!FacesVersion.supports21() && Arrays.equals(cbuf, XML_VERSION_1_0_ENCODING_UTF_8_CHARS)) {
      // drop
    } else {
      super.write(cbuf, off, len);
    }
  }

  @Override
  protected final void closeEmptyTag() throws IOException {
    getWriter().write("\n>");
  }

  @Override
  protected void writerAttributeValue(String value, boolean escape) throws IOException {
    if (escape) {
      helper.writeAttributeValue(value);
    } else {
      getWriter().write(value);
    }
  }

  public ResponseWriter cloneWithWriter(final Writer originalWriter) {
    return new HtmlResponseWriter(
        originalWriter, getContentType(), getCharacterEncoding());
  }

  /**
   * @deprecated
   */
  @Deprecated
  public static Style ensureHtmlStyleMap(UIComponent component, Style styles) {
    if (styles == null) {
      styles = new Style();
      ((Map<String, Object>) component.getAttributes()).put(Attributes.STYLE, styles);
    }
    return styles;
  }

  @Override
  public void startDocument() throws IOException {
    getWriter().write(HTML_DOCTYPE);
    getWriter().write('\n');
    startElement(HtmlElements.HTML, null);
  }

  @Override
  public void endElement(String name) throws IOException {
    if (name == HtmlElements.BODY) {
      String javascript = getJavascript();
      if (org.apache.commons.lang.StringUtils.isNotEmpty(javascript)) {
        startElement(HtmlElements.SCRIPT, null);
        writeAttribute(HtmlAttributes.TYPE, "text/javascript", false);
        write(getJavascript());
        endElement(HtmlElements.SCRIPT);
      }
    }
    super.endElement(name);
  }

  @Override
  public void endDocument() throws IOException {

    endElement(HtmlElements.HTML);
  }
}
