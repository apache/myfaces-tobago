/*
 * Copyright 2002-2005 atanion GmbH.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
 * Created 07.02.2003 16:00:00.
 * $Id: InRenderer.java 1361 2005-08-15 11:46:20 +0200 (Mon, 15 Aug 2005) lofwyr $
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.InRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class TimeRenderer extends InRendererBase{

  private static final Log LOG = LogFactory.getLog(TimeRenderer.class);

// ----------------------------------------------------------- business methods

  protected void renderMain(FacesContext facesContext, UIInput input,
                            TobagoResponseWriter writer) throws IOException {

    List<String> scriptFiles = ComponentUtil.findPage(input).getScriptFiles();
    scriptFiles.add("script/dateConverter.js");
    scriptFiles.add("script/calendar.js");

    Iterator messages = facesContext.getMessages(
        input.getClientId(facesContext));
    StringBuffer stringBuffer = new StringBuffer();
    while (messages.hasNext()) {
      FacesMessage message = (FacesMessage) messages.next();
      stringBuffer.append(message.getDetail());
    }

    String title = null;
    if (stringBuffer.length() > 0) {
      title = stringBuffer.toString();
    }

    title = HtmlRendererUtil.addTip(
            title, (String) input.getAttributes().get(ATTR_TIP));

    String currentValue = getCurrentValue(facesContext, input);
    if (LOG.isDebugEnabled()) {
      LOG.debug("currentValue = '" + currentValue + "'");
    }


    String converterPattern = "HH:mm";
    if (input.getConverter() != null) {
      Converter converter = input.getConverter();
      if (converter instanceof DateTimeConverter) {
        String pattern = ((DateTimeConverter) converter).getPattern();
        if (pattern != null && pattern.indexOf('s') > -1) {
          converterPattern += ":ss";
        }
      }
    } else if (ComponentUtil.getBooleanAttribute(input, ATTR_POPUP_CALENDAR_FORCE_TIME)) {
      converterPattern += ":ss";
    }

    boolean hasSeconds = converterPattern.indexOf('s') > -1;

    SimpleDateFormat dateFormat = new SimpleDateFormat(converterPattern);

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
    final String idPrefix = id + SUBCOMPONENT_SEP;

    writer.startElement("div", input);
    writer.writeComponentClass();

    writer.startElement("div", input);
    writer.writeAttribute("id", idPrefix + "borderDiv", null);
    writer.writeClassAttribute("tobago-time-borderDiv"
        + (hasSeconds ? " tobago-time-borderDiv-seconds" : ""));

    writeInput(writer, idPrefix + "hour", hour, true);
    writeInputSeparator(writer, ":");
    writeInput(writer, idPrefix + "minute", minute, false);
    if (hasSeconds) {
      writeInputSeparator(writer, ":");
      writeInput(writer, idPrefix + "second", second, false);
    }

    writer.endElement("div");

    writer.startElement("img");
    writer.writeIdAttribute(idPrefix + "inc");
    writer.writeAttribute("onclick", "tbgIncTime(this)", false);
    writer.writeClassAttribute("tobago-time-inc-image"
        + (hasSeconds ? " tobago-time-image-seconds" : ""));
    writer.writeAttribute("src", ResourceManagerUtil.getImageWithPath(facesContext, "image/timeIncrement.gif"), true);
    writer.writeAttribute("alt", "", false); // todo: tip
    writer.endElement("img");

    writer.startElement("img");
    writer.writeIdAttribute(idPrefix + "dec");
    writer.writeAttribute("onclick", "tbgDecTime(this)", false);
    writer.writeClassAttribute("tobago-time-dec-image"
        + (hasSeconds ? " tobago-time-image-seconds" : ""));
    writer.writeAttribute("src", ResourceManagerUtil.getImageWithPath(facesContext, "image/timeDecrement.gif"), true);
    writer.writeAttribute("alt", "", false); // todo: tip
    writer.endElement("img");



    writer.startElement("input", input);
    writer.writeAttribute("type", "hidden", false);
    writer.writeIdAttribute(id + ":converterPattern");
    writer.writeAttribute("value", converterPattern, null);
    writer.endElement("input");


    writer.startElement("input", input);
    writer.writeAttribute("type", "hidden", false);
    writer.writeIdAttribute(id);
    writer.writeNameAttribute(id);
    writer.writeAttribute("value", hour + ":" + minute + ":" + second, false);
    writer.endElement("input");


    String dateTextBoxId = (String) input.getAttributes().get(ATTR_CALENDAR_DATE_INPUT_ID);

    if (dateTextBoxId != null) {
      HtmlRendererUtil.startJavascript(writer);
      writer.writeText(
          "tbgInitTimeParse('" + id + "', '" + dateTextBoxId + "');", null);
      HtmlRendererUtil.endJavascript(writer);
    }

    writer.endElement("div");
  }

  private void writeInputSeparator(TobagoResponseWriter writer, String sep) throws IOException {
    writer.startElement("span");
    writer.writeClassAttribute("tobago-time-sep");
    writer.writeText(sep, null);
    writer.endElement("span");
  }

  private void writeInput(TobagoResponseWriter writer, String id, String hour, boolean hourMode)
  throws IOException {
    writer.startElement("input");
    writer.writeAttribute("type", "text", false);
    writer.writeIdAttribute(id);
    writer.writeClassAttribute("tobago-time-input");
    writer.writeAttribute("onfocus", "tbgTimerInputFocus(this, " + hourMode + ")", false);
    writer.writeAttribute("onblur", "tbgTimerInputBlur(this)", false);
    writer.writeAttribute("onkeyup", "tbgTimerKeyUp(this, event)", false);
    writer.writeAttribute("value", hour, true);
    writer.endElement("input");
  }
}

