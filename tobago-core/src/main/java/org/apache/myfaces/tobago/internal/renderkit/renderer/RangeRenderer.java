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

import org.apache.myfaces.tobago.internal.component.AbstractUIRange;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class RangeRenderer<T extends AbstractUIRange> extends MessageLayoutRendererBase<T> {

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_RANGE;
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component)
      throws IOException {
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final String currentValue = getCurrentValue(facesContext, component);
    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final boolean readonly = component.isReadonly();
    final boolean disabled = component.isDisabled();
    final int min = component.getMin();
    final int max = component.getMax();
    final int step = component.getStep();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.TOOLTIP, BootstrapClass.FADE);
    writer.endElement(HtmlElements.SPAN);

    writer.startElement(HtmlElements.INPUT);

    if (component.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(component.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, component.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RANGE);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
    writer.writeAttribute(HtmlAttributes.MIN, min);
    writer.writeAttribute(HtmlAttributes.MAX, max);
    writer.writeAttribute(HtmlAttributes.STEP, step);

    final CssItem rendererCssClass = getRendererCssClass();
    writer.writeClassAttribute(
        rendererCssClass,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)),
        BootstrapClass.FORM_RANGE,
        component.getCustomClass());

    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);

    writer.endElement(HtmlElements.INPUT);

    encodeBehavior(writer, facesContext, component);
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
  }

  protected CssItem getRendererCssClass() {
    return TobagoClass.RANGE;
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }
}
