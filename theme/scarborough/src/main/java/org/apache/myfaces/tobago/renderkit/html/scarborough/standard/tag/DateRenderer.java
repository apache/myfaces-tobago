package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

/*
 * Copyright 2002-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_CLASS;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.List;

public class DateRenderer extends InRenderer {

  private static final Log LOG = LogFactory.getLog(DateRenderer.class);

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    final String[] scripts = {
        "script/date.js",
        "script/dateConverter.js",
        "script/calendar.js"};

    final List<String> scriptFiles
        = ComponentUtil.findPage(component).getScriptFiles();
    for (String script : scripts) {
      scriptFiles.add(script);
    }

    if (TobagoConfig.getInstance(facesContext).isAjaxEnabled()) {
      HtmlRendererUtil.writeScriptLoader(facesContext, scripts, null);
    }

    String classes = ComponentUtil.getStringAttribute(component, ATTR_STYLE_CLASS);
    classes = classes.replaceAll("tobago-date-", "tobago-in-");
    component.getAttributes().put(ATTR_STYLE_CLASS, classes);
    super.encodeEndTobago(facesContext, component);

    Converter converter = getConverter(facesContext, component);
    // TODO is this really needed?
    if (converter instanceof DateTimeConverter) {
      String pattern = ((DateTimeConverter) converter).getPattern();
      if (pattern != null) {
        TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();
        String id = component.getClientId(facesContext);
        writer.startElement("input", component);
        writer.writeAttribute("type", "hidden", null);
        writer.writeIdAttribute(id + ":converterPattern");
        writer.writeAttribute("value", pattern, null);
        writer.endElement("input");
      }
    }
  }
}

