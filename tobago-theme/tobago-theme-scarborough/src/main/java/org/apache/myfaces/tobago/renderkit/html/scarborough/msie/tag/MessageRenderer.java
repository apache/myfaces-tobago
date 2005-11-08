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
package org.apache.myfaces.tobago.renderkit.html.scarborough.msie.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class MessageRenderer extends MessageRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(MessageRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("component = '" + component + "'");
    }
    String clientId = null;
    if (component instanceof UIMessage) {
      clientId = ((UIMessage)component).getFor();
    }
    int count = 0;
    for (Iterator i = facesContext.getMessages(clientId); i.hasNext(); ) {
      count++;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("here are " + count + " messages");
    }

    if (count != 0) {
      return count * 20; // FIXME: depends on theme
    } else {
      return 1;
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {


    UIMessage component = (UIMessage) uiComponent;

    String clientId = ComponentUtil.findClientIdFor(component, facesContext);

    Iterator iterator = facesContext.getMessages(clientId);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();


    if (iterator.hasNext()) {

      writer.startElement("span", component);
      writer.writeClassAttribute("tobago-validation-message");
      writer.writeAttribute("style", null, ATTR_STYLE);

      while (iterator.hasNext()) {
        FacesMessage message = (FacesMessage) iterator.next();
//      MessageFormat detail = new MessageFormat(formatString, tobagoContext.getLocale());
        writer.startElement("label", null);
        writer.writeAttribute("for", clientId, null);
        writer.writeAttribute("title", message.getDetail(), null);
        writer.writeText(message.getSummary(), null);
        writer.endElement("label");

        writer.startElement("br", null);
        writer.endElement("br");
      }
      writer.endElement("span");

    } else {
      writer.startElement("img", null);
      String image = ResourceManagerUtil.getImageWithPath(
          facesContext, "image/1x1.gif");
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("alt", "", null);
      writer.writeAttribute("style", "border: 0px; height: 1px; width: 1px;", null);
      writer.endElement("img");      
    }
  }
// ///////////////////////////////////////////// bean getter + setter

}

