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
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.InRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class TextAreaRenderer extends InRendererBase {



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

    String clientId = input.getClientId(facesContext);
    String onchange = HtmlUtils.generateOnchange(input, facesContext);

    writer.startElement("textarea", input);
    writer.writeNameAttribute(clientId);
    writer.writeIdAttribute(clientId);
    writer.writeAttribute("rows", null, ATTR_ROWS);
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
      writer.writeAttribute("onchange", onchange, null);
    }
    String currentValue = ComponentUtil.currentValue(input);
    if (currentValue != null) {
      // this is because browsers eat the first CR+LF of <textarea>
      if (currentValue.startsWith("\r\n")) {
        currentValue = "\r\n" + currentValue;
      } else if (currentValue.startsWith("\n")) {
        currentValue = "\n" + currentValue;
      } else if (currentValue.startsWith("\r")) {
        currentValue = "\r" + currentValue;
      }
      writer.writeText(currentValue, null);
    }
    writer.endElement("textarea");
  }
}

