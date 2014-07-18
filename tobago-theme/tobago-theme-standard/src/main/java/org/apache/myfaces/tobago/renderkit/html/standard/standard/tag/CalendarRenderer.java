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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICalendar;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.model.CalendarModel;
import org.apache.myfaces.tobago.model.DateModel;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarRenderer extends LayoutComponentRendererBase {

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UICalendar output = (UICalendar) component;
    final String id = output.getClientId(facesContext);

    final Locale locale = facesContext.getViewRoot().getLocale();

    final Object value = output.getValue();
    final Calendar calendar;
    if (value instanceof Calendar) {
      calendar = (Calendar) value;
    } else { 
      calendar = new GregorianCalendar();
      if (value instanceof Date) {
        calendar.setTime((Date) value);
      }
    }
    final CalendarModel model = new CalendarModel(calendar);

    // rendering
    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, component);
    writer.writeIdAttribute(id);
    writer.writeClassAttribute(Classes.create(output));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    final Style style = new Style(facesContext, output);
    writer.writeStyleAttribute(style);
    final String dateInputId = (String) output.getAttributes().get(Attributes.DATE_INPUT_ID);
    if (dateInputId != null) {
      writer.writeAttribute(DataAttributes.DATE_INPUT_ID, dateInputId, false);
    }
    writer.writeAttribute(DataAttributes.DAY, Integer.toString(calendar.get(Calendar.YEAR)), false);
    writer.writeAttribute(DataAttributes.MONTH, Integer.toString(1 + calendar.get(Calendar.MONTH)), false);
    writer.writeAttribute(DataAttributes.YEAR, Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)), false);

    writer.writeAttribute(DataAttributes.FIRST_DAY_OF_WEEK, Integer.toString(calendar.getFirstDayOfWeek()), false);
    writer.writeAttribute(DataAttributes.MONTH_NAMES, getMonthNames(locale), false);

    // begin header
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(output, "row"));

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarFastPrev.png"), false);
    writer.writeAttribute(DataAttributes.COMMAND, "fastPrev", false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarPrev.png"), false);
    writer.writeAttribute(DataAttributes.COMMAND, "prev", false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(Classes.create(output, "header"));

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeAttribute(DataAttributes.COMMAND, "month", false);
    writer.writeText(new SimpleDateFormat("MMMMM", locale).format(calendar.getTime()));
    writer.endElement(HtmlElements.SPAN);

    writer.writeText(" "); // non breaking space

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeAttribute(DataAttributes.COMMAND, "year", false);
    writer.writeText(new SimpleDateFormat("yyyy", locale).format(calendar.getTime()));
    writer.endElement(HtmlElements.SPAN);

    writer.endElement(HtmlElements.SPAN);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarNext.png"), false);
    writer.writeAttribute(DataAttributes.COMMAND, "next", false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarFastNext.png"), false);
    writer.writeAttribute(DataAttributes.COMMAND, "fastNext", false);
    writer.endElement(HtmlElements.IMG);

    writer.endElement(HtmlElements.DIV);
    // end header

    // begin weeks
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(output, "row"));

    final SimpleDateFormat dayInWeekFormat = new SimpleDateFormat("E", locale);
    for (int dayIt = 0; dayIt < 7; ++dayIt) {
      final DateModel date = model.getDate(0, dayIt);
      String dayName = dayInWeekFormat.format(date.getCalendar().getTime());
      if (dayName != null && dayName.length() > 2) {
        dayName = dayName.substring(0, 2);
      }

      writer.startElement(HtmlElements.SPAN, null);
      writer.writeClassAttribute(Classes.create(output, "dayOfWeek"));
      writer.writeText(dayName);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.DIV);
    // end weeks

    // begin grid
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(output, "grid"));
    for (int week = 0; week < 6; ++week) {
      writer.startElement(HtmlElements.DIV, null);
      writer.writeClassAttribute(Classes.create(output, "row"));

      for (int dayIt = 0; dayIt < 7; ++dayIt) {
        final DateModel date = model.getDate(week, dayIt);
        final String dayDescription = String.valueOf(date.getDay());

        writer.startElement(HtmlElements.SPAN, null);
        writer.writeClassAttribute(
            Classes.create(output, "day", date.getMonth() == model.getMonth() ? null : Markup.DISABLED));

        writer.writeText(dayDescription);

        writer.endElement(HtmlElements.SPAN);
      }
      writer.endElement(HtmlElements.DIV);
    }
    writer.endElement(HtmlElements.DIV);
    // end grid

    writer.endElement(HtmlElements.DIV);
  }
  
  private String getMonthNames(final Locale locale) {
    final SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM", locale);
    final StringBuilder buffer = new StringBuilder(64);
    final Calendar calendar = Calendar.getInstance();
    calendar.set(2000, Calendar.JANUARY, 1);
    for (int month = 0; month < 12; ++month) {
      if (month > 0) {
        buffer.append(',');
      }
      buffer.append(dateFormat.format(calendar.getTime()));
      calendar.add(java.util.Calendar.MONTH, 1);
    }
    return buffer.toString();
  }

}
