/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.model.DateModel;
import com.atanion.tobago.model.CalendarModel;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CalendarRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIOutput output = (UIOutput) component;
    UIPage page = ComponentUtil.findPage(output);
    page.getScriptFiles().add("script/calendar.js");
    page.getScriptFiles().add("script/dateConverter.js");

    String id = output.getClientId(facesContext);

//    String dateTextBoxId = (String) facesContext.getExternalContext()
//        .getRequestParameterMap().get("tobago.date.inputId");
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

    writer.startElement("table", component);
    writer.writeIdAttribute(id);
    writer.writeComponentClass();
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "3", null);
    writer.writeAttribute("summary", "", null);

    writer.startElement("tr", null);
    writer.writeClassAttribute("tobago-calendar-header-tr");
    writer.startElement("th", null);
    writer.writeAttribute("colspan", "7", null);

    writer.startElement("table", null);
    writer.writeAttribute("summary", "", null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.startElement("tr", null);

    writer.startElement("td", null);
    writer.writeAttribute("align", "left", null);
    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "image/calendarFastPrev.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', -12)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.startElement("td", null);
    writer.writeAttribute("align", "left", null);
    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "image/calendarPrev.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', -1)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.startElement("th", null);
    writer.writeClassAttribute("tobago-calendar-header-center");
    writer.writeAttribute("align", "center", null);
    writer.writeIdAttribute(id + ":title");
    writer.writeText(dateFormat.format(calendar.getTime()), null);
    writer.endElement("th");

    writer.startElement("td", null);
    writer.writeAttribute("align", "right", null);
    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "image/calendarNext.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', 1)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.startElement("td", null);
    writer.writeAttribute("align", "right", null);
    writer.startElement("img", null);
    writer.writeClassAttribute("tobago-calendar-header");
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "image/calendarFastNext.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', 12)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.endElement("tr");
    writer.endElement("table");

    writer.endElement("th");
    writer.endElement("tr");

    writer.startElement("tr", null);

    dateFormat = new SimpleDateFormat("E", locale);
    for (int dayIt = 0; dayIt < 7; ++dayIt) {
      DateModel date = model.getDate(0, dayIt);
      String dayName = dateFormat.format(date.getCalendar().getTime());
      dayName = StringUtils.substring(dayName, 0, 2);

      writer.startElement("th", null);
      writer.writeClassAttribute("tobago-calendar-inner-header");
      writer.writeText(dayName, null);
      writer.endElement("th");
    }

    writer.endElement("tr");

//    int weekCount = model.getWeekCount();
    for (int week = 0; week < 6; ++week) {
//    String style = (week < weekCount) ? "" : "style=\"display: none\"";
      writer.startElement("tr", null);
      writer.writeIdAttribute(id + ":" + week);
//      writer.writeAttribute("style", style, null);

      for (int dayIt = 0; dayIt < 7; ++dayIt) {
//      if (week < weekCount) {
        DateModel date = model.getDate(week, dayIt);
        String dayDescription = String.valueOf(date.getDay());
        String onclick = "selectDay('" + id + "', " + week + " , " + dayIt + ");";

        writer.startElement("td", null);
        writer.writeAttribute("onclick", onclick, null);
        writer.writeIdAttribute(id + ":" + week + ":" + dayIt);
        writer.writeClassAttribute(getClass(date, model));

        writer.writeText(dayDescription, null);

        writer.endElement("td");

//      } else {
//        % ><td id="< %= id + ":" + week + ":" + day % >">x</td>< %
//      }
      }
      writer.endElement("tr");
    }
    writer.endElement("table");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute("/" + id + "/year");
    writer.writeIdAttribute(id + ":year");
    writer.writeAttribute("value", new Integer(calendar.get(Calendar.YEAR)), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute("/" + id + "/month");
    writer.writeIdAttribute(id + ":month");
    writer.writeAttribute("value", new Integer(1 + calendar.get(Calendar.MONTH)), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeNameAttribute("/" + id + "/day");
    writer.writeIdAttribute(id + ":day");
    writer.writeAttribute("value", new Integer(calendar.get(Calendar.DAY_OF_MONTH)), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeIdAttribute(id + ":firstDayOfWeek");
    writer.writeAttribute("value", Integer.toString(calendar.getFirstDayOfWeek()), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeIdAttribute(id + ":monthNames");
    writer.writeAttribute("value", getMonthNames(locale), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeIdAttribute(id + ":fieldId");
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

//    HtmlRendererUtil.writeJavascript(writer, "document.calendar = new Object();");
    HtmlRendererUtil.startJavascript(writer);
    writer.writeText("document.calendar = new Object();", null);

    if (dateTextBoxId != null) {
//      page.getOnloadScripts().add("initCalendarParse('"
//          + id + "', '" + dateTextBoxId + "');");
       writer.writeText(
           "initCalendarParse('" + id + "', '" + dateTextBoxId + "');", null);
    }
    HtmlRendererUtil.endJavascript(writer);
//    writer.startElement("script", null);
//    writer.writeAttribute("tyle", "text/javascript", null);
//    writer.writeText("document.calendar = new Object();", null);
//    writer.endElement("script");
  }


  private static String getClass(DateModel date, CalendarModel model) {
    return (date.getMonth() == model.getMonth())
        ? "tobago-calendar-day"
        : "tobago-calendar-day-disabled";
  }

  private static String getMonthNames(Locale locale) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM", locale);
    StringBuffer buffer = new StringBuffer();
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

// ///////////////////////////////////////////// bean getter + setter

}

