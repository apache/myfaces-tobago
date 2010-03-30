package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICalendar;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.model.CalendarModel;
import org.apache.myfaces.tobago.model.DateModel;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarRenderer extends LayoutComponentRendererBase {

  private static final Log LOG = LogFactory.getLog(CalendarRenderer.class);

  private static final String[] SCRIPTS = {
        "script/calendar.js",
        "script/dateConverter.js"
    };

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getScriptFiles().addAll(Arrays.asList(SCRIPTS));
    }
  }

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

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeIdAttribute(id);
    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute();
    Style style = new Style(facesContext, output);
    writer.writeStyleAttribute(style);
    writer.writeAttribute(HtmlAttributes.CELLSPACING, 0);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, 3);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeClassAttribute("tobago-calendar-header-tr");
    writer.startElement(HtmlConstants.TH, null);
    writer.writeAttribute(HtmlAttributes.COLSPAN, 7);

    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", false);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.startElement(HtmlConstants.TR, null);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "left", false);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarFastPrev.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', -12)", false);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "left", false);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarPrev.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', -1)", false);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TH, null);
    writer.writeClassAttribute("tobago-calendar-header-center");
    writer.writeAttribute(HtmlAttributes.ALIGN, "center", false);
    writer.writeIdAttribute(id + ":title");
    writer.writeText(dateFormat.format(calendar.getTime()));
    writer.endElement(HtmlConstants.TH);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "right", false);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarNext.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', 1)", false);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "right", false);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", false);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarFastNext.gif"), false);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', 12)", false);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.endElement(HtmlConstants.TR);
    writer.endElement(HtmlConstants.TABLE);

    writer.endElement(HtmlConstants.TH);
    writer.endElement(HtmlConstants.TR);

    writer.startElement(HtmlConstants.TR, null);

    dateFormat = new SimpleDateFormat("E", locale);
    for (int dayIt = 0; dayIt < 7; ++dayIt) {
      DateModel date = model.getDate(0, dayIt);
      String dayName = dateFormat.format(date.getCalendar().getTime());
      dayName = StringUtils.substring(dayName, 0, 2);

      writer.startElement(HtmlConstants.TH, null);
      writer.writeClassAttribute("tobago-calendar-inner-header");
      writer.writeText(dayName);
      writer.endElement(HtmlConstants.TH);
    }

    writer.endElement(HtmlConstants.TR);

//    int weekCount = model.getWeekCount();
    for (int week = 0; week < 6; ++week) {
//    String style = (week < weekCount) ? "" : "style=\"display: none\"";
      writer.startElement(HtmlConstants.TR, null);
      writer.writeIdAttribute(id + ":" + week);
//      writer.writeAttribute(HtmlAttributes.STYLE, style, null);

      for (int dayIt = 0; dayIt < 7; ++dayIt) {
//      if (week < weekCount) {
        DateModel date = model.getDate(week, dayIt);
        String dayDescription = String.valueOf(date.getDay());
        String onclick = "selectDay('" + id + "', " + week + " , " + dayIt + ");";

        writer.startElement(HtmlConstants.TD, null);
        writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, true);
        writer.writeIdAttribute(id + ":" + week + ":" + dayIt);
        writer.writeClassAttribute(getClass(date, model));

        writer.writeText(dayDescription);

        writer.endElement(HtmlConstants.TD);

//      } else {
//        % ><td id="< %= id + ":" + week + ":" + day % >">x</td>< %
//      }
      }
      writer.endElement(HtmlConstants.TR);
    }
    writer.endElement(HtmlConstants.TABLE);

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
    HtmlRendererUtils.writeScriptLoader(facesContext, SCRIPTS, cmd);
  }
  
  private void writeInputHidden(TobagoResponseWriter writer,
       String id, String value) throws IOException {
    writeInputHidden(writer, null, id, value);
   }

  private void writeInputHidden(TobagoResponseWriter writer, String name,
      String id, String value) throws IOException {
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    if (name != null) {
      writer.writeNameAttribute(name);
    }
    writer.writeIdAttribute(id);
    writer.writeAttribute(HtmlAttributes.VALUE, value, true);
    writer.endElement(HtmlConstants.INPUT);
  }

  private String getClass(DateModel date, CalendarModel model) {
    return (date.getMonth() == model.getMonth())
        ? "tobago-calendar-day"
        : "tobago-calendar-day-disabled";
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

