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

package org.apache.myfaces.tobago.convert;

import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.ConverterException;
import java.lang.invoke.MethodHandles;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.apache.myfaces.tobago.convert.DateTimeConverter.CONVERTER_ID;

/**
 * @deprecated Since 5.0.0. Should work with &lt;f:convertDateTime%gt; since JSF 2.3.
 */
@Deprecated
@org.apache.myfaces.tobago.apt.annotation.Converter(id = CONVERTER_ID)
public class DateTimeConverter extends jakarta.faces.convert.DateTimeConverter {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  public static final String CONVERTER_ID = "org.apache.myfaces.tobago.DateTime";

  private static final String TYPE_DATE = "date";
  private static final String TYPE_TIME = "time";
  private static final String TYPE_BOTH = "both";
  private static final String TYPE_CALENDAR = "calendar";
  private static final String TYPE_LOCAL_DATE = "localDate";
  private static final String TYPE_LOCAL_TIME = "localTime";
  private static final String TYPE_LOCAL_DATE_TIME = "localDateTime";
  private static final String TYPE_OFFSET_TIME = "offsetTime";
  private static final String TYPE_OFFSET_DATE_TIME = "offsetDateTime";
  private static final String TYPE_ZONED_DATE_TIME = "zonedDateTime";

  @Override
  public Object getAsObject(FacesContext facesContext, UIComponent component, String string) throws ConverterException {
    if (StringUtils.isBlank(string)) {
      return null;
    } else {
      final String type = getType();
      if (TYPE_DATE.equals(type) || TYPE_TIME.equals(type) || TYPE_BOTH.equals(type)) {
        return super.getAsObject(facesContext, component, string);
      } else if (TYPE_CALENDAR.equals(type)) {
        final Locale locale = getLocale();
        final String pattern = getPattern();
        final TimeZone timeZone = getTimeZone();
        final Calendar calendar;
        if (component instanceof UIInput && ((UIInput) component).getValue() != null) {
          calendar = (Calendar) ((UIInput) component).getValue();
        } else {
          if (timeZone != null && locale != null) {
            calendar = Calendar.getInstance(timeZone, locale);
          } else if (locale != null) {
            calendar = Calendar.getInstance(locale);
          } else if (timeZone != null) {
            calendar = Calendar.getInstance(timeZone);
          } else {
            calendar = Calendar.getInstance();
          }
        }

        SimpleDateFormat sdf = locale != null ? new SimpleDateFormat(pattern, locale) : new SimpleDateFormat(pattern);
        try {
          calendar.setTime(sdf.parse(string));
        } catch (ParseException e) {
          throw new ConverterException("string='" + string + "'", e);
        }

        return calendar;
      } else if (TYPE_LOCAL_DATE.equals(type)) {
        logMandatoryPatterns("yMd");
        return getDateTimeFormatter().parse(string, LocalDate::from);
      } else if (TYPE_LOCAL_TIME.equals(type)) {
        logMandatoryPatterns("H");
        return getDateTimeFormatter().parse(string, LocalTime::from);
      } else if (TYPE_LOCAL_DATE_TIME.equals(type)) {
        logMandatoryPatterns("yMdH");
        return getDateTimeFormatter().parse(string, LocalDateTime::from);
      } else if (TYPE_OFFSET_TIME.equals(type)) {
        logMandatoryPatterns("HZ");
        return getDateTimeFormatter().parse(string, OffsetTime::from);
      } else if (TYPE_OFFSET_DATE_TIME.equals(type)) {
        logMandatoryPatterns("yMdHZ");
        return getDateTimeFormatter().parse(string, OffsetDateTime::from);
      } else if (TYPE_ZONED_DATE_TIME.equals(type)) {
        logMandatoryPatterns("yMdHZ");
        return getDateTimeFormatter().parse(string, ZonedDateTime::from);
      } else {
        throw new ConverterException("invalid type '" + type + "'");
      }
    }
  }

  @Override
  public String getAsString(FacesContext facesContext, UIComponent component, Object object) throws ConverterException {
    if (object == null) {
      return null;
    } else {
      final String type = getType();
      if (TYPE_DATE.equals(type) || TYPE_TIME.equals(type) || TYPE_BOTH.equals(type)) {
        return super.getAsString(facesContext, component, object);
      } else if (TYPE_CALENDAR.equals(type)) {
        Calendar calendar = (Calendar) object;
        final Locale locale = getLocale();
        final String pattern = getPattern();

        SimpleDateFormat sdf = new SimpleDateFormat(pattern, locale);
        return sdf.format(calendar.getTime());
      } else if (TYPE_LOCAL_DATE.equals(type)
          || TYPE_LOCAL_TIME.equals(type)
          || TYPE_LOCAL_DATE_TIME.equals(type)
          || TYPE_OFFSET_TIME.equals(type)
          || TYPE_OFFSET_DATE_TIME.equals(type)
          || TYPE_ZONED_DATE_TIME.equals(type)) {
        return getDateTimeFormatter().format((TemporalAccessor) object);
      } else {
        throw new ConverterException("invalid type '" + type + "'");
      }
    }
  }

  private void logMandatoryPatterns(String mandatoryChars) {
    logMandatoryPattern(mandatoryChars, "y", "year");
    logMandatoryPattern(mandatoryChars, "M", "month");
    logMandatoryPattern(mandatoryChars, "d", "day");
    logMandatoryPattern(mandatoryChars, "H", "hour");
    logMandatoryPattern(mandatoryChars, "Z", "offset");
  }

  private void logMandatoryPattern(String mandatoryChars, String c, String name) {
    final String pattern = getPattern();
    if (pattern != null && mandatoryChars.contains(c) && !pattern.contains(c)) {
      LOG.error("No char for " + name + " ('" + c + "') in pattern: " + pattern);
    }
  }

  private DateTimeFormatter getDateTimeFormatter() {
    final String pattern = getPattern();
    if (!StringUtils.isBlank(pattern)) {
      final Locale locale = getLocale();
      return locale != null ? DateTimeFormatter.ofPattern(pattern, locale) : DateTimeFormatter.ofPattern(pattern);
    } else {
      throw new ConverterException("no pattern set");
    }
  }
}
