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

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIParameter;
import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIOut;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.sanitizer.SanitizeMode;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class OutRenderer<T extends AbstractUIOut> extends DecorationPositionRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

  @Override
  protected boolean isOutputOnly(T component) {
    return true;
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_OUT;
  }

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final boolean plain = component.isPlain() || component.isCompact() || !component.isCreateSpan();

    if (isInside(facesContext, HtmlElements.TOBAGO_IN)) {
      encodeBeginField(facesContext, component);
    } else if (plain) {
      encodeText(facesContext, component);
    } else {
      super.encodeBeginInternal(facesContext, component);
    }
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final boolean plain = component.isPlain() || component.isCompact() || !component.isCreateSpan();

    if (isInside(facesContext, HtmlElements.TOBAGO_IN)) {
      encodeEndField(facesContext, component);
    } else if (!plain) {
      super.encodeEndInternal(facesContext, component);
    }
  }

  @Override
  public void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final Markup markup = component.getMarkup();

    writer.startElement(isInside(facesContext, HtmlElements.TOBAGO_IN) ? HtmlElements.TOBAGO_OUT : HtmlElements.SPAN);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);

    writer.writeClassAttribute(
        markup != null && markup.contains(Markup.DELETED) ? TobagoClass.DELETED : null,
        markup != null && markup.contains(Markup.NUMBER) ? TobagoClass.NUMBER : null,
        component.getCustomClass(),
        isInside(facesContext, HtmlElements.TOBAGO_IN)
            ? BootstrapClass.INPUT_GROUP_TEXT : BootstrapClass.FORM_CONTROL_PLAINTEXT,
        BootstrapClass.textColor(markup),
        BootstrapClass.fontStyle(markup));
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }

    encodeText(facesContext, component);
  }

  private void encodeText(final FacesContext facesContext, final T out) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final boolean escape = out.isEscape();
    final boolean keepLineBreaks = out.isKeepLineBreaks();

    String text = getCurrentValue(facesContext, out);
    if (text == null) {
      text = "";
    }

    if (out.isMessageFormat()) {
      text = getOutputFormatText(facesContext, text, out);
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
      writer.writeText(""); // to ensure the closing > of the <span> start tag.
      if (SanitizeMode.auto == out.getSanitize()) {
        final Sanitizer sanitizer = TobagoConfig.getInstance(facesContext).getSanitizer();
        text = sanitizer.sanitize(text);
      }
      writer.write(text);
    }
  }

  private String getOutputFormatText(final FacesContext facesContext, final String pattern, final T out) {
    final Object[] args;
    if (out.getChildCount() > 0) {
      final List<UIParameter> validParams = getValidUIParameterChildren(out);
      if (!validParams.isEmpty()) {
        List<Object> argsList = new ArrayList<>(validParams.size());
        for (UIParameter param : validParams) {
          argsList.add(param.getValue());
        }
        args = argsList.toArray(EMPTY_OBJECT_ARRAY);
      } else {
        args = EMPTY_OBJECT_ARRAY;
      }
    } else {
      args = EMPTY_OBJECT_ARRAY;
    }

    final MessageFormat format = new MessageFormat(pattern, facesContext.getViewRoot().getLocale());
    try {
      return format.format(args);
    } catch (Exception e) {
      LOG.error("Error formatting message of component with clientId='{}'", out.getClientId(facesContext));
      return pattern;
    }
  }

  private List<UIParameter> getValidUIParameterChildren(T out) {

    List<UIParameter> parameters = new ArrayList<>();
    for (UIComponent child : out.getChildren()) {
      if (child instanceof UIParameter) {
        UIParameter param = (UIParameter) child;
        if (param.isDisable() || !param.isRendered()) {
          // ignore
          continue;
        }
        parameters.add(param);
      }
    }

    return parameters;
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(isInside(facesContext, HtmlElements.TOBAGO_IN) ? HtmlElements.TOBAGO_OUT : HtmlElements.SPAN);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getClientId(facesContext);
  }

}
