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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.CreateComponentUtils;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIButton;
import org.apache.myfaces.tobago.component.UICell;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIGridLayout;
import org.apache.myfaces.tobago.component.UIMessages;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.component.UIPopup;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MessagesRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MessagesRenderer.class);

  public static final String CLOSE_POPUP = "closePopup";

  @Override
  public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {

    UIMessages messages = (UIMessages) component;

    if (messages.isConfirmation()) {
      createPopup(facesContext, messages);
      return;
    }

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContext is " + facesContext.getClass().getName());
    }
    if (facesContext.getMessages().hasNext()) { // in ie empty span gets a height
      writer.startElement(HtmlConstants.SPAN, messages);
      writer.writeClassAttribute("tobago-validation-message");
      Style style = new Style(facesContext, messages);
      writer.writeStyleAttribute(style);

      // with id
      String focusId = null;
      Iterator clientIds;
      if (ComponentUtils.getBooleanAttribute(messages, Attributes.GLOBAL_ONLY)) {
        ArrayList<String> list = new ArrayList<String>(1);
        list.add(null);
        clientIds = list.iterator();
      } else {
        clientIds = facesContext.getClientIdsWithMessages();
      }

      for (UIMessages.Item item : messages.createMessageList(facesContext)) {
        encodeMessage(writer, messages, item.getFacesMessage(), item.getClientId());
      }
/*
      while(clientIds.hasNext()) {
        String clientId = (String) clientIds.next();
        encodeMessagesForId(facesContext, writer, clientId, showSummary, showDetail);
        if (focusId == null) {
          focusId = clientId;
        }
      }
  todo: don't forget: focus
      if (focusId != null) {
        ComponentUtils.findPage(facesContext, messages).setFocusId(focusId);
      }
*/
      writer.endElement(HtmlConstants.SPAN);
    }
  }

  private void createPopup(FacesContext facesContext, UIMessages messages) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("POPUP");
    }
    String id = messages.getId() != null ? messages.getId() + "popup" : facesContext.getViewRoot().createUniqueId();
    final UIPopup popup = (UIPopup)
        CreateComponentUtils.createComponent(facesContext, UIPopup.COMPONENT_TYPE, RendererTypes.POPUP, id);
    popup.getAttributes().put(Attributes.Z_INDEX, 10);

    AbstractUIPage page = ComponentUtils.findPage(facesContext, messages);

    popup.setWidth(page.getCurrentWidth().subtract(200));
    popup.setHeight(page.getCurrentHeight().subtract(200));
    popup.setLeft(Measure.valueOf(100));
    popup.setTop(Measure.valueOf(100));
    popup.setRendered(true);
    popup.setActivated(true);
    ((TobagoFacesContext) facesContext).getPopups().add(popup);

    Map<String, Object> okButtonAttributes = popup.getAttributes();
    okButtonAttributes.put(Attributes.POPUP_RESET, Boolean.TRUE);

    final UIComponent box = CreateComponentUtils.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, RendererTypes.BOX);
    popup.getChildren().add(box);
    box.setId("box");
    // TODO: set string resources in renderer
    box.getAttributes().put(Attributes.LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.message.confirmation.title"));
    UIComponent layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "layout");
    box.getFacets().put(Facets.LAYOUT, layout);
    layout.getAttributes().put(Attributes.ROWS, "*;fixed");
    layout.getAttributes().put(Attributes.MARGIN, "10");

    final UICell scrollPanel = (UICell)
        CreateComponentUtils.createComponent(facesContext, UICell.COMPONENT_TYPE, "Cell", "messagePanel");
    box.getChildren().add(scrollPanel);

    messages.getParent().getChildren().remove(messages);
    messages.setConfirmation(false);
    scrollPanel.setScrollbars("auto");
    scrollPanel.getChildren().add(messages);

    UIComponent buttonPanel = CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL, "buttonPanel");
    layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "buttonPanelLayout");
    buttonPanel.getFacets().put(Facets.LAYOUT, layout);
    layout.getAttributes().put(Attributes.COLUMNS, "*;100px");
    layout.getAttributes().put(Attributes.ROWS, "fixed");

    box.getChildren().add(buttonPanel);

    final UICell space = (UICell)
        CreateComponentUtils.createComponent(facesContext, UICell.COMPONENT_TYPE, "Cell", "space");
    buttonPanel.getChildren().add(space);

    final UICommand okButton = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON, CLOSE_POPUP);
    buttonPanel.getChildren().add(okButton);
    okButtonAttributes = okButton.getAttributes();
    okButtonAttributes.put(Attributes.LABEL, ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.message.confirmation.okay"));
    okButtonAttributes.put("popupClose", "immediate");
    return;
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

  @Override
  public Measure getHeight(FacesContext facesContext, Configurable component) {
    Measure measure = super.getPreferredHeight(facesContext, component);
    UIMessages messages = (UIMessages) component;
    int count = messages.createMessageList(facesContext).size();
    return measure.multiply(count);
  } 

  /*@Override
  public Measure getPreferredHeight(FacesContext facesContext, Configurable component) {
    Measure measure = super.getPreferredHeight(facesContext, component);
    UIMessages messages = (UIMessages) component;
    int count = messages.createMessageList(facesContext).size();
    return measure.multiply(count);
  }*/
}
