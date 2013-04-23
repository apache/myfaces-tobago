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

package org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUILink;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.PixelMeasure;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.Command;
import org.apache.myfaces.tobago.renderkit.html.CommandMap;
import org.apache.myfaces.tobago.renderkit.html.DataAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.JsonUtils;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LinkRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(LinkRenderer.class);

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    final AbstractUILink link = (AbstractUILink) component;
    final String clientId = link.getClientId(facesContext);
    final boolean disabled = link.isDisabled();
// final CommandRendererHelper helper = new CommandRendererHelper(facesContext, link, CommandRendererHelper.Tag.ANCHOR);
// final String href = helper.getHref();

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(link);

    if (disabled) {
      writer.startElement(HtmlElements.SPAN, link);
    } else {
      writer.startElement(HtmlElements.A, link);
//      writer.writeAttribute(HtmlAttributes.HREF, href, true); XXX

      final CommandMap map = new CommandMap(new Command(facesContext, link));
      writer.writeAttribute(DataAttributes.COMMANDS, JsonUtils.encode(map), true);

      writer.writeAttribute(HtmlAttributes.HREF, "#", false);

      if (label.getAccessKey() != null) {
        writer.writeAttribute(HtmlAttributes.ACCESSKEY, Character.toString(label.getAccessKey()), false);
      }

      final Integer tabIndex = link.getTabIndex();
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, link);
    Style style = new Style(facesContext, link);
    writer.writeStyleAttribute(style);
    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute(Classes.create(link));
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.renderTip(link, writer);
    writer.flush();

//  image
    String image = link.getImage();
    if (image != null) {
      if (ResourceManagerUtils.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = getImageWithPath(facesContext, image, disabled);
      }
      writer.startElement(HtmlElements.IMG, link);
      writer.writeClassAttribute(Classes.create(link, "image"));
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      writer.writeAttribute(HtmlAttributes.BORDER, 0); // TODO: is border=0 setting via style possible?
      String tip = link.getTip();
      writer.writeAttribute(HtmlAttributes.ALT, tip != null ? tip : "", true);
      if (tip != null) {
        writer.writeAttribute(HtmlAttributes.TITLE, tip, true);
      }
      writer.endElement(HtmlElements.IMG);
    }

//  label
    if (label.getText() != null) {
      if (image != null) {
        writer.write(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }

    if (label.getAccessKey() != null && !disabled) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("duplicated accessKey : " + label.getAccessKey());
      }

      //HtmlRendererUtils.addClickAcceleratorKey(facesContext, clientId, label.getAccessKey());
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
    AbstractUILink link = (AbstractUILink) component;
    ResponseWriter writer = facesContext.getResponseWriter();
    if (link.isDisabled()) {
      writer.endElement(HtmlElements.SPAN);
    } else {
      writer.endElement(HtmlElements.A);
    }
  }

  @Override
  public Measure getPreferredWidth(FacesContext facesContext, Configurable component) {
    final AbstractUILink link = (AbstractUILink) component;
    final LabelWithAccessKey label = new LabelWithAccessKey(link);
    final String text = label.getText();
    final String image = link.getImage();

    Measure width = PixelMeasure.ZERO;
    if (text != null) {
      final Measure m = RenderUtils.calculateStringWidth(facesContext, link, text);
      width = width.add(m);
    }
    if ((text != null && image != null)) {
      width = width.add(4);
    }
    if (image != null) {
      width = width.add(16);
    }
    return width;
  }
}
