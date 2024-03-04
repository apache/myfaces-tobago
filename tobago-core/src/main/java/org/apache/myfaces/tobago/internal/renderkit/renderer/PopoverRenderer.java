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
import org.apache.myfaces.tobago.internal.component.AbstractUIPopover;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.sanitizer.SanitizeMode;
import org.apache.myfaces.tobago.sanitizer.Sanitizer;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class PopoverRenderer<T extends AbstractUIPopover> extends RendererBase<T> {

  @Override
  public void encodeBeginInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final String labelString = component.getLabel();
    final String currentValue = getCurrentValue(facesContext, component);

    insideBegin(facesContext, HtmlElements.TOBAGO_POPOVER);
    writer.startElement(HtmlElements.TOBAGO_POPOVER);
    writer.writeIdAttribute(clientId);

    if (component.getCustomClass() != null) {
      writer.writeAttribute(CustomAttributes.CUSTOM_CLASS, component.getCustomClass().getName(), true);
    }

    if (SanitizeMode.auto == component.getSanitize()) {
      final Sanitizer sanitizer = TobagoConfig.getInstance(facesContext).getSanitizer();
      if (labelString != null && !labelString.isEmpty()) {
        writer.writeAttribute(CustomAttributes.LABEL, sanitizer.sanitize(labelString), true);
      }
      writer.writeAttribute(HtmlAttributes.VALUE, sanitizer.sanitize(currentValue), true);
    } else {
      if (labelString != null && !labelString.isEmpty()) {
        writer.writeAttribute(CustomAttributes.LABEL, labelString, true);
      }
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }

    writer.writeAttribute(CustomAttributes.ESCAPE, Boolean.toString(component.isEscape()), false);
    writer.writeAttribute(CustomAttributes.SANITIZE, component.getSanitize().name(), false);
  }

  @Override
  public void encodeEndInternal(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.TOBAGO_POPOVER);
    insideEnd(facesContext, HtmlElements.TOBAGO_POPOVER);
  }
}
