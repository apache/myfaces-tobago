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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButtonCommand;
import org.apache.myfaces.tobago.component.UICell;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIMessages;
import org.apache.myfaces.tobago.component.UIPage;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.MessageRendererBase;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class MessagesRenderer extends MessageRendererBase {

  private static final Log LOG = LogFactory.getLog(MessagesRenderer.class);

  public static final String CLOSE_POPUP = "closePopup";

  @Override
  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    UIMessages messages = (UIMessages) component;
    int count = messages.getMessageListCount(facesContext);
    if (LOG.isDebugEnabled()) {
      LOG.debug("component = '" + component + "'");
      LOG.debug("here are " + count + " messages");
    }
    return (count > 0)
        ? count * getConfiguredValue(facesContext, component, "messageHeight")
        : getConfiguredValue(facesContext, component, "fixedHeight");
  }

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UIMessages messages = (UIMessages) component;

    if (messages.isConfirmation()) {
      createPopup(facesContext, messages);
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContext is " + facesContext.getClass().getName());
    }
    if (facesContext.getMessages().hasNext()) { // in ie empty span gets a height
      writer.startElement(HtmlConstants.SPAN, messages);
      writer.writeClassAttribute("tobago-validation-message");
      writer.writeStyleAttribute();

      for (UIMessages.Item item : messages.createMessageList(facesContext)) {
        encodeMessage(writer, messages, item.getFacesMessage(), item.getClientId());
      }

      writer.endElement(HtmlConstants.SPAN);
    }
  }

  private void createPopup(FacesContext facesContext, UIMessages messages) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("POPUP");
    }
    String id = messages.getId() != null ? messages.getId() + "popup" : facesContext.getViewRoot().createUniqueId();
    final UIPopup popup = (UIPopup)
        ComponentUtil.createComponent(facesContext, UIPopup.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_POPUP, id);

    UIPage page = ComponentUtil.findPage(facesContext, messages);

    popup.setWidth(page.getWidth() - 200 + "px");
    popup.setHeight(page.getHeight() - 200 + "px");
    popup.setLeft("100px");
    popup.setTop("100px");
    popup.setRendered(true);
    popup.setActivated(true);
    page.getPopups().add(popup);

    Map<String, Object> okButtonAttributes = popup.getAttributes();
    okButtonAttributes.put(TobagoConstants.ATTR_POPUP_RESET, Boolean.TRUE);

    final UIComponent box = ComponentUtil.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_BOX);
    popup.getChildren().add(box);
    box.setId("box");
    // TODO: set string resources in renderer
    box.getAttributes().put(TobagoConstants.ATTR_LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.message.confirmation.title"));
    UIComponent layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_GRID_LAYOUT, "layout");
    box.getFacets().put(TobagoConstants.FACET_LAYOUT, layout);
    layout.getAttributes().put(TobagoConstants.ATTR_ROWS, "*;fixed");
    layout.getAttributes().put(TobagoConstants.ATTR_MARGIN, "10");

    final UICell scrollPanel = (UICell)
        ComponentUtil.createComponent(facesContext, UICell.COMPONENT_TYPE, "Cell", "messagePanel");
    box.getChildren().add(scrollPanel);

    messages.getParent().getChildren().remove(messages);
    messages.setConfirmation(false);
    scrollPanel.setScrollbars("auto");
    scrollPanel.getChildren().add(messages);

    UIComponent buttonPanel = ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_PANEL, "buttonPanel");
    layout = ComponentUtil.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_GRID_LAYOUT, "buttonPanelLayout");
    buttonPanel.getFacets().put(TobagoConstants.FACET_LAYOUT, layout);
    layout.getAttributes().put(TobagoConstants.ATTR_COLUMNS, "*;100px");
    layout.getAttributes().put(TobagoConstants.ATTR_ROWS, "fixed");

    box.getChildren().add(buttonPanel);

    final UICell space = (UICell)
        ComponentUtil.createComponent(facesContext, UICell.COMPONENT_TYPE, "Cell", "space");
    buttonPanel.getChildren().add(space);

    final UICommand okButton = (UICommand) ComponentUtil.createComponent(
        facesContext, UIButtonCommand.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_BUTTON, CLOSE_POPUP);
    buttonPanel.getChildren().add(okButton);
    okButtonAttributes = okButton.getAttributes();
    okButtonAttributes.put(TobagoConstants.ATTR_LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.message.confirmation.okay"));
    okButtonAttributes.put("popupClose", "immediate");
  }

  /*
    private void encodeMessagesForId(FacesContext facesContext,
        TobagoResponseWriter writer, String clientId, boolean showSummary, boolean showDetail) throws IOException {
      Iterator iterator = facesContext.getMessages(clientId);
      while (iterator.hasNext()) {
        FacesMessage message = (FacesMessage) iterator.next();
        if (LOG.isDebugEnabled()) {
          LOG.debug("message = " + message.getSummary());
        }
        encodeMessage(writer, message, clientId, showSummary, showDetail);
      }
    }
  */
  private void encodeMessage(TobagoResponseWriter writer, UIMessages messages, FacesMessage message, String clientId)
      throws IOException {

    String summary = message.getSummary();
    String detail = message.getDetail();
    writer.startElement(HtmlConstants.LABEL, null);
    if (clientId != null) {
      writer.writeAttribute(HtmlAttributes.FOR, clientId, false);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, detail, true);
    StyleClasses classes = new StyleClasses();
    classes.addMarkupClass("messages", message.getSeverity().toString().toLowerCase());
    writer.writeClassAttribute(classes);
    boolean writeEmptyText = true;
    if (summary != null && messages.isShowSummary()) {
      writer.writeText(summary);
      writeEmptyText = false;
      if (detail != null && messages.isShowDetail()) {
        writer.writeText(" ");
      }
    }
    if (detail != null && messages.isShowDetail()) {
      writeEmptyText = false;
      writer.writeText(detail);
    }
    if (writeEmptyText) {
      writer.writeText("");
    }
    writer.endElement(HtmlConstants.LABEL);
    writer.startElement(HtmlConstants.BR, null);
    writer.endElement(HtmlConstants.BR);
  }

}
