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

import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.util.AccessKeyLogger;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LinkRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(LinkRenderer.class);

  public void encodeBegin(final FacesContext facesContext, final UIComponent component) throws IOException {

    final AbstractUILink link = (AbstractUILink) component;
    final String clientId = link.getClientId(facesContext);
    final boolean disabled = link.isDisabled();
    final LabelWithAccessKey label = new LabelWithAccessKey(link);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (disabled) {
      writer.startElement(HtmlElements.SPAN, link);
    } else {
      writer.startElement(HtmlElements.A, link);

      final CommandMap map = new CommandMap(new Command(facesContext, link));
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);

      writer.writeAttribute(HtmlAttributes.HREF, "#", false);

      if (label.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
        AccessKeyLogger.addAccessKey(facesContext, label.getAccessKey(), clientId);
      }

      final Integer tabIndex = link.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, link);
    writer.writeStyleAttribute(link.getStyle());
    writer.writeClassAttribute(Classes.create(link), link.getCustomClass());
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    final String title = HtmlRendererUtils.getTitleFromTipAndMessages(facesContext, link);
    if (title != null) {
      writer.writeAttribute(HtmlAttributes.TITLE, title, true);
    }
    writer.flush();

//  image
    String image = link.getImage();
    HtmlRendererUtils.encodeIconWithLabel(writer, facesContext, image, label, disabled);
  }

  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {
    final AbstractUILink link = (AbstractUILink) component;
    final ResponseWriter writer = facesContext.getResponseWriter();
    if (link.isDisabled()) {
      writer.endElement(HtmlElements.SPAN);
    } else {
      writer.endElement(HtmlElements.A);
    }
  }
}
