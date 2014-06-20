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
import org.apache.myfaces.tobago.component.UITime;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.Validator;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TimeRenderer.class);

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);

    if (!TobagoConfig.getInstance(facesContext).isClassicDateTimePicker()) {
      final UITime time = (UITime) component;
      Markup markup = time.getCurrentMarkup();
      markup = markup.add(Markup.valueOf("timepicker"));
      time.setCurrentMarkup(markup);
    }
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UITime time = (UITime) component;
    final String id = time.getClientId(facesContext);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, time);
    final String currentValue = getCurrentValue(facesContext, time);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }
    final boolean readonly = time.isReadonly();
    final boolean disabled = time.isDisabled();
    final boolean required = time.isRequired();

    final Object value = time.getValue();
    final Date date;
    if (value instanceof Date) {
      date = (Date) value;
    } else if (value instanceof Calendar) {
      date = ((Calendar) value).getTime();
    } else {
      date = new Date();
    }

    TobagoConfig tobagoConfig = TobagoConfig.getInstance(facesContext);

    if (tobagoConfig.isClassicDateTimePicker()) {

      String pattern = "HH:mm";
      if (time.getConverter() != null) {
        final Converter converter = time.getConverter();
        if (converter instanceof DateTimeConverter) {
          final String string = DateFormatUtils.findPattern((DateTimeConverter) converter);
          if (string != null && string.indexOf('s') > -1) {
            pattern += ":ss";
          }
        }
      }
      final boolean hasSeconds = pattern.indexOf('s') > -1;

      final String hour = new SimpleDateFormat("HH").format(date);
      final String minute = new SimpleDateFormat("mm").format(date);
      final String second = new SimpleDateFormat("ss").format(date);

      final String idPrefix = id + ComponentUtils.SUB_SEPARATOR;

      final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

      writer.startElement(HtmlElements.DIV, time);
      writer.writeIdAttribute(id);
      writer.writeClassAttribute(Classes.create(time));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, time);
      writer.writeAttribute(DataAttributes.CLASSIC_DATE_TIME_PICKER, "", false);
      final Style style = new Style(facesContext, time);
      writer.writeStyleAttribute(style);
      final String dateInputId = (String) time.getAttributes().get(Attributes.DATE_INPUT_ID);
      if (dateInputId != null) {
        writer.writeAttribute(DataAttributes.DATE_INPUT_ID, dateInputId, false);
      }
      writer.writeAttribute(DataAttributes.PATTERN, pattern, true);

      writer.startElement(HtmlElements.DIV, time);
      writer.writeIdAttribute(idPrefix + "borderDiv");
      Markup markup = time.getCurrentMarkup();
      if (hasSeconds) {
        markup = markup.add(Markup.SECONDS);
      }
      writer.writeClassAttribute(Classes.create(time, "borderDiv", markup));

      writeInput(writer, time, idPrefix, "hour", hour, title, 24);
      writeInputSeparator(writer, time, ":");
      writeInput(writer, time, idPrefix, "minute", minute, title, 60);
      if (hasSeconds) {
        writeInputSeparator(writer, time, ":");
        writeInput(writer, time, idPrefix, "second", second, title, 60);
      }

      writer.endElement(HtmlElements.DIV);

      writer.startElement(HtmlElements.IMG, null);
      writer.writeIdAttribute(idPrefix + "inc");
      writer.writeClassAttribute(Classes.create(time, "incImage", markup));
      writer.writeAttribute(HtmlAttributes.SRC,
          ResourceManagerUtils.getImageWithPath(facesContext, "image/timeIncrement.gif"), true);
      writer.writeAttribute(HtmlAttributes.ALT, "", false); // TODO: tip
      writer.writeAttribute(HtmlAttributes.READONLY, time.isReadonly());
      writer.writeAttribute(HtmlAttributes.DISABLED, time.isDisabled());
      writer.endElement(HtmlElements.IMG);

      writer.startElement(HtmlElements.IMG, null);
      writer.writeIdAttribute(idPrefix + "dec");
      writer.writeClassAttribute(Classes.create(time, "decImage", markup));
      writer.writeAttribute(HtmlAttributes.SRC,
          ResourceManagerUtils.getImageWithPath(facesContext, "image/timeDecrement.gif"), true);
      writer.writeAttribute(HtmlAttributes.ALT, "", false); // TODO: tip
      writer.writeAttribute(HtmlAttributes.READONLY, time.isReadonly());
      writer.writeAttribute(HtmlAttributes.DISABLED, time.isDisabled());
      writer.endElement(HtmlElements.IMG);

      writer.startElement(HtmlElements.INPUT, time);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeIdAttribute(idPrefix + "field");
      writer.writeNameAttribute(id);
      writer.writeAttribute(HtmlAttributes.VALUE, hour + ":" + minute + ":" + second, false);
      writer.endElement(HtmlElements.INPUT);

      writer.endElement(HtmlElements.DIV);

    } else {

      // Todo: in Tobago 3.0 TimeRenderer should extend InRenderer, and this can cleaned up!
      String pattern = null;
      final Converter help = getConverter(facesContext, time);
      if (help instanceof DateTimeConverter) {
        final DateTimeConverter converter = (DateTimeConverter) help;
        pattern = DateFormatUtils.findPattern(converter);
      }
      if (pattern == null) {
        pattern = "HH-mm";
        LOG.warn("Can't find the pattern for the converter! DatePicker may not work correctly. "
            + "Trying to use: '" + pattern + "'");
      }

      final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

      writer.startElement(HtmlElements.INPUT, time);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false); // tbd: no "time"
      writer.writeNameAttribute(id);
      writer.writeIdAttribute(id);
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, time);
      if (currentValue != null) {
        writer.writeAttribute(HtmlAttributes.VALUE, currentValue, true);
      }
      if (title != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, title, true);
      }
      int maxLength = 0;
      for (final Validator validator : time.getValidators()) {
        if (validator instanceof LengthValidator) {
          final LengthValidator lengthValidator = (LengthValidator) validator;
          maxLength = lengthValidator.getMaximum();
        }
        /*if (validator instanceof RegexValidator) {
          RegexValidator regexValidator = (RegexValidator) validator;
          pattern = regexValidator.getPattern();
        }*/
      }
      if (maxLength > 0) {
        writer.writeAttribute(HtmlAttributes.MAXLENGTH, maxLength);
      }
      writer.writeAttribute(DataAttributes.PATTERN, pattern, true);
      writer.writeAttribute(HtmlAttributes.READONLY, readonly);
      writer.writeAttribute(HtmlAttributes.DISABLED, disabled);
      final Integer tabIndex = time.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
      final Style style = new Style(facesContext, time);
      writer.writeStyleAttribute(style);

      final String placeholder = time.getPlaceholder();
      if (!disabled && !readonly && StringUtils.isNotBlank(placeholder)) {
        writer.writeAttribute(HtmlAttributes.PLACEHOLDER, placeholder, true);
      }

      writer.writeAttribute(HtmlAttributes.AUTOCOMPLETE, "off", false);

      writer.writeClassAttribute(Classes.create(time));
      /*if (component instanceof AbstractUIInput) {
       String onchange = HtmlUtils.generateOnchange((AbstractUIInput) component, facesContext);
       if (onchange != null) {
         // TODO: create and use utility method to write attributes without quoting
     //      writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
       }
     } */
      writer.writeAttribute(HtmlAttributes.REQUIRED, required);
      HtmlRendererUtils.renderFocus(id, time.isFocus(), ComponentUtils.isError(time), facesContext, writer);
      HtmlRendererUtils.renderCommandFacet(time, facesContext, writer);
      writer.endElement(HtmlElements.INPUT);


    }
  }

  private void writeInputSeparator(final TobagoResponseWriter writer, final UITime time, final String sep)
      throws IOException {
    writer.startElement(HtmlElements.SPAN, null);
    writer.writeClassAttribute(Classes.create(time, "sep"));
    writer.writeText(sep);
    writer.endElement(HtmlElements.SPAN);
  }

  private void writeInput(
      final TobagoResponseWriter writer, final UITime input, final String idPrefix, final String unit,
      final String value, final String title, final int max)
      throws IOException {
    final Integer tabIndex = input.getTabIndex();
    writer.startElement(HtmlElements.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false);
    writer.writeIdAttribute(idPrefix + unit);
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);

    writer.writeClassAttribute(Classes.create(input, "input"));
    writer.writeAttribute(HtmlAttributes.READONLY, input.isReadonly());
    writer.writeAttribute(HtmlAttributes.DISABLED, input.isDisabled());
    writer.writeAttribute(HtmlAttributes.VALUE, value, true);
    writer.writeAttribute(DataAttributes.MAX, Integer.toString(max), true);
    writer.writeAttribute(DataAttributes.UNIT, unit, true);
    writer.endElement(HtmlElements.INPUT);
  }
}
