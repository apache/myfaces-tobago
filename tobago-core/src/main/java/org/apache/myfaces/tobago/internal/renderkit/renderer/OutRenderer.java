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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIOut;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.RenderUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.sanitizer.SanitizeMode;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.StringTokenizer;

public class OutRenderer extends MessageLayoutRendererBase {

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.DIV;
  }

  @Override
  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIOut out = (AbstractUIOut) component;
    final boolean plain = out.isPlain() || out.isCompact() || !out.isCreateSpan();

    if (plain) {
      encodeText(facesContext, out);
    } else {
      super.encodeBegin(facesContext, component);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIOut out = (AbstractUIOut) component;
    final boolean plain = out.isPlain() || out.isCompact() || !out.isCreateSpan();

    if (!plain) {
      super.encodeEnd(facesContext, component);
    }
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUIOut out = (AbstractUIOut) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = out.getMarkup();

    writer.startElement(HtmlElements.SPAN);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, out);

    writer.writeClassAttribute(
        TobagoClass.OUT,
        TobagoClass.OUT.createMarkup(markup),
        getCssItems(facesContext, out),
        BootstrapClass.textColor(markup),
        BootstrapClass.fontStyle(markup),
        out.getCustomClass());
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, out);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    encodeText(facesContext, out);
  }

  private void encodeText(final FacesContext facesContext, final AbstractUIOut out) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final boolean escape = out.isEscape();
    final boolean keepLineBreaks = out.isKeepLineBreaks();

    String text = RenderUtils.currentValue(out);
    if (text == null) {
      text = "";
    }

    if (escape) {
      if (keepLineBreaks) {
        final StringTokenizer tokenizer = new StringTokenizer(text, "\r\n");
        while (tokenizer.hasMoreTokens()) {
          final String token = tokenizer.nextToken();
          writer.writeText(token);
          if (tokenizer.hasMoreTokens()) {
            writer.startElement(HtmlElements.BR);
            writer.endElement(HtmlElements.BR);
          }
        }
      } else {
        writer.writeText(text);
      }
    } else { // escape="false"
      writer.writeText("", null); // to ensure the closing > of the <span> start tag.
      if (SanitizeMode.auto == out.getSanitize()) {
        final Sanitizer sanitizer = TobagoConfig.getInstance(facesContext).getSanitizer();
        text = sanitizer.sanitize(text);
      }
      writer.write(text);
    }
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUIOut out = (AbstractUIOut) component;
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.endElement(HtmlElements.SPAN);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    return component.getClientId(facesContext);
  }

  protected CssItem[] getCssItems(final FacesContext facesContext, final AbstractUIOut out) {
    return new CssItem[]{BootstrapClass.FORM_CONTROL_PLAINTEXT};
  }
}
