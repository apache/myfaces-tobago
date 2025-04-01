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

import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlTypes;
import org.apache.myfaces.tobago.renderkit.html.MarkupLanguageAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.io.Writer;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class TobagoResponseWriterBase extends TobagoResponseWriter {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  protected static final String XML_VERSION_1_0_ENCODING_UTF_8 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";

  protected static final char[] XML_VERSION_1_0_ENCODING_UTF_8_CHARS = XML_VERSION_1_0_ENCODING_UTF_8.toCharArray();

  private static final boolean COMPATIBLE = false;

  private int level = 0;

  private int inlineStack = 0;

  private UIComponent component;
  private Map<Integer, String> levelToChangedElementTag = new HashMap<>();

  private boolean startStillOpen;

  private final Writer writer;

  private final String contentType;

  private final Charset charset;

  protected TobagoResponseWriterBase(final Writer writer, final String contentType, final Charset charset) {
    this.writer = writer;
    this.contentType = contentType;
    this.charset = charset != null ? charset : StandardCharsets.UTF_8;
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

  protected final void writeInternal(final Writer sink, final String string) throws IOException {
    closeOpenTag();
    sink.write(string);
  }

  @Override
  public void write(final int j) throws IOException {
    closeOpenTag();
    writer.write(j);
  }

  @Override
  public void write(final char[] chars) throws IOException {
    closeOpenTag();
    writer.write(chars);
  }

  @Override
  public void write(final String string, final int j, final int k) throws IOException {
    closeOpenTag();
    writer.write(string, j, k);
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
    Flush any output buffered by the output method to the underlying Writer or OutputStream.
    This method will not flush the underlying Writer or OutputStream;
    it simply clears any values buffered by this ResponseWriter.
     */
    closeOpenTag();
  }

  protected void closeOpenTag() throws IOException {
    if (startStillOpen) {
      handlePassThroughAttributes();
      writer.write('>');
      startStillOpen = false;
    }
  }

  protected void handlePassThroughAttributes() throws IOException {
    if (component != null && component.getPassThroughAttributes(false) != null) {
      for (Map.Entry<String, Object> entry : component.getPassThroughAttributes().entrySet()) {
        String key = entry.getKey();
        if (Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY.equals(key)) {
          // skip rendering
          continue;
        }

        Object value = entry.getValue();
        if (value instanceof ValueExpression) {
          value = ((ValueExpression) value).getValue(FacesContext.getCurrentInstance().getELContext());
        }
        // Faces 2.2 In the renderkit javadoc of jsf 2.2 spec says this
        // (Rendering Pass Through Attributes):
        // "... The ResponseWriter must ensure that any pass through attributes are
        // rendered on the outer-most markup element for the component. If there is
        // a pass through attribute with the same name as a renderer specific
        // attribute, the pass through attribute takes precedence. Pass through
        // attributes are rendered as if they were passed to
        // ResponseWriter.writeURIAttribute(). ..."
        // Note here it says "as if they were passed", instead say "... attributes are
        // encoded and rendered as if ...". Black box testing against RI shows that there
        // is no URI encoding at all in this part, so in this case the best is do the
        // same here. After all, it is responsibility of the one who set the passthrough
        // attribute to do the proper encoding in cases when a URI is provided. However,
        // that does not means the attribute should not be encoded as other attributes.
        // TODO boolean should be handled in writeAttributeInternal
        if (value instanceof Boolean) {
          if (!Boolean.FALSE.equals(value)) {
            writer.write(' ');
            writer.write(key);
          }
        } else {
          writeAttributeInternal(writer, key, value != null ? value.toString() : "", true);
        }
      }
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
    return charset.name();
  }

  protected Charset getCharset() {
    return charset;
  }

  @Override
  public void startElement(final String name, final UIComponent currentComponent) throws IOException {
    closeOpenTag();
    final boolean inline = HtmlElements.isInline(name);
    this.component = currentComponent;
    startElementInternal(writer, name, inline);
  }

  public void startElement(final HtmlElements name, final UIComponent currentComponent) throws IOException {
    closeOpenTag();
    this.component = currentComponent;
    startElementInternal(writer, name.getValue(), name.isInline());
  }

  @Override
  public void startElement(final HtmlElements name) throws IOException {
     startElement(name, null);
  }

  protected void startElementInternal(final Writer sink, final String name, final boolean inline)
      throws IOException {
    String localElementName = null;
    boolean localElementInline = inline;
    if (component != null && component.getPassThroughAttributes(false) != null) {
      Map<String, Object> passThroughAttributes = component.getPassThroughAttributes(false);
      Object value = passThroughAttributes.get(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
      if (value != null) {
        if (value instanceof ValueExpression) {
          value = ((ValueExpression) value).getValue(FacesContext.getCurrentInstance().getELContext());
        }
        localElementName = value.toString().toLowerCase(Locale.ROOT).trim();
        if (!name.equals(localElementName)) {
          levelToChangedElementTag.put(level, localElementName);
          localElementInline = HtmlElements.isInline(localElementName);
        }
      }
    }
    if (localElementInline) {
      inlineStack++;
    }
    if (inlineStack <= 1) {
      sink.write('\n');
    }
    sink.write('<');
    if (localElementName != null) {
      sink.write(localElementName);
    } else {
      sink.write(name);
    }
    startStillOpen = true;
    level++;
  }

  @Override
  public void endElement(final String name) throws IOException {
    level--;
    String elementTagToRender = levelToChangedElementTag.get(level);
    if (elementTagToRender == null) {
      elementTagToRender = name;
    }
    renderNonTobagoEndElement(elementTagToRender);
  }

  private void renderNonTobagoEndElement(String element) throws IOException {
    final boolean inline = HtmlElements.isInline(element);
    if (HtmlElements.isVoid(element)) {
      closeEmptyTag();
    } else {
      endElementInternal(writer, element, inline);
    }
    startStillOpen = false;
    if (inline) {
      inlineStack--;
      assert inlineStack >= 0;
    }
  }

  @Override
  public void endElement(final HtmlElements name) throws IOException {
    level--;
    String elementTagToRender = levelToChangedElementTag.remove(level);
    if (elementTagToRender != null) {
      renderNonTobagoEndElement(elementTagToRender);
    } else {
      final boolean inline = name.isInline();
      if (name.isVoid()) {
        closeEmptyTag();
      } else {
        endElementInternal(writer, name.getValue(), inline);
      }
      startStillOpen = false;
      if (inline) {
        inlineStack--;
        assert inlineStack >= 0;
      }
    }
  }

  @Override
  public void writeComment(final Object obj) throws IOException {
    closeOpenTag();
    final String comment = obj.toString();
    write("<!--");
    write(comment);
    write("-->");
  }

  @Deprecated(since = "3.0.0", forRemoval = true)
  @Override
  public void writeAttribute(final String name, final Object value, final String property)
      throws IOException {

    final String attribute = findValue(value, property);
    writeAttributeInternal(writer, name, attribute, true);
  }

  protected final String getCallingClassStackTraceElementString() {
    final StackTraceElement[] stackTrace = new Exception().getStackTrace();
    int j = 1;
    while (stackTrace[j].getClassName().contains("ResponseWriter")) {
      j++;
    }
    return stackTrace[j].toString();
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

  protected void endElementInternal(final Writer sink, final String name, final boolean inline) throws IOException {
    if (startStillOpen) {
      handlePassThroughAttributes();
      sink.write('>');
    }
    if (inline) {
      sink.write("</");
    } else {
      if (COMPATIBLE) {
        sink.write("\n");
      }
      sink.write("</");
    }
    sink.write(name);
    sink.write('>');
  }

  protected abstract void closeEmptyTag() throws IOException;

  protected void writeAttributeInternal(
      final Writer sink, final MarkupLanguageAttributes name, final String value, final boolean escape)
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
    // If there is a pass through attribute with the same
    // name as a renderer specific attribute,
    // the pass through attribute takes precedence.
    String attributeName = name.getValue();
    if (component != null && component.getPassThroughAttributes(false) != null
        && component.getPassThroughAttributes(false).containsKey(attributeName)) {
      return;
    }
    writeAttributeInternal(sink, attributeName, value, escape);
  }

  private void writeAttributeInternal(
      final Writer sink, final String attributeName, final String value, final boolean escape)
      throws IOException {
    if (value != null) {
      sink.write(' ');
      sink.write(attributeName);
      sink.write("='");
      writerAttributeValue(value, escape);
      sink.write('\'');
    }
  }

  protected abstract void writerAttributeValue(String value, boolean escape) throws IOException;


}

