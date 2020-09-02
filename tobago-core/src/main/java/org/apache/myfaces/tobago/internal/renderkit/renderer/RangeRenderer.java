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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class RangeRenderer extends MessageLayoutRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_RANGE;
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final UIComponent component)
      throws IOException {
    final AbstractUIRange range = (AbstractUIRange) component;
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, range);
    final String currentValue = getCurrentValue(facesContext, range);
    final String clientId = range.getClientId(facesContext);
    final String fieldId = range.getFieldId(facesContext);
    final boolean readonly = range.isReadonly();
    final boolean disabled = range.isDisabled();
    final int min = range.getMin();
    final int max = range.getMax();
    final int step = range.getStep();

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.INPUT);

    if (range.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(range.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, range.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.RANGE);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, range);
    if (currentValue != null) {
      writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    }
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, range.getTabIndex());
    writer.writeAttribute(HtmlAttributes.MIN, min);
    writer.writeAttribute(HtmlAttributes.MAX, max);
    writer.writeAttribute(HtmlAttributes.STEP, step);

    final CssItem rendererCssClass = getRendererCssClass();
    writer.writeClassAttribute(
        rendererCssClass,
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(range)),
        BootstrapClass.FORM_CONTROL,
        range.getCustomClass());

    HtmlRendererUtils.renderFocus(clientId, range.isFocus(), ComponentUtils.isError(range), facesContext, writer);

    writer.endElement(HtmlElements.INPUT);

    encodeTooltip(writer, currentValue);

    encodeBehavior(writer, facesContext, range);
  }

  private void encodeTooltip(final TobagoResponseWriter writer, final String content) throws IOException {
    writer.startElement(HtmlElements.DIV);
//    writer.writeClassAttribute(TobagoClass.POPOVER__BOX, BootstrapClass.POPOVER);
    writer.writeClassAttribute(BootstrapClass.POPOVER, BootstrapClass.D_NONE);
//    writer.writeNameAttribute(popoverId);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.ARROW);
    writer.endElement(HtmlElements.DIV);
    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.POPOVER_BODY);
    if (content != null) {
      writer.writeText(content);
    }
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected void encodeEndField(final FacesContext facesContext, final UIComponent component) throws IOException {
  }

  protected CssItem getRendererCssClass() {
    return TobagoClass.IN;
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final UIComponent component) {
    final AbstractUIRange range = (AbstractUIRange) component;
    return range.getFieldId(facesContext);
  }
}
