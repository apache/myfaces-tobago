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

import org.apache.myfaces.tobago.internal.component.AbstractUIDate;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.CssItem;
import org.apache.myfaces.tobago.renderkit.css.FaIcons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.lang.invoke.MethodHandles;

public class DateRenderer<T extends AbstractUIDate> extends InRenderer<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_DATE;
  }

  @Override
  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final T input)
      throws IOException {

    super.writeAdditionalAttributes(facesContext, writer, input);
    writer.writeAttribute(HtmlAttributes.PATTERN, DateFormatUtils.toJavaScriptPattern(input.getPattern()), true);
//    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    writer.writeAttribute(CustomAttributes.TODAY, sdf.format(new Date()), true); XXX seem no longer needed
    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(facesContext.getViewRoot().getLocale());
    writer.writeAttribute(CustomAttributes.I18N, JsonUtils.encode(dateTimeI18n), true);
    writer.writeAttribute(CustomAttributes.TODAY_BUTTON, input.isTodayButton());
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.INPUT__GROUP__OUTER);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);

    super.encodeBeginField(facesContext, component);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final T component) throws IOException {

    super.encodeEndField(facesContext, component);

    final String pattern = component.getPattern();
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.BUTTON);
    writer.writeClassAttribute(
        BootstrapClass.BTN,
        BootstrapClass.BTN_SECONDARY,
        TobagoClass.DATE__PICKER);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeAttribute(HtmlAttributes.TITLE,
        ResourceUtils.getString(facesContext, "date.title"), true);
    writer.writeAttribute(HtmlAttributes.DISABLED, component.isDisabled() || component.isReadonly());
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());

    final boolean hasDate = StringUtils.containsAny(pattern, "yYMDdE");
    final boolean hasTime = StringUtils.containsAny(pattern, "Hhms");

    if (hasDate || !hasTime) { //  || !hasTime is, to have at least one icon
      writer.startElement(HtmlElements.I);
      writer.writeClassAttribute(FaIcons.FA, FaIcons.CALENDAR);
      writer.endElement(HtmlElements.I);
    }
    if (hasTime) {
      writer.startElement(HtmlElements.I);
      writer.writeClassAttribute(FaIcons.FA, FaIcons.CLOCK_O);
      writer.endElement(HtmlElements.I);
    }

    if (StringUtils.containsAny(pattern, "GWFKzX")) {
      LOG.warn("Pattern chars 'G', 'W', 'F', 'K', 'z' and 'X' are not supported: " + pattern);
    }

    writer.endElement(HtmlElements.BUTTON);

    writer.endElement(HtmlElements.DIV);

    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected CssItem getRendererCssClass() {
    return null;
  }
}
