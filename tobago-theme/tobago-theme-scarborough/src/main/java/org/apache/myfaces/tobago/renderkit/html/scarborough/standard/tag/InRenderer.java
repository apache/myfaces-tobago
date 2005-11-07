/*
 * Copyright 2002-2005 The Apache Software Foundation.
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
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.InRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.Iterator;
import static org.apache.myfaces.tobago.TobagoConstants.*;

public class InRenderer extends InRendererBase{
  private static final Log LOG = LogFactory.getLog(InRenderer.class);

// ----------------------------------------------------------- business methods

  protected void renderMain(FacesContext facesContext, UIInput input,
      TobagoResponseWriter writer) throws IOException {
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
    String type = ComponentUtil.getBooleanAttribute(input,
        ATTR_PASSWORD) ? "password" : "text";

    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    String id = input.getClientId(facesContext);

    writer.startElement("input", input);
    writer.writeAttribute("type", type, null);
    writer.writeNameAttribute(id);
    writer.writeIdAttribute(id);
    if (currentValue != null) {
      writer.writeAttribute("value", currentValue, null);
    }
    if (title != null) {
      writer.writeAttribute("title", title, null);
    }
    writer.writeAttribute("readonly",
        ComponentUtil.getBooleanAttribute(input, ATTR_READONLY));
    writer.writeAttribute("disabled",
        ComponentUtil.getBooleanAttribute(input, ATTR_DISABLED));
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeComponentClass();
    if (onchange != null) {
      // todo: create and use utility method to write attributes without quoting
//      writer.writeAttribute("onchange", onchange, null);
    }
    writer.endElement("input");

    if (input.getConverter() != null) {
      Converter converter = input.getConverter();
      if (converter instanceof DateTimeConverter) {
        String pattern
            = ((DateTimeConverter) converter).getPattern();
        if (pattern != null) {
          writer.startElement("input", input);
          writer.writeAttribute("type", "hidden", null);
          writer.writeIdAttribute(id + ":converterPattern");
          writer.writeAttribute("value", pattern, null);
          writer.endElement("input");
        }
      }
    }
  }
}

