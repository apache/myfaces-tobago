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

import org.apache.myfaces.tobago.internal.component.AbstractUIStars;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import jakarta.faces.context.FacesContext;

import java.io.IOException;

public class StarsRenderer<T extends AbstractUIStars> extends DecorationPositionRendererBase<T> {

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_STARS;
  }

  @Override
  protected void encodeBeginField(FacesContext facesContext, T component) throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final String hiddenInputId = clientId + ComponentUtils.SUB_SEPARATOR + "input";
    final String sliderId = clientId + ComponentUtils.SUB_SEPARATOR + "slider";
    final int value = component.getRangeValue();
    final int max = component.getRangeMax();
    final Double placeholder = component.getPlaceholder();
    final boolean readonly = component.isReadonly();
    final boolean disabled = component.isDisabled();
    final boolean required = component.isRequired();
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);

    final String sliderValue = component.getSubmittedValue() != null
        ? (String) component.getSubmittedValue() : String.valueOf(value);
    final String hiddenInputValue = required && "0".equals(sliderValue) ? null : sliderValue;

    writer.startElement(HtmlElements.DIV);
    writer.writeIdAttribute(fieldId);
    writer.writeClassAttribute(
        TobagoClass.STARS,
        component.getCustomClass());

    // The hidden input must be used to submit the rating. The 'required' attribute is not allowed on slider component.
    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(hiddenInputId);
    writer.writeNameAttribute(clientId);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN);
    writer.writeAttribute(HtmlAttributes.VALUE, hiddenInputValue, true);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    writer.endElement(HtmlElements.INPUT);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.STARS__CONTAINER);

    writer.startElement(HtmlElements.INPUT);
    writer.writeIdAttribute(sliderId);
    writer.writeNameAttribute(clientId);
    writer.writeClassAttribute(TobagoClass.STARS__SLIDER);
    writer.writeAttribute(HtmlAttributes.TYPE, readonly || disabled ? HtmlInputTypes.HIDDEN : HtmlInputTypes.RANGE);
    writer.writeAttribute(HtmlAttributes.MIN, required ? 1 : 0);
    writer.writeAttribute(HtmlAttributes.MAX, max);
    writer.writeAttribute(HtmlAttributes.VALUE, sliderValue, true);
    if (placeholder != null) {
      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder.toString(), true);
    }
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.endElement(HtmlElements.INPUT);

    encodeBehavior(writer, facesContext, component);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.STARS__FOCUS_BOX);
    writer.endElement(HtmlElements.DIV);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.STARS__TOOLTIP, BootstrapClass.FADE);
    writer.endElement(HtmlElements.SPAN);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.STARS__SELECTED);
    writer.endElement(HtmlElements.SPAN);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.STARS__UNSELECTED);
    writer.endElement(HtmlElements.SPAN);
    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(TobagoClass.STARS__PRESELECTED);
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.SPAN);
  }

  @Override
  protected void encodeEndField(FacesContext facesContext, T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected String getFieldId(FacesContext facesContext, T component) {
    return component.getFieldId(facesContext);
  }
}
