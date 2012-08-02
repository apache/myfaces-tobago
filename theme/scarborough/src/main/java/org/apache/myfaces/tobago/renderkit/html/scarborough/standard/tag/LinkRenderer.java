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

/*
 * Created 07.02.2003 16:00:00.
 * $Id$
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UILinkCommand;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.html.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.util.AccessKeyMap;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;

public class LinkRenderer extends CommandRendererBase {

  private static final Log LOG = LogFactory.getLog(LinkRenderer.class);

  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
    if (!(component instanceof UICommand)) {
      LOG.error("Wrong type: Need " + UICommand.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    UICommand command = (UICommand) component;
    String clientId = command.getClientId(facesContext);
    CommandRendererHelper helper = new CommandRendererHelper(facesContext, command, CommandRendererHelper.Tag.ANCHOR);
    String href = helper.getHref();
    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(command);

    if (helper.isDisabled()) {
      writer.startElement(HtmlConstants.SPAN, command);
    } else {
      writer.startElement(HtmlConstants.A, command);
      writer.writeAttribute(HtmlAttributes.HREF, href, true);
      if (helper.getOnclick() != null) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true);
      }
      if (helper.getTarget() != null) {
        writer.writeAttribute(HtmlAttributes.TARGET, helper.getTarget(), true);
      }
      Integer tabIndex = null;
      if (command instanceof UILinkCommand) {
        tabIndex = ((UILinkCommand) command).getTabIndex();
      }
      if (tabIndex != null) {
        writer.writeAttribute(HtmlAttributes.TABINDEX, tabIndex);
      }
    }
    writer.writeClassAttribute();
    writer.writeIdAttribute(clientId);
    writer.writeNameAttribute(clientId);
    HtmlRendererUtil.renderTip(command, writer);
    writer.flush();

//  image
    String image = (String) command.getAttributes().get(ATTR_IMAGE);
    if (image != null) {
      if (ResourceManagerUtil.isAbsoluteResource(image)) {
        // absolute Path to image : nothing to do
      } else {
        image = ResourceManagerUtil.getImageWithPath(facesContext, image, helper);
      }
      writer.startElement(HtmlConstants.IMG, command);
      writer.writeAttribute(HtmlAttributes.SRC, image, true);
      writer.writeAttribute(HtmlAttributes.BORDER, 0); // TODO: is border=0 setting via style possible?
      HtmlRendererUtil.renderImageTip(command, writer);
      HtmlRendererUtil.renderTip(command, writer);
      writer.endElement(HtmlConstants.IMG);
    }

//  label
    if (label.getText() != null) {
      if (image != null) {
        writer.write(" "); // separator: e.g. &nbsp;
      }
      HtmlRendererUtil.writeLabelWithAccessKey(writer, label);
    }

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("dublicated accessKey : " + label.getAccessKey());
      }

      HtmlRendererUtil.addClickAcceleratorKey(facesContext, clientId, label.getAccessKey());
    }
  }

  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (!(component instanceof UICommand)) {
      LOG.error("Wrong type: Need " + UICommand.class.getName() + ", but was " + component.getClass().getName());
      return;
    }

    ResponseWriter writer = facesContext.getResponseWriter();
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      writer.endElement(HtmlConstants.SPAN);
    } else {
      writer.endElement(HtmlConstants.A);
    }
  }
}

