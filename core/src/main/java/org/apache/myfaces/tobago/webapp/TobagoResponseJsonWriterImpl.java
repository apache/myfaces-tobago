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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.util.FastStringWriter;
import org.apache.myfaces.tobago.util.XmlUtils;

import javax.faces.component.UIComponent;
import java.io.IOException;
import java.io.Writer;
import java.util.EmptyStackException;

public class TobagoResponseJsonWriterImpl extends TobagoResponseWriterImpl {

  private static final Log LOG = LogFactory.getLog(TobagoResponseWriterImpl.class);

  private Writer javascriptWriter;
  private boolean javascriptMode;

  public TobagoResponseJsonWriterImpl(Writer writer, String contentType, String characterEncoding, boolean xml) {
    super(writer, contentType, characterEncoding, xml);
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
    writeInternal(javascriptMode ? javascriptWriter : getWriter(), AjaxUtils.encodeJavascriptString(string));
  }

  @Override
  public void writeJavascript(String script) throws IOException {
    writeInternal(javascriptWriter, AjaxUtils.encodeJavascriptString(script));
  }

  public String getJavascript() {
    return javascriptWriter.toString();
  }

  @Override
  protected void startElementInternal(Writer writer, String name, UIComponent currentComponent)
      throws IOException {
    setComponent(currentComponent);
    getStack().push(name);
    if (isStartStillOpen()) {
      writer.write(">");
    }
    writer.write("<");
    writer.write(name);
    setStartStillOpen(true);
  }

  @Override
  protected void endElementInternal(Writer writer, String name) throws IOException {
    if (LOG.isDebugEnabled()) {
      LOG.debug("end Element: " + name);
    }
    String top = "";
    try {
      top = getStack().pop();
    } catch (EmptyStackException e) {
      LOG.error("Failed to close element \"" + name + "\"!");
      throw e;
    }
    if (!top.equals(name)) {
      final String trace = getCallingClassStackTraceElementString();
      LOG.error("Element end with name='" + name + "' doesn't "
          + "match with top element on the stack='" + top + "' "
          + trace.substring(trace.indexOf('(')));
    }

    if (EMPTY_TAG.contains(name)) {
      if (isXml()) {
        writer.write("/>");
      } else {
        writer.write(">");
      }
    } else {
      if (isStartStillOpen()) {
        writer.write(">");
      }
      writer.write("</");
      writer.write(name);
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
  protected void writeAttributeInternal(Writer writer, String name, String value, boolean escape)
      throws IOException {
    if (!isStartStillOpen()) {
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
      writer.write("=\\\"");
      if (isXml()) {
        writer.write(XmlUtils.escape(value));
      } else {
        if (escape) {
          getHelper().writeAttributeValue(value);
        } else {
          writer.write(value);
        }
      }
      writer.write("\\\"");
    }
  }
}
