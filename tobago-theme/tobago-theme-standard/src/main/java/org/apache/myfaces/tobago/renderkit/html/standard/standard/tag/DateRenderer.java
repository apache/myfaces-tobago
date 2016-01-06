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

import org.apache.myfaces.tobago.internal.component.AbstractUIDate;
import org.apache.myfaces.tobago.internal.component.AbstractUIInput;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class DateRenderer extends InRenderer {

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
  protected void encodeBeginField(FacesContext facesContext, UIComponent component) throws IOException {
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.PANEL);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);

    super.encodeBeginField(facesContext, component);
  }

  @Override
  public void encodeEndField(FacesContext facesContext, UIComponent component) throws IOException {

    super.encodeEndField(facesContext, component);

    final AbstractUIDate date = (AbstractUIDate) component;
    final String pattern = date.getPattern();
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.SPAN);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP_BTN);
    writer.startElement(HtmlElements.BUTTON);
    writer.writeClassAttribute(BootstrapClass.BTN, BootstrapClass.BTN_SECONDARY);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeAttribute(HtmlAttributes.DISABLED, date.isDisabled() || date.isReadonly());
    if (pattern.contains("m")) { // simple guessing
      if (pattern.contains("d")) {
        writer.writeIcon(Icons.CALENDAR);
        writer.writeIcon(Icons.CLOCK_O);
      } else {
        writer.writeIcon(Icons.CLOCK_O);
      }
    } else {
      writer.writeIcon(Icons.CALENDAR);
    }
    writer.endElement(HtmlElements.BUTTON);
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);
  }
}
