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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.component.AbstractUIDate;
import org.apache.myfaces.tobago.internal.context.DateTimeI18n;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.internal.util.JsonUtils;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.CustomAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateRenderer<T extends AbstractUIDate> extends MessageLayoutRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String TYPE_DATE = "date";
  private static final String TYPE_TIME = "time";
  private static final String TYPE_BOTH = "both";
  private static final String TYPE_LOCAL_DATE = "localDate";
  private static final String TYPE_LOCAL_TIME = "localTime";
  private static final String TYPE_LOCAL_DATE_TIME = "localDateTime";
  private static final String TYPE_OFFSET_TIME = "offsetTime";
  private static final String TYPE_OFFSET_DATE_TIME = "offsetDateTime";
  private static final String TYPE_ZONED_DATE_TIME = "zonedDateTime";
  private static final String TYPE_MONTH = "month";
  private static final String TYPE_WEEK = "week";

  private static final String PATTERN_DATE = "yyyy-MM-dd";
  private static final String PATTERN_DATETIME = "yyyy-MM-dd'T'HH:mm:ss.SSS";
  private static final String PATTERN_TIME = "HH:mm:ss.SSS";
  private static final String PATTERN_MONTH = "yyyy-MM";
  private static final String PATTERN_WEEK = "yyyy-'W'MM";

  @Override
  public void encodeBeginInternal(FacesContext facesContext, T component) throws IOException {
    prepare(facesContext, component);
    super.encodeBeginInternal(facesContext, component);
  }

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public HtmlElements getComponentTag() {
    return HtmlElements.TOBAGO_DATE;
  }

  @Override
  protected void writeAdditionalAttributes(
      final FacesContext facesContext, final TobagoResponseWriter writer, final T date)
      throws IOException {

    super.writeAdditionalAttributes(facesContext, writer, date);
//    writer.writeAttribute(HtmlAttributes.PATTERN,
    final DateTimeI18n dateTimeI18n = DateTimeI18n.valueOf(facesContext.getViewRoot().getLocale());
    writer.writeAttribute(CustomAttributes.I18N, JsonUtils.encode(dateTimeI18n), true);
    writer.writeAttribute(CustomAttributes.TODAY_BUTTON, date.isTodayButton());
  }

  @Override
  protected void encodeBeginField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(TobagoClass.INPUT__GROUP__OUTER);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);

    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, component);
    final HtmlInputTypes type = component.getType();

    final String currentValue = getCurrentValue(facesContext, component);
    final String clientId = component.getClientId(facesContext);
    final String fieldId = component.getFieldId(facesContext);
    final boolean readonly = component.isReadonly();
    final boolean disabled = component.isDisabled();
    final boolean required = ComponentUtils.getBooleanAttribute(component, Attributes.required);

    writer.startElement(HtmlElements.INPUT);

    if (component.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(component.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, component.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, type.getValue(), false);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, component);
    writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());
//    if (!disabled && !readonly) {
//      writer.writeAttribute(HtmlAttributes.PLACEHOLDER, component.getPlaceholder(), true);
//    }
    writer.writeAttribute(HtmlAttributes.MIN, convertToString(component.getMin()), true);
    writer.writeAttribute(HtmlAttributes.MAX, convertToString(component.getMax()), true);

    writer.writeClassAttribute(
        BootstrapClass.borderColor(ComponentUtils.getMaximumSeverity(component)),
        BootstrapClass.FORM_CONTROL,
        component.getCustomClass());

    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    renderFocus(clientId, component.isFocus(), component.isError(), facesContext, writer);

    writer.endElement(HtmlElements.INPUT);

    encodeBehavior(writer, facesContext, component);

