/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.model.CalendarModel;
import com.atanion.model.DateModel;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.taglib.component.CalendarTag;
import com.atanion.util.CalendarUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.ServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(CalendarRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIPage page = ComponentUtil.findPage(component);
    page.getScriptFiles().add("calendar.js", true);
    page.getScriptFiles().add("dateConverter.js", true);

    String id = component.getId();

    ServletRequest request
        = (ServletRequest) facesContext.getExternalContext().getRequest();
    if (LOG.isDebugEnabled()) {
      LOG.debug(request.getParameterMap());
    }
    String dateTextBoxId = request.getParameter("tobago.date.inputId");
    if (dateTextBoxId != null) {
      page.getOnloadScripts().add("initCalendarParse('"
          + id + "', '" + dateTextBoxId + "');");
    }

    Locale locale = facesContext.getViewRoot().getLocale();
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM yyyy", locale);

    // fixme don't use the concept of setting year,month,day explicit,
    // fixme but use a date-model as background-value instead
    Integer year = (Integer) component.getAttributes().get(CalendarTag.YEAR);
    Integer month = (Integer) component.getAttributes().get(CalendarTag.MONTH);
    Integer day = (Integer) component.getAttributes().get(CalendarTag.DAY);
    Calendar calendar = CalendarUtils.getCalendar(
        year != null ? year : new Integer(1970),
        month != null ? month : new Integer(1),
        day != null ? day : new Integer(1), locale);
    CalendarModel model = new CalendarModel(calendar);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("table", component);
    writer.writeAttribute("id", id, null);
    // todo: use created standard classes
    //writer.writeAttribute("class", null, "class");
    writer.writeAttribute("class", "calendar", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "3", null);
    writer.writeAttribute("summary", "", null);

    writer.startElement("tr", null);
    writer.startElement("th", null);
    writer.writeAttribute("colspan", "7", null);

    writer.startElement("table", null);
    writer.writeAttribute("summary", "", null);
    writer.writeAttribute("width", "100%", null);
    writer.startElement("tr", null);

    writer.startElement("td", null);
    writer.writeAttribute("align", "left", null);
    writer.startElement("img", null);
    writer.writeAttribute("style", "cursor: pointer", null);
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "fastPrev.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', -12)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.startElement("td", null);
    writer.writeAttribute("align", "left", null);
    writer.startElement("img", null);
    writer.writeAttribute("style", "cursor: pointer", null);
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "prev.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', -1)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.startElement("th", null);
    writer.writeAttribute("align", "center", null);
    writer.writeAttribute("id", id + ":title", null);
    writer.writeText(dateFormat.format(calendar.getTime()), null);
    writer.endElement("th");

    writer.startElement("td", null);
    writer.writeAttribute("align", "right", null);
    writer.startElement("img", null);
    writer.writeAttribute("style", "cursor: pointer", null);
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "next.gif"), null);
    writer.writeAttribute("onclick", "addMonth('" + id + "', 1)", null);
    writer.endElement("img");
    writer.endElement("td");

    writer.startElement("td", null);
    writer.writeAttribute("align", "right", null);
    writer.startElement("img", null);
    writer.writeAttribute("style", "cursor: pointer", null);
    writer.writeAttribute("alt", "", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "fastNext.gif"), null);
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
      writer.writeText(dayName, null);
      writer.endElement("th");
    }

    writer.endElement("tr");

//    int weekCount = model.getWeekCount();
    for (int week = 0; week < 6; ++week) {
//    String style = (week < weekCount) ? "" : "style=\"display: none\"";
      writer.startElement("tr", null);
      writer.writeAttribute("id", id + ":" + week, null);
//      writer.writeAttribute("style", style, null);

      for (int dayIt = 0; dayIt < 7; ++dayIt) {
//      if (week < weekCount) {
        DateModel date = model.getDate(week, dayIt);
        String dayDescription = String.valueOf(date.getDay());
        String onclick = "selectDay('" + id + "', " + week +" , " + dayIt + ");";

        writer.startElement("td", null);
        writer.writeAttribute("onclick", onclick, null);
        writer.writeAttribute("id", id + ":" + week + ":" + dayIt, null);
        writer.writeAttribute("class", getClass(date, model), null);
        writer.writeAttribute("style", "cursor: pointer", null);

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
    writer.writeAttribute("name", "/" + id + "/year", null);
    writer.writeAttribute("id", id + ":year", null);
    writer.writeAttribute("value", year, null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", "/" + id + "/month", null);
    writer.writeAttribute("id", id + ":month", null);
    writer.writeAttribute("value", month, null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("name", "/" + id + "/day", null);
    writer.writeAttribute("id", id + ":day", null);
    writer.writeAttribute("value", day, null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("id", id + ":firstDayOfWeek", null);
    writer.writeAttribute("value", Integer.toString(calendar.getFirstDayOfWeek()), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("id", id + ":monthNames", null);
    writer.writeAttribute("value", getMonthNames(locale), null);
    writer.endElement("input");

    writer.startElement("input", null);
    writer.writeAttribute("type", "hidden", null);
    writer.writeAttribute("id", id + ":fieldId", null);
    writer.writeAttribute("value", "", null);
    writer.endElement("input");

    writer.startElement("script", null);
    writer.writeAttribute("tyle", "text/javascript", null);
    writer.writeText("document.calendar = new Object();", null);
    writer.endElement("script");
  }


  private static String getClass(DateModel date, CalendarModel model) {
    return (date.getMonth() == model.getMonth()) ? "day" : "day-disabled";
  }

  private static String getMonthNames(Locale locale) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMMM", locale);
    StringBuffer buffer = new StringBuffer();
    java.util.Calendar calendar = CalendarUtils.getCalendar(2000, 1, 1);
    for (int month=0; month < 12; ++month) {
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

