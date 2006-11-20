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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_CALENDAR_DATE_INPUT_ID;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.model.CalendarModel;
import org.apache.myfaces.tobago.model.DateModel;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CalendarRenderer extends RendererBase {


  private static final Log LOG = LogFactory.getLog(CalendarRenderer.class);


  private static final String[] SCRIPTS = {
        "script/calendar.js",
        "script/dateConverter.js"
    };


  public void encodeEnd(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIOutput output = (UIOutput) component;
    UIPage page = ComponentUtil.findPage(output);
    for (String script : SCRIPTS) {
      page.getScriptFiles().add(script);      
    }

    String id = output.getClientId(facesContext);

    String dateTextBoxId = (String) component.getAttributes().get(ATTR_CALENDAR_DATE_INPUT_ID);

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

    // rendering:

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.TABLE, component);
    writer.writeIdAttribute(id);
    writer.writeComponentClass();
    writer.writeAttribute(HtmlAttributes.CELLSPACING, "0", null);
    writer.writeAttribute(HtmlAttributes.CELLPADDING, "3", null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);

    writer.startElement(HtmlConstants.TR, null);
    writer.writeClassAttribute("tobago-calendar-header-tr");
    writer.startElement(HtmlConstants.TH, null);
    writer.writeAttribute(HtmlAttributes.COLSPAN, "7", null);

    writer.startElement(HtmlConstants.TABLE, null);
    writer.writeAttribute(HtmlAttributes.SUMMARY, "", null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.startElement(HtmlConstants.TR, null);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "left", null);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", null);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarFastPrev.gif"), null);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', -12)", null);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "left", null);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", null);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarPrev.gif"), null);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', -1)", null);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TH, null);
    writer.writeClassAttribute("tobago-calendar-header-center");
    writer.writeAttribute(HtmlAttributes.ALIGN, "center", null);
    writer.writeIdAttribute(id + ":title");
    writer.writeText(dateFormat.format(calendar.getTime()), null);
    writer.endElement(HtmlConstants.TH);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "right", null);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", null);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarNext.gif"), null);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', 1)", null);
    writer.endElement(HtmlConstants.IMG);
    writer.endElement(HtmlConstants.TD);

    writer.startElement(HtmlConstants.TD, null);
    writer.writeAttribute(HtmlAttributes.ALIGN, "right", null);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute(HtmlAttributes.ALT, "", null);
    writer.writeAttribute(HtmlAttributes.SRC,
        ResourceManagerUtil.getImageWithPath(facesContext, "image/calendarFastNext.gif"), null);
    writer.writeAttribute(HtmlAttributes.ONCLICK, "addMonth('" + id + "', 12)", null);
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
      writer.writeText(dayName, null);
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
        writer.writeAttribute(HtmlAttributes.ONCLICK, onclick, null);
        writer.writeIdAttribute(id + ":" + week + ":" + dayIt);
        writer.writeClassAttribute(getClass(date, model));

        writer.writeText(dayDescription, null);

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
    HtmlRendererUtil.writeScriptLoader(facesContext, SCRIPTS, cmd);
  }
  
  private void writeInputHidden(TobagoResponseWriter writer,
       String id, Object value) throws IOException {
    writeInputHidden(writer, null, id, value);
   }

  private void writeInputHidden(TobagoResponseWriter writer, String name,
      String id, Object value) throws IOException {
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", null);
    if (name != null) {
      writer.writeNameAttribute(name);
    }
    writer.writeIdAttribute(id);
    writer.writeAttribute(HtmlAttributes.VALUE, value, null);
    writer.endElement(HtmlConstants.INPUT);
  }

  private String getClass(DateModel date, CalendarModel model) {
    return (date.getMonth() == model.getMonth())
        ? "tobago-calendar-day"
        : "tobago-calendar-day-disabled";
  }

  private String getMonthNames(Locale locale) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM", locale);
    StringBuilder buffer = new StringBuilder();
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

