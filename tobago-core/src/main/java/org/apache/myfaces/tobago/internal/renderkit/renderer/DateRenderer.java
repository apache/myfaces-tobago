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
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.internal.component.AbstractUIDate;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.internal.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.model.DateType;
import org.apache.myfaces.tobago.renderkit.css.BootstrapClass;
import org.apache.myfaces.tobago.renderkit.css.Icons;
import org.apache.myfaces.tobago.renderkit.css.TobagoClass;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlButtonTypes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.ResourceUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.component.UIComponent;
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
import java.util.Date;

public class DateRenderer<T extends AbstractUIDate> extends DecorationPositionRendererBase<T> {

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
  protected void encodeBeginField(final FacesContext facesContext, final T date) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final UIComponent after = ComponentUtils.getFacet(date, Facets.after);
    final UIComponent before = ComponentUtils.getFacet(date, Facets.before);

    writer.startElement(HtmlElements.DIV);
    writer.writeClassAttribute(BootstrapClass.INPUT_GROUP);

    encodeGroupAddon(facesContext, writer, before, false);

    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, date);
    final DateType type = date.getType();

    final String currentValue = getCurrentValue(facesContext, date);
    final String clientId = date.getClientId(facesContext);
    final String fieldId = date.getFieldId(facesContext);
    final boolean readonly = date.isReadonly();
    final boolean disabled = date.isDisabled();
    final boolean required = ComponentUtils.getBooleanAttribute(date, Attributes.required);
    final Markup markup = date.getMarkup() != null ? date.getMarkup() : Markup.NULL;

    writer.startElement(HtmlElements.INPUT);

    if (date.getAccessKey() != null) {
      writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(date.getAccessKey()), false);
      AccessKeyLogger.addAccessKey(facesContext, date.getAccessKey(), clientId);
    }

    writer.writeAttribute(HtmlAttributes.TYPE, type.getName(), false);
    final Double step = date.getStep();
    if (step != null) {
      writer.writeAttribute(HtmlAttributes.STEP, Double.toString(step), false);
    }
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(fieldId);
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, date);
    writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.writeAttribute(HtmlAttributes.READONLY, readonly);
    writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
    writer.writeAttribute(HtmlAttributes.TABINDEX, date.getTabIndex());
    writer.writeAttribute(HtmlAttributes.MIN, convertToString(date.getMin()), true);
    if (date.getMax() == null && DateType.DATE.equals(type)) {
      writer.writeAttribute(HtmlAttributes.MAX, "9999-12-31", false);
    } else if (date.getMax() == null && DateType.DATETIME_LOCAL.equals(type)) {
      writer.writeAttribute(HtmlAttributes.MAX, "9999-12-31T23:59", false);
    } else {
      writer.writeAttribute(HtmlAttributes.MAX, convertToString(date.getMax()), true);
    }

    writer.writeClassAttribute(
        BootstrapClass.validationColor(ComponentUtils.getMaximumSeverity(date)),
        BootstrapClass.FORM_CONTROL,
        markup.contains(Markup.LARGE) ? BootstrapClass.FORM_CONTROL_LG : null,
        markup.contains(Markup.SMALL) ? BootstrapClass.FORM_CONTROL_SM : null,
        date.getCustomClass());

    writer.writeAttribute(HtmlAttributes.REQUIRED, required);
    renderFocus(clientId, date.isFocus(), date.isError(), facesContext, writer);

    writer.endElement(HtmlElements.INPUT);

    encodeBehavior(writer, facesContext, date);

    encodeGroupAddon(facesContext, writer, after, true);

    if (date.isTodayButton()) {
      encodeButton(facesContext, date, type);
    }
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

  private void encodeButton(final FacesContext facesContext, final T component, final DateType type)
      throws IOException {

    final TobagoResponseWriter writer = getResponseWriter(facesContext);

    final String title = ResourceUtils.getString(facesContext,
        type == DateType.DATETIME_LOCAL || type == DateType.TIME ? "date.now" : "date.today");

    writer.startElement(HtmlElements.BUTTON);
    writer.writeClassAttribute(
        BootstrapClass.BTN,
        BootstrapClass.BTN_SECONDARY,
        TobagoClass.NOW);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlButtonTypes.BUTTON);
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    writer.writeAttribute(HtmlAttributes.DISABLED, component.isDisabled() || component.isReadonly());
    writer.writeAttribute(HtmlAttributes.TABINDEX, component.getTabIndex());

    writer.startElement(HtmlElements.I);
    writer.writeClassAttribute(Icons.ARROW_DOWN);
    writer.endElement(HtmlElements.I);

    writer.endElement(HtmlElements.BUTTON);
  }

  @Override
  public void encodeEndField(final FacesContext facesContext, final T component) throws IOException {
    final TobagoResponseWriter writer = getResponseWriter(facesContext);
    writer.endElement(HtmlElements.DIV);
  }

  @Override
  protected String getFieldId(final FacesContext facesContext, final T component) {
    return component.getFieldId(facesContext);
  }

  /**
   * Creates a converter (if not defined any) which satisfies the requirements of HTML5.
   * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTML/Date_and_time_formats">MDN Web Docs</a>
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
        final DateTimeConverter dateTimeConverter = (DateTimeConverter)
            facesContext.getApplication().createConverter("jakarta.faces.DateTime");
        if (estimatedType.isAssignableFrom(LocalDateTime.class)) {
          dateTimeConverter.setType("localDateTime");
          final Double step = component.getStep();
          if (step == null || step >= 60) {
            dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL);
          } else if (step >= 1) {
            dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL_SECONDS);
          } else {
            dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL_MILLIS);
          }
        } else if (estimatedType.isAssignableFrom(LocalDate.class)) {
          dateTimeConverter.setType("localDate");
          dateTimeConverter.setPattern(DateType.PATTERN_DATE);
        } else if (estimatedType.isAssignableFrom(LocalTime.class)) {
          dateTimeConverter.setType("localTime");
          final Double step = component.getStep();
          if (step == null || step >= 60) {
            dateTimeConverter.setPattern(DateType.PATTERN_TIME);
          } else if (step >= 1) {
            dateTimeConverter.setPattern(DateType.PATTERN_TIME_SECONDS);
          } else {
            dateTimeConverter.setPattern(DateType.PATTERN_TIME_MILLIS);
          }
        } else if (estimatedType.isAssignableFrom(ZonedDateTime.class)) {
          dateTimeConverter.setType("zonedDateTime");
          final Double step = component.getStep();
          if (step == null || step >= 60) {
            dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL);
          } else if (step >= 1) {
            dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL_SECONDS);
          } else {
            dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL_MILLIS);
          }
        } else if (estimatedType.isAssignableFrom(Long.class)) {
          dateTimeConverter.setType("date");
          dateTimeConverter.setPattern(DateType.PATTERN_DATE);
        } else if (estimatedType.isAssignableFrom(Date.class)) {
          dateTimeConverter.setType("date");
          final DateType type = component.getType();
          if (DateType.DATETIME_LOCAL.equals(type)) {
            final Double step = component.getStep();
            if (step == null || step >= 60) {
              dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL);
            } else if (step >= 1) {
              dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL_SECONDS);
            } else {
              dateTimeConverter.setPattern(DateType.PATTERN_DATETIME_LOCAL_MILLIS);
            }
          } else if (DateType.TIME.equals(type)) {
            final Double step = component.getStep();
            if (step == null || step >= 60) {
              dateTimeConverter.setPattern(DateType.PATTERN_TIME);
            } else if (step >= 1) {
              dateTimeConverter.setPattern(DateType.PATTERN_TIME_SECONDS);
            } else {
              dateTimeConverter.setPattern(DateType.PATTERN_TIME_MILLIS);
            }
          } else {
            dateTimeConverter.setPattern(DateType.PATTERN_DATE);
          }
        } else if (estimatedType.isAssignableFrom(Number.class)) {
          LOG.error("date");
          dateTimeConverter.setType("date");
          dateTimeConverter.setPattern(DateType.PATTERN_DATE);
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
    DateType type = component.getType();
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
          type = DateType.DATE;
        } else if (TYPE_BOTH.equals(typeFromConverter)) {
          type = DateType.DATETIME_LOCAL;
        } else if (TYPE_TIME.equals(typeFromConverter)) {
          type = DateType.TIME;
        } else if (TYPE_LOCAL_DATE.equals(typeFromConverter)) {
          type = DateType.DATE;
        } else if (TYPE_LOCAL_DATE_TIME.equals(typeFromConverter)) {
          type = DateType.DATETIME_LOCAL;
        } else if (TYPE_LOCAL_TIME.equals(typeFromConverter)) {
          type = DateType.TIME;
        } else if (TYPE_ZONED_DATE_TIME.equals(typeFromConverter)) {
          type = DateType.DATETIME_LOCAL;
        } else if (TYPE_OFFSET_DATE_TIME.equals(typeFromConverter)) {
          type = DateType.DATETIME_LOCAL;
        } else if (TYPE_OFFSET_TIME.equals(typeFromConverter)) {
          type = DateType.TIME;
        } else if (TYPE_MONTH.equals(typeFromConverter)) {
          type = DateType.MONTH;
        } else if (TYPE_WEEK.equals(typeFromConverter)) {
          type = DateType.WEEK;
        } else { // unknown type
          type = DateType.DATE;
        }
      } else { // no converter
        type = DateType.DATE;
      }
    }

    if (pattern == null && converter != null) {
      pattern = converter.getPattern();
    }

    if (pattern == null) {
      pattern = type.getPattern();
    }

    component.setPattern(pattern);
    component.setType(type);
  }
}
