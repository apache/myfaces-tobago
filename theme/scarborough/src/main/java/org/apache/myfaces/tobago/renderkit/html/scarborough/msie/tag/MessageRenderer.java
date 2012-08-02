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

package org.apache.myfaces.tobago.renderkit.html.scarborough.msie.tag;

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_DETAIL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_SHOW_SUMMARY;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

/**
 * @deprecated Please use MessagesRenderer with maxNumber="1"
 */
@Deprecated
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

    if (count != 0) {
      return count * getConfiguredValue(facesContext, component, "messageHeight");
    } else {
      return 1;
    }
  }

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIMessage component = (UIMessage) uiComponent;

    String clientId = ComponentUtil.findClientIdFor(component, facesContext);

    Iterator iterator = facesContext.getMessages(clientId);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    boolean showSummary = ComponentUtil.getBooleanAttribute(component, ATTR_SHOW_SUMMARY);
    boolean showDetail = ComponentUtil.getBooleanAttribute(component, ATTR_SHOW_DETAIL);

    if (iterator.hasNext()) {

      writer.startElement(HtmlConstants.SPAN, component);
      writer.writeClassAttribute("tobago-validation-message");
      writer.writeStyleAttribute();

      while (iterator.hasNext()) {
        FacesMessage message = (FacesMessage) iterator.next();
//      MessageFormat detail = new MessageFormat(formatString, tobagoContext.getLocale());

        String summary = message.getSummary();
        String detail = message.getDetail();

        writer.startElement(HtmlConstants.LABEL, null);
        writer.writeAttribute(HtmlAttributes.FOR, clientId, false);
        writer.writeAttribute(HtmlAttributes.TITLE, detail, true);
        boolean writeEmptyText = true;
        if (summary != null && showSummary) {
          writer.writeText(summary);
          writeEmptyText = false;
          if (detail != null && showDetail) {
            writer.writeText(" ");
          }
        }
        if (detail != null && showDetail) {
          writer.writeText(detail);
          writeEmptyText = false;
        }
        if (writeEmptyText) {
          writer.writeText("");
        }
        writer.endElement(HtmlConstants.LABEL);

        writer.startElement(HtmlConstants.BR, null);
        writer.endElement(HtmlConstants.BR);
      }
      writer.endElement(HtmlConstants.SPAN);

    } else {
      writer.startElement(HtmlConstants.IMG, null);
      String image = ResourceManagerUtil.getImageWithPath(facesContext, "image/1x1.gif");
      writer.writeAttribute(HtmlAttributes.SRC, image, false);
      writer.writeAttribute(HtmlAttributes.ALT, "", false);
      writer.writeStyleAttribute("border: 0px; height: 1px; width: 1px;");
      writer.endElement(HtmlConstants.IMG);
    }
  }

}

