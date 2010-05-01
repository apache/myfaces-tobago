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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UILink;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.internal.util.Deprecation;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class LinkRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(LinkRenderer.class);

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    AbstractUICommand link = (AbstractUICommand) component;
    String clientId = link.getClientId(facesContext);
    CommandRendererHelper helper = new CommandRendererHelper(facesContext, link, CommandRendererHelper.Tag.ANCHOR);
    String href = helper.getHref();
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(link);

    if (helper.isDisabled()) {
      writer.startElement(HtmlConstants.SPAN, link);
    } else {
      writer.startElement(HtmlConstants.A, link);
      writer.writeAttribute(HtmlAttributes.HREF, href, true);
      if (helper.getOnclick() != null) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true);
      }
      if (helper.getTarget() != null) {
        writer.writeAttribute(HtmlAttributes.TARGET, helper.getTarget(), true);
      }
      Integer tabIndex = null;
      if (link instanceof UILink) {
        tabIndex = ((UILink) link).getTabIndex();
      } else {
        Deprecation.LOG.warn("LinkRenderer should only render UILink but got " + link.getClass().getName() 
        + " id=" + link.getClientId(facesContext));
      }
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }
    Style style = new Style(facesContext, link);
    writer.writeStyleAttribute(style);
    HtmlRendererUtils.renderDojoDndItem(component, writer, true);
    writer.writeClassAttribute();
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtils.renderTip(link, writer);
    writer.flush();

//  image
    String image = (String) link.getAttributes().get(Attributes.IMAGE);
    if (image != null) {
      if (image.startsWith("HTTP:") || image.startsWith("FTP:") || image.startsWith("/")) {
        // absolute Path to image : nothing to do
      } else {
        image = getImageWithPath(facesContext, image, helper.isDisabled());
      }
      writer.startElement(HtmlConstants.IMG, link);
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      writer.writeAttribute(HtmlAttributes.BORDER, 0); // TODO: is border=0 setting via style possible?
      HtmlRendererUtils.renderImageTip(link, writer);
      HtmlRendererUtils.renderTip(link, writer);
      writer.endElement(HtmlConstants.IMG);
    }

//  label
    if (label.getText() != null) {
      if (image != null) {
        writer.write(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }

      HtmlRendererUtils.addClickAcceleratorKey(facesContext, clientId, label.getAccessKey());
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (!(component instanceof UICommand)) {
      LOG.error("Wrong type: Need " + UICommand.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    ResponseWriter writer = facesContext.getResponseWriter();
    if (ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      writer.endElement(HtmlConstants.SPAN);
    } else {
      writer.endElement(HtmlConstants.A);
    }
  }
}
