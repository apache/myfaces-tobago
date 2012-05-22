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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.UITreeCommand;
import org.apache.myfaces.tobago.component.UITreeNode;
import org.apache.myfaces.tobago.internal.component.AbstractUICommand;
import org.apache.myfaces.tobago.internal.util.AccessKeyMap;
import org.apache.myfaces.tobago.renderkit.CommandRendererBase;
import org.apache.myfaces.tobago.renderkit.LabelWithAccessKey;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.util.CommandRendererHelper;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class TreeCommandRenderer extends CommandRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(TreeCommandRenderer.class);

  @Override
  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    final UITreeCommand command = (UITreeCommand) component;
    final UITreeNode node = ComponentUtils.findAncestor(command, UITreeNode.class);
    // Todo: use an expression?
    command.setDisabled(node.isDisabled());
    super.prepareRender(facesContext, component);
  }

  @Override
  public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {

    UITreeCommand command = (UITreeCommand) component;
    String clientId = command.getClientId(facesContext);
    CommandRendererHelper helper = new CommandRendererHelper(facesContext, command, CommandRendererHelper.Tag.ANCHOR);
    String href = helper.getHref();
    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    LabelWithAccessKey label = new LabelWithAccessKey(command);

    if (helper.isDisabled()) {
      writer.startElement(HtmlElements.SPAN, command);
    } else {
      writer.startElement(HtmlElements.A, command);
      writer.writeAttribute(HtmlAttributes.HREF, href, true);
      if (helper.getOnclick() != null) {
        writer.writeAttribute(HtmlAttributes.ONCLICK, helper.getOnclick(), true);
      }
      if (helper.getTarget() != null) {
        writer.writeAttribute(HtmlAttributes.TARGET, helper.getTarget(), true);
      }
      writer.writeNameAttribute(clientId);
    }
    writer.writeStyleAttribute(createStyle(facesContext, command));
    writer.writeClassAttribute(Classes.create(command));
    writer.writeIdAttribute(clientId);
    HtmlRendererUtils.renderTip(command, writer);
    writer.flush();

//  label
    if (label.getText() != null) {
      HtmlRendererUtils.writeLabelWithAccessKey(writer, label);
    }

    if (label.getAccessKey() != null) {
      if (LOG.isInfoEnabled()
          && !AccessKeyMap.addAccessKey(facesContext, label.getAccessKey())) {
        LOG.info("duplicated accessKey : " + label.getAccessKey());
      }

      HtmlRendererUtils.addClickAcceleratorKey(facesContext, clientId, label.getAccessKey());
    }
  }

  protected Style createStyle(FacesContext facesContext, AbstractUICommand link) {
    return new Style(facesContext, link);
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component)
      throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    if (ComponentUtils.getBooleanAttribute(component, Attributes.DISABLED)) {
      writer.endElement(HtmlElements.SPAN);
    } else {
      writer.endElement(HtmlElements.A);
    }
  }
}
