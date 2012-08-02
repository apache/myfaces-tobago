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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICalendar;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.model.CalendarModel;
import org.apache.myfaces.tobago.model.DateModel;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(CalendarRenderer.class);


  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UICalendar output = (UICalendar) component;
    String id = output.getClientId(facesContext);
    String dateTextBoxId = (String) component.getAttributes().get(Attributes.DATE_INPUT_ID);

    if (LOG.isDebugEnabled()) {
      LOG.debug("dateTextBoxId = '" + dateTextBoxId + "'");
    }

    Locale locale = facesContext.getViewRoot().getLocale();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy", locale);

    Object value = output.getValue();
    Calendar calendar;
    if (value instanceof Calendar) {
      calendar = (Calendar) value;
    } else { 
      calendar = new GregorianCalendar();
      if (value instanceof Date) {
        calendar.setTime((Date) value);
      }
    }
    CalendarModel model = new CalendarModel(calendar);

    // rendering
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, component);
    writer.writeIdAttribute(id);
    writer.writeClassAttribute(Classes.create(output));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    Style style = new Style(facesContext, output);
    writer.writeStyleAttribute(style);

    // begin header
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(output, "row"));

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarFastPrev.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', -12)", false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarPrev.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', -1)", false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeIdAttribute(id + ":title"); // todo: ComponentUtils.SUB_SEPARATOR
    writer.writeText(dateFormat.format(calendar.getTime()));
    writer.endElement(HtmlElements.SPAN);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarNext.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', 1)", false);
    writer.endElement(HtmlElements.IMG);

    writer.startElement(HtmlElements.IMG, null);
    writer.writeClassAttribute(Classes.create(output, "header"));
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtils.getImageWithPath(facesContext, "image/calendarFastNext.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', 12)", false);
    writer.endElement(HtmlElements.IMG);

    writer.endElement(HtmlElements.DIV);
    // end header

    // begin weeks
    writer.startElement(HtmlElements.DIV, null);
    writer.writeClassAttribute(Classes.create(output, "row"));

    dateFormat = new SimpleDateFormat("E", locale);
    for (int dayIt = 0; dayIt < 7; ++dayIt) {
      DateModel date = model.getDate(0, dayIt);
      String dayName = dateFormat.format(date.getCalendar().getTime());
      dayName = StringUtils.substring(dayName, 0, 2);

      writer.startElement(HtmlElements.SPAN, null);
      writer.writeClassAttribute(Classes.create(output, "dayOfWeek"));
      writer.writeText(dayName);
      writer.endElement(HtmlElements.SPAN);
    }

    writer.endElement(HtmlElements.DIV);
    // end weeks

//    int weekCount = model.getWeekCount();
    for (int week = 0; week < 6; ++week) {
//    String style = (week < weekCount) ? "" : "style=\"display: none\"";
      writer.startElement(HtmlElements.DIV, null);
      writer.writeIdAttribute(id + ":" + week);
      writer.writeClassAttribute(Classes.create(output, "row"));
//      writer.writeAttribute(HtmlAttributes.STYLE, style, null);

      for (int dayIt = 0; dayIt < 7; ++dayIt) {
//      if (week < weekCount) {
        DateModel date = model.getDate(week, dayIt);
        String dayDescription = String.valueOf(date.getDay());
        String onclick = "selectDay('" + id + "', " + week + " , " + dayIt + ");";

        writer.startElement(HtmlElements.SPAN, null);
        writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, true);
        writer.writeIdAttribute(id + ":" + week + ":" + dayIt);
        writer.writeClassAttribute(
            Classes.create(output, "day", date.getMonth() == model.getMonth() ? null : Markup.DISABLED));

        writer.writeText(dayDescription);

        writer.endElement(HtmlElements.SPAN);

//      } else {
//        % ><td id="< %= id + ":" + week + ":" + day % >">x</td>< %
//      }
      }
      writer.endElement(HtmlElements.DIV);
    }
    writer.endElement(HtmlElements.DIV);

    writeInputHidden(writer, "/" + id + "/year", id + ":year", Integer.toString(calendar.get(Calendar.YEAR)));

    writeInputHidden(writer, "/" + id + "/month", id + ":month", Integer.toString(1 + calendar.get(Calendar.MONTH)));

    writeInputHidden(writer, "/" + id + "/day", id + ":day", Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));

    writeInputHidden(writer, id + ":firstDayOfWeek", Integer.toString(calendar.getFirstDayOfWeek()));

    writeInputHidden(writer, id + ":monthNames", getMonthNames(locale));

    writeInputHidden(writer, id + ":fieldId", "");

    String[] cmd = {
        "document.calendar = new Object();",
        dateTextBoxId != null
            ? "initCalendarParse('" + id + "', '" + dateTextBoxId + "');"
            : ""
    };
    HtmlRendererUtils.writeScriptLoader(facesContext, null, cmd);
  }
  
  private void writeInputHidden(TobagoResponseWriter writer,
       String id, String value) throws IOException {
    writeInputHidden(writer, null, id, value);
   }

  private void writeInputHidden(TobagoResponseWriter writer, String name,
      String id, String value) throws IOException {
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
    if (name != null) {
      writer.writeNameAttribute(name);
    }
    writer.writeIdAttribute(id);
    writer.writeAttribute(HtmlAttributes.VALUE, value, true);
    writer.endElement(HtmlElements.INPUT);
  }

  private String getMonthNames(Locale locale) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM", locale);
    StringBuilder buffer = new StringBuilder(64);
    Calendar calendar = Calendar.getInstance();
    calendar.set(2000, 0, 1);
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

