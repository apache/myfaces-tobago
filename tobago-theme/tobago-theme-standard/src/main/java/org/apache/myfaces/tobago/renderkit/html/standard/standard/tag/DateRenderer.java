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

package org.apache.myfaces.tobago.renderkit.html.standard.standard.tag;

import org.apache.myfaces.tobago.component.SupportsCss;
import org.apache.myfaces.tobago.internal.component.AbstractUIDate;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.renderkit.html.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class DateRenderer extends InRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(DateRenderer.class);

  @Override
  public void prepareRender(
      final FacesContext facesContext, final UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    SupportsCss css = (SupportsCss) component;
    css.getCurrentCss().add(BootstrapClass.FORM_CONTROL.getName());
  }

  @Override
  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final AbstractUIInput input)
      throws IOException {

    final AbstractUIDate date = (AbstractUIDate) input;

    super.writeAdditionalAttributes(facesContext, writer, date);
    writer.writeAttribute(DataAttributes.PATTERN, date.getPattern(), true);
    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(facesContext.getViewRoot().getLocale());
    writer.writeAttribute(DataAttributes.DATE_TIME_I18N, JsonUtils.encode(dateTimeI18n), true);
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final AbstractUIDate date = (AbstractUIDate) component;
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    final String pattern = date.getPattern();

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(TobagoClass.PANEL);

    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);

    super.encodeBegin(facesContext, component);

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP_BTN);
    writer.startElement(HtmlElements.BUTTON, null);
    writer.writeClassAttribute(BootstrapClass.BTN, BootstrapClass.BTN_DEFAULT);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON, false);
    writer.writeAttribute(HtmlAttributes.DISABLED, date.isDisabled() || date.isReadonly());
    if (pattern.contains("m")) { // simple guessing
      if (pattern.contains("d")) {
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(BootstrapClass.GLYPHICON, BootstrapClass.GLYPHICON_CALENDAR);
        writer.endElement(HtmlElements.SPAN);
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(BootstrapClass.GLYPHICON, BootstrapClass.GLYPHICON_TIME);
        writer.endElement(HtmlElements.SPAN);
      } else {
        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(BootstrapClass.GLYPHICON, BootstrapClass.GLYPHICON_TIME);
        writer.endElement(HtmlElements.SPAN);
      }
    } else {
      writer.startElement(HtmlElements.SPAN, null);
      writer.writeClassAttribute(BootstrapClass.GLYPHICON, BootstrapClass.GLYPHICON_CALENDAR);
      writer.endElement(HtmlElements.SPAN);
    }
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);
  }
}
