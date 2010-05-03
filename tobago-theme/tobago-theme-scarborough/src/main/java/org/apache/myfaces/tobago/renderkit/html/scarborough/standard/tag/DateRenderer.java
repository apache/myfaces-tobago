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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.util.DateFormatUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.Arrays;

public class DateRenderer extends InRenderer {

  private static final Logger LOG = LoggerFactory.getLogger(DateRenderer.class);

  private static final String[] SCRIPTS = {
      "script/dateConverter.js",
      "script/calendar.js"
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

    // for AJAX
    HtmlRendererUtils.writeScriptLoader(facesContext, SCRIPTS, null);

    super.encodeEnd(facesContext, component);

    Converter help = getConverter(facesContext, component);
    // TODO is this really needed?
    if (help instanceof DateTimeConverter) {
      DateTimeConverter converter = (DateTimeConverter) help;
      String pattern = DateFormatUtils.findPattern(converter);

      if (pattern != null) {
        TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);
        String id = component.getClientId(facesContext);
        writer.startElement(HtmlConstants.INPUT, component);
        writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
        writer.writeIdAttribute(id + ":converterPattern");
        writer.writeAttribute(HtmlAttributes.VALUE, pattern, false);
        writer.endElement(HtmlConstants.INPUT);
      } else {
        LOG.warn("Can't find the pattern for the converter! DatePicker may not work correctly.");
      }
    }
  }
}

