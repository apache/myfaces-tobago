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

import org.apache.myfaces.tobago.internal.util.FastStringWriter;
import org.apache.myfaces.tobago.internal.util.JavascriptWriterUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;
import org.apache.myfaces.tobago.util.FacesVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.io.Writer;

public class JsonResponseWriter extends HtmlResponseWriter {

  private static final Logger LOG = LoggerFactory.getLogger(JsonResponseWriter.class);

  private Writer javascriptWriter;
  private boolean javascriptBlock;
  private JavascriptWriterUtils encodeInJavascriptBlock;
  private JavascriptWriterUtils encodeOutsideJavascriptBlock;

  public JsonResponseWriter(final Writer writer, final String contentType, final String characterEncoding) {
    super(writer, contentType, characterEncoding);
    this.javascriptWriter = new FastStringWriter();
    this.encodeOutsideJavascriptBlock = new JavascriptWriterUtils(writer, characterEncoding);
    this.encodeInJavascriptBlock = new JavascriptWriterUtils(javascriptWriter, characterEncoding);
  }

  @Override
  public void endJavascript() throws IOException {
    javascriptBlock = false;
  }

  @Override
  public void startJavascript() throws IOException {
    javascriptBlock = true;
  }

  @Override
  public void write(final String string) throws IOException {
    closeOpenTag();
    if (FacesVersion.isMojarra() && FacesVersion.supports21() && XML_VERSION_1_0_ENCODING_UTF_8.equals(string)) {
      // ignore
      return;
    }
    if (javascriptBlock) {
      encodeInJavascriptBlock.writeText(string);
    } else {
      encodeOutsideJavascriptBlock.writeText(string);
    }
  }

  @Override
  public void write(final char[] chars) throws IOException {
    // XXX remove me later:
    // this is a temporary workaround, should be removed after fixing the bug in Mojarra.
    // http://java.net/jira/browse/JAVASERVERFACES-2411
    // https://issues.apache.org/jira/browse/TOBAGO-1124
    if (FacesVersion.isMojarra() && FacesVersion.supports20()) {
      final StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
      if (stackTraceElements[2].getClassName().equals("com.sun.faces.renderkit.ServerSideStateHelper")) {
        super.write(StringUtils.replace(new String(chars), "\"", "\\\""));
        return;
      }
    }
    super.write(chars);
  }

  @Override
  public void writeJavascript(final String script) throws IOException {
    closeOpenTag();
    encodeInJavascriptBlock.writeText(script);
  }

  public String getJavascript() {
    return javascriptWriter.toString();
  }

  @Override
  protected void startElementInternal(final Writer writer, final HtmlElements name, final UIComponent currentComponent)
      throws IOException {
    setComponent(currentComponent);
    if (isStartStillOpen()) {
      writer.write(">");
    }
    writer.write("<");
    writer.write(name.getValue());
    setStartStillOpen(true);
  }

  @Override
  protected void endElementInternal(final Writer writer, final HtmlElements name) throws IOException {
    if (EMPTY_TAG.contains(name)) {
        writer.write(">");
    } else {
      if (isStartStillOpen()) {
        writer.write(">");
      }
      writer.write("</");
      writer.write(name.getValue());
      writer.write(">");
    }
    setStartStillOpen(false);
  }

  @Override
  protected void closeOpenTag() throws IOException {
    if (isStartStillOpen()) {
      getWriter().write(">");
      setStartStillOpen(false);
    }
  }

  @Override
  protected void writeAttributeInternal(
      final Writer writer, final MarkupLanguageAttributes name, final String value, final boolean escape)
      throws IOException {
    if (!isStartStillOpen()) {
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

      if (escape) {
        getHelper().writeAttributeValue(value);
      } else {
        writer.write(value);
      }
      writer.write("'");
    }
  }

  public void writeText(final Object text, final String property)
      throws IOException {
    closeOpenTag();
    final String value = findValue(text, property);
    getHelper().writeText(value);
  }

/* TODO: may also encode the backslash \, but will not be used currently
  public void writeText(final char[] text, final int offset, final int length)
      throws IOException {
    closeOpenTag();
    getHelper().writeText(text, offset, length);
  }
*/

  public ResponseWriter cloneWithWriter(final Writer originalWriter) {
     return new JsonResponseWriter(
         originalWriter, getContentType(), getCharacterEncoding());
   }


  @Override
  public void startDocument() throws IOException {
  }

  @Override
  public void endDocument() throws IOException {
  }
}