//    if (type.supportsDate()) {
//      encodeButton(facesContext, component, Icons.CALENDAR3);
//    }
//    if (type.supportsTime()) {
//      encodeButton(facesContext, component, Icons.CLOCK);
//    }
  }

  private String convertToString(Object value) {
    if (value == null) {
      return null;
    } else if (value instanceof String) {
      return (String) value;
    } else if (value instanceof LocalDate) {
      return ((LocalDate) value).format(DateTimeFormatter.ISO_LOCAL_DATE);
    } else {
      LOG.warn("Unknown type for min/max: '{}'", value);
      return value.toString();
    }
  }

  private void encodeButton(final FacesContext facesContext, final T component, final Icons icon)
      throws IOException {

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

    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(icon);
    writer.endElement(HtmlElements.I);

    writer.endElement(HtmlElements.BUTTON);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }

  /**
   * Creates a converter (if not defined any) which satifies the requirements of HTML5 like described here:
   * <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Date_and_time_formats">MDN</a>
   */
  @Override
  protected Converter getConverter(FacesContext facesContext, T component, Object value) {
    final Converter converter = component.getConverter();
    if (converter != null) {
      // todo: should we warn here, if the type is not "text"?
      return converter;
    } else {
      Class<?> estimatedType = estimateValueType(facesContext, component);
      if (estimatedType == null) {
        // todo: log debug instead of warn
        LOG.warn("Can't estimate type (clientId='{}')!", component.getClientId(facesContext));
        return null;
      } else if (estimatedType.isAssignableFrom(String.class)) {
        // todo: log debug instead of warn
        LOG.warn("No converter for java.lang.String");
        return null;
      } else {
        final DateTimeConverter dateTimeConverter
            = (DateTimeConverter) facesContext.getApplication().createConverter("javax.faces.DateTime");
        if (estimatedType.isAssignableFrom(LocalDateTime.class)) {
          dateTimeConverter.setType("localDateTime");
          dateTimeConverter.setPattern(PATTERN_DATETIME);
        } else if (estimatedType.isAssignableFrom(LocalDate.class)) {
          dateTimeConverter.setType("localDate");
          dateTimeConverter.setPattern(PATTERN_DATE);
        } else if (estimatedType.isAssignableFrom(LocalTime.class)) {
          dateTimeConverter.setType("localTime");
          dateTimeConverter.setPattern(PATTERN_TIME);
        } else if (estimatedType.isAssignableFrom(ZonedDateTime.class)) {
          dateTimeConverter.setType("zonedDateTime");
          dateTimeConverter.setPattern(PATTERN_DATETIME);
        } else if (estimatedType.isAssignableFrom(Long.class)) {
          dateTimeConverter.setType("date");
          dateTimeConverter.setPattern(PATTERN_DATE);
        } else if (estimatedType.isAssignableFrom(Date.class)) {
          dateTimeConverter.setType("date");
          dateTimeConverter.setPattern(PATTERN_DATE);
        } else if (estimatedType.isAssignableFrom(Calendar.class)) {
          dateTimeConverter.setType("date");
          dateTimeConverter.setPattern(PATTERN_DATE);
        } else if (estimatedType.isAssignableFrom(Number.class)) {
          LOG.error("date");
          dateTimeConverter.setType("date");
          dateTimeConverter.setPattern(PATTERN_DATE);
//      } else if (estimatedType.isAssignableFrom(Month.class)) {
//        LOG.error("month");
//        dateTimeConverter.setType("month"); // XXX is there a type month?
//      } else if (estimatedType.isAssignableFrom(Week.class)) {
//        LOG.error("week");
//        dateTimeConverter.setType("week"); // XXX is there a type week?
          // todo: ZonedDateTime
        } else {
          LOG.warn("Type might not be supported (type='{}' clientId='{}')!",
              estimatedType.getName(), component.getClientId(facesContext));
          throw new RuntimeException();
        }
        if (LOG.isDebugEnabled()) {
          LOG.debug("type='{}' pattern='{}'", dateTimeConverter.getType(), dateTimeConverter.getPattern());
        }

        return dateTimeConverter;
      }
    }
  }

  private Class<?> estimateValueType(final FacesContext facesContext, final T component) {
    Class<?> estimatedType = null;
    final ValueExpression valueExpression = component.getValueExpression("value");
    if (valueExpression != null) {
      try {
        estimatedType = valueExpression.getType(facesContext.getELContext());
      } catch (final Exception e) {
        // ignore, seems not to be possible, when EL is a function like #{bean.getName(item.id)}
      }
    }
    if (estimatedType == null) {
      Object submittedValue = component.getSubmittedValue();
      if (submittedValue != null) {
        estimatedType = submittedValue.getClass();
      }
    }
    if (estimatedType == null) {
      Object value = component.getValue();
      if (value != null) {
        estimatedType = value.getClass();
      }
    }
    return estimatedType;
  }

  private void prepare(final FacesContext facesContext, final T component) {
    HtmlInputTypes type = component.getType();
    String pattern = component.getPattern();

    DateTimeConverter converter = null;
    if (type == null || pattern == null) {
      Converter someConverter = getConverter(facesContext, component, component.getSubmittedValue()); // XXX submitted?
      if (someConverter instanceof DateTimeConverter) {
        converter = (DateTimeConverter) someConverter;
      }
    }

    if (type == null) {
      if (converter != null) {
        final String typeFromConverter = converter.getType();
        if (TYPE_DATE.equals(typeFromConverter)) {
          type = HtmlInputTypes.DATE;
        } else if (TYPE_BOTH.equals(typeFromConverter)) {
          type = HtmlInputTypes.DATETIME_LOCAL;
        } else if (TYPE_TIME.equals(typeFromConverter)) {
          type = HtmlInputTypes.TIME;
        } else if (TYPE_LOCAL_DATE.equals(typeFromConverter)) {
          type = HtmlInputTypes.DATE;
        } else if (TYPE_LOCAL_DATE_TIME.equals(typeFromConverter)) {
          type = HtmlInputTypes.DATETIME_LOCAL;
        } else if (TYPE_LOCAL_TIME.equals(typeFromConverter)) {
          type = HtmlInputTypes.TIME;
        } else if (TYPE_ZONED_DATE_TIME.equals(typeFromConverter)) {
          type = HtmlInputTypes.DATETIME_LOCAL;
        } else if (TYPE_OFFSET_DATE_TIME.equals(typeFromConverter)) {
          type = HtmlInputTypes.DATETIME_LOCAL;
        } else if (TYPE_OFFSET_TIME.equals(typeFromConverter)) {
          type = HtmlInputTypes.TIME;
        } else if (TYPE_MONTH.equals(typeFromConverter)) {
          type = HtmlInputTypes.MONTH;
        } else if (TYPE_WEEK.equals(typeFromConverter)) {
          type = HtmlInputTypes.WEEK;
        } else { // unknown type
          type = HtmlInputTypes.DATE;
        }
      } else { // no converter
        type = HtmlInputTypes.DATE;
      }
    }

    if (pattern == null && converter != null) {
      pattern = converter.getPattern();
    }

    if (pattern == null) {
      switch (type) {
        case DATE:
          pattern = PATTERN_DATE;
          break;
        case TIME:
          pattern = PATTERN_TIME;
          break;
        case DATETIME_LOCAL:
        case DATETIME:
          pattern = PATTERN_DATETIME;
          break;
        case MONTH:
          pattern = PATTERN_MONTH;
          break;
        case WEEK:
          pattern = PATTERN_WEEK;
          break;
        case TEXT:
        default:
          pattern = PATTERN_DATETIME;
      }
    }

    component.setPattern(pattern);
    component.setType(type);
  }
}
