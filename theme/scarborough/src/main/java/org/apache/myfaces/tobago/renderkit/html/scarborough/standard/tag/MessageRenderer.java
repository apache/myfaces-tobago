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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class MessageRenderer extends MessageRendererBase {

  private static final Log LOG = LogFactory.getLog(MessageRenderer.class);

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("component = '" + component + "'");
    }
    String clientId = null;
    if (component instanceof UIMessage) {
      clientId = ComponentUtil.findClientIdFor(component, facesContext);
    }
    int count = 0;
    for (Iterator i = facesContext.getMessages(clientId); i.hasNext(); i.next()) {
      count++;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("here are " + count + " messages");
    }
    return count * getConfiguredValue(facesContext, component, "messageHeight");
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {


    // FIXME: must be refactored! Bitte daran denken die msie version auch umzubauen!!!

    UIMessage component = (UIMessage) uiComponent;

    String clientId = ComponentUtil.findClientIdFor(component, facesContext);

    Iterator iterator = facesContext.getMessages(clientId);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement(HtmlConstants.SPAN, component);
    writer.writeClassAttribute("tobago-validation-message");
    writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE);

    while (iterator.hasNext()) {
      FacesMessage message = (FacesMessage) iterator.next();
//      MessageFormat detail = new MessageFormat(formatString, tobagoContext.getLocale());
      writer.startElement(HtmlConstants.LABEL, null);
      writer.writeAttribute(HtmlAttributes.FOR, clientId, null);
      writer.writeAttribute(HtmlAttributes.TITLE, message.getDetail(), null);
      writer.writeText(message.getSummary(), null);
      writer.endElement(HtmlConstants.LABEL);

      writer.startElement(HtmlConstants.BR, null);
      writer.endElement(HtmlConstants.BR);
    }
    writer.endElement(HtmlConstants.SPAN);

  }
}

