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

import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UITime;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class TimeRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TimeRenderer.class);

  private static final String[] SCRIPTS = {
      "script/calendar.js",
      "script/dateConverter.js"
  };

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    super.prepareRender(facesContext, component);
    if (facesContext instanceof TobagoFacesContext) {
      ((TobagoFacesContext) facesContext).getScriptFiles().addAll(Arrays.asList(SCRIPTS));
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UITime)) {
      LOG.error("Wrong type: Need " + UITime.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    UITime input = (UITime) component;

    String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, input);
    String currentValue = getCurrentValue(facesContext, input);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }


    String converterPattern = "HH:mm";
    if (input.getConverter() != null) {
      Converter converter = input.getConverter();
      if (converter instanceof DateTimeConverter) {
        String pattern = DateFormatUtils.findPattern((DateTimeConverter) converter);
        if (pattern != null && pattern.indexOf('s') > -1) {
          converterPattern += ":ss";
        }
      }
    }

    boolean hasSeconds = converterPattern.indexOf('s') > -1;

    Object value = input.getValue();
    Date date;
    if (value instanceof Date) {
      date = (Date) value;
    } else if (value instanceof Calendar) {
      date = ((Calendar) value).getTime();
    } else {
      date = new Date();
    }

    String hour = new SimpleDateFormat("HH").format(date);
    String minute = new SimpleDateFormat("mm").format(date);
    String second = new SimpleDateFormat("ss").format(date);

    String id = input.getClientId(facesContext);
    final String idPrefix = id + ComponentUtils.SUB_SEPARATOR;
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
    writer.startElement(HtmlConstants.DIV, input);
    writer.writeClassAttribute();
    Style style = new Style(facesContext, input);
    writer.writeStyleAttribute(style);

    writer.startElement(HtmlConstants.DIV, input);
    writer.writeAttribute(HtmlAttributes.ID, idPrefix + "borderDiv", false);
    writer.writeClassAttribute("tobago-time-borderDiv"
        + (hasSeconds ? " tobago-time-borderDiv-seconds" : ""));


    writeInput(writer, input, idPrefix + "hour", hour, true, title);
    writeInputSeparator(writer, ":");
    writeInput(writer, input, idPrefix + "minute", minute, false, title);
    if (hasSeconds) {
      writeInputSeparator(writer, ":");
      writeInput(writer, input, idPrefix + "second", second, false, title);
    }

    writer.endElement(HtmlConstants.DIV);

    String imageId = idPrefix + "inc";
    String imageSrc = "image/timeIncrement.gif";
    HtmlRendererUtils.addImageSources(facesContext, writer, imageSrc, imageId);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeIdAttribute(imageId);
    writer.writeClassAttribute("tobago-time-inc-image"
        + (hasSeconds ? " tobago-time-image-seconds" : ""));
    writer.writeAttribute(HtmlAttributes.SRC, ResourceManagerUtils.getImageWithPath(facesContext, imageSrc), true);
    writer.writeAttribute(HtmlAttributes.ALT, "", false); // TODO: tip

    if (!(ComponentUtils.getBooleanAttribute(input, Attributes.DISABLED)
        || ComponentUtils.getBooleanAttribute(input, Attributes.READONLY))) {
      writer.writeAttribute(HtmlAttributes.ONCLICK, "tbgIncTime(this)", false);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOVER,
          "Tobago.imageMouseover('" + imageId + "')", null);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOUT,
          "Tobago.imageMouseout('" + imageId + "')", null);
    }
    writer.endElement(HtmlConstants.IMG);

    imageId = idPrefix + "dec";
    imageSrc = "image/timeDecrement.gif";
    HtmlRendererUtils.addImageSources(facesContext, writer, imageSrc, imageId);
    writer.startElement(HtmlConstants.IMG, null);
    writer.writeIdAttribute(imageId);
    writer.writeClassAttribute("tobago-time-dec-image"
        + (hasSeconds ? " tobago-time-image-seconds" : ""));
    writer.writeAttribute(HtmlAttributes.SRC, ResourceManagerUtils.getImageWithPath(facesContext, imageSrc), true);
    writer.writeAttribute(HtmlAttributes.ALT, "", false); // TODO: tip
    if (!(ComponentUtils.getBooleanAttribute(input, Attributes.DISABLED)
        || ComponentUtils.getBooleanAttribute(input, Attributes.READONLY))) {
      writer.writeAttribute(HtmlAttributes.ONCLICK, "tbgDecTime(this)", false);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOVER,
          "Tobago.imageMouseover('" + imageId + "')", null);
      writer.writeAttribute(HtmlAttributes.ONMOUSEOUT,
          "Tobago.imageMouseout('" + imageId + "')", null);
    }
    writer.endElement(HtmlConstants.IMG);

    writer.startElement(HtmlConstants.INPUT, input);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeIdAttribute(id + ":converterPattern");
    writer.writeAttribute(HtmlAttributes.VALUE, converterPattern, true);
    writer.endElement(HtmlConstants.INPUT);

    writer.startElement(HtmlConstants.INPUT, input);
    writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
    writer.writeIdAttribute(id);
    writer.writeNameAttribute(id);
    writer.writeAttribute(HtmlAttributes.VALUE, hour + ":" + minute + ":" + second, false);
    writer.endElement(HtmlConstants.INPUT);

    String dateTextBoxId = (String) input.getAttributes().get(Attributes.DATE_INPUT_ID);

    if (dateTextBoxId != null) {
      String[] cmds = {"tbgInitTimeParse('" + id + "', '" + dateTextBoxId + "');"};
      HtmlRendererUtils.writeScriptLoader(facesContext, SCRIPTS, cmds);
    }

    writer.endElement(HtmlConstants.DIV);
  }

  private void writeInputSeparator(TobagoResponseWriter writer, String sep) throws IOException {
    writer.startElement(HtmlConstants.SPAN, null);
    writer.writeClassAttribute("tobago-time-sep");
    writer.writeText(sep);
    writer.endElement(HtmlConstants.SPAN);
  }

  private void writeInput(TobagoResponseWriter writer, UITime input, String id, String hour, boolean hourMode,
      String title) throws IOException {
    Integer tabIndex = input.getTabIndex();
    writer.startElement(HtmlConstants.INPUT, null);
    writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.TEXT, false);
    writer.writeIdAttribute(id);
    if (tabIndex != null) {
      writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, title, true);

    StyleClasses styleClasses = new StyleClasses();
    //styleClasses.updateClassAttributeAndMarkup(input, "time");
    styleClasses.addClass("time", "input");
    writer.writeClassAttribute(styleClasses);
    writer.writeAttribute(HtmlAttributes.READONLY, ComponentUtils.getBooleanAttribute(input, Attributes.READONLY));
    writer.writeAttribute(HtmlAttributes.DISABLED, ComponentUtils.getBooleanAttribute(input, Attributes.DISABLED));
    if (!(ComponentUtils.getBooleanAttribute(input, Attributes.DISABLED)
        || ComponentUtils.getBooleanAttribute(input, Attributes.READONLY))) {
      writer.writeAttribute(HtmlAttributes.ONFOCUS, "tbgTimerInputFocus(this, " + hourMode + ")", false);
      writer.writeAttribute(HtmlAttributes.ONBLUR, "tbgTimerInputBlur(this)", false);
      writer.writeAttribute(HtmlAttributes.ONKEYUP, "tbgTimerKeyUp(this, event)", false);
    }
    writer.writeAttribute(HtmlAttributes.VALUE, hour, true);
    writer.endElement(HtmlConstants.INPUT);
  }
}
