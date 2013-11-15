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

import org.apache.myfaces.tobago.ajax.AjaxUtils;
import org.apache.myfaces.tobago.component.Attributes;
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
import org.apache.myfaces.tobago.context.Markup;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.component.AbstractUIPage;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutComponentRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MessagesRenderer extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(MessagesRenderer.class);

  public static final String CLOSE_POPUP = "closePopup";

  @Override
  public void encodeEnd(final FacesContext facesContext, final UIComponent component) throws IOException {

    final UIMessages messages = (UIMessages) component;

    if (messages.isConfirmation()) {
      createPopup(facesContext, messages);
      return;
    }

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    if (LOG.isDebugEnabled()) {
      LOG.debug("facesContext is " + facesContext.getClass().getName());
    }
    final List<UIMessages.Item> messageList = messages.createMessageList(facesContext);

    if (messageList.size() > 0) { // in ie empty span gets a height
      writer.startElement(HtmlElements.SPAN, messages);
      writer.writeClassAttribute(Classes.create(messages));
      HtmlRendererUtils.writeDataAttributes(facesContext, writer, messages);
      writer.writeStyleAttribute(new Style(facesContext, messages));

      // with id
      /*String focusId = null;
      Iterator clientIds;
      if (ComponentUtils.getBooleanAttribute(messages, Attributes.GLOBAL_ONLY)) {
        ArrayList<String> list = new ArrayList<String>(1);
        list.add(null);
        clientIds = list.iterator();
      } else {
        clientIds = facesContext.getClientIdsWithMessages();
      }*/

      for (final UIMessages.Item item : messageList) {
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
      writer.endElement(HtmlElements.SPAN);
      if (messages.getFor() == null) {
        final String clientId = messages.getClientId(facesContext);
        writer.startElement(HtmlElements.INPUT, null);
        writer.writeAttribute(HtmlAttributes.VALUE, Boolean.TRUE.toString(), false);
        writer.writeAttribute(HtmlAttributes.ID,
            clientId + ComponentUtils.SUB_SEPARATOR + "messagesExists", false);
        writer.writeAttribute(HtmlAttributes.NAME,
            clientId + ComponentUtils.SUB_SEPARATOR + "messagesExists", false);
        writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
        writer.endElement(HtmlElements.INPUT);
      }
    }
    if (messages.getFor() == null
        && !AjaxUtils.isAjaxRequest(facesContext)) {
      AjaxInternalUtils.storeMessagesClientIds(facesContext, messages);
    }
  }

  private void createPopup(final FacesContext facesContext, final UIMessages messages) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("POPUP");
    }
    final String id
        = messages.getId() != null ? messages.getId() + "popup" : facesContext.getViewRoot().createUniqueId();
    final UIPopup popup = (UIPopup)
        CreateComponentUtils.createComponent(facesContext, UIPopup.COMPONENT_TYPE, RendererTypes.POPUP, id);
    popup.getAttributes().put(Attributes.Z_INDEX, 10);

    final AbstractUIPage page = ComponentUtils.findPage(facesContext, messages);

    popup.setWidth(page.getCurrentWidth().subtract(200));
    popup.setHeight(page.getCurrentHeight().subtract(200));
    popup.setLeft(Measure.valueOf(100));
    popup.setTop(Measure.valueOf(100));
    popup.setRendered(true);
    popup.setActivated(true);
    popup.onComponentPopulated(facesContext, messages);
    FacesContextUtils.addPopup(facesContext, popup);

    Map<String, Object> okButtonAttributes = popup.getAttributes();
    okButtonAttributes.put(Attributes.POPUP_RESET, Boolean.TRUE);

    final UIComponent box = CreateComponentUtils.createComponent(
        facesContext, UIBox.COMPONENT_TYPE, RendererTypes.BOX);
    popup.getChildren().add(box);
    box.setId("box");
    // TODO: set string resources in renderer
    box.getAttributes().put(Attributes.LABEL, ResourceManagerUtils.getPropertyNotNull(
        facesContext, "tobago", "tobago.message.confirmation.title"));
    UIComponent layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "layout");
    box.getFacets().put(Facets.LAYOUT, layout);
    layout.getAttributes().put(Attributes.ROWS, "*;auto");
    layout.getAttributes().put(Attributes.MARGIN, Measure.valueOf(10));

    final UICell scrollPanel = (UICell)
        CreateComponentUtils.createComponent(facesContext, UICell.COMPONENT_TYPE, "Cell", "messagePanel");
    box.getChildren().add(scrollPanel);

    messages.getParent().getChildren().remove(messages);
    messages.setConfirmation(false);
    scrollPanel.onComponentPopulated(facesContext, messages);
    scrollPanel.setScrollbars("auto");
    scrollPanel.getChildren().add(messages);

    final UIComponent buttonPanel = CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.PANEL, "buttonPanel");
    layout = CreateComponentUtils.createComponent(
        facesContext, UIGridLayout.COMPONENT_TYPE, RendererTypes.GRID_LAYOUT, "buttonPanelLayout");
    buttonPanel.getFacets().put(Facets.LAYOUT, layout);
    layout.getAttributes().put(Attributes.COLUMNS, "*;100px");
    layout.getAttributes().put(Attributes.ROWS, "auto");

    box.getChildren().add(buttonPanel);

    final UICell space = (UICell)
        CreateComponentUtils.createComponent(facesContext, UICell.COMPONENT_TYPE, "Cell", "space");
    buttonPanel.getChildren().add(space);
    space.onComponentPopulated(facesContext, messages);

    final UICommand okButton = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UIButton.COMPONENT_TYPE, RendererTypes.BUTTON, CLOSE_POPUP);
    buttonPanel.getChildren().add(okButton);
    okButtonAttributes = okButton.getAttributes();
    okButtonAttributes.put(Attributes.LABEL, ResourceManagerUtils.getPropertyNotNull(
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
  private void encodeMessage(
      final TobagoResponseWriter writer, final UIMessages messages, final FacesMessage message, final String clientId)
      throws IOException {

    final String summary = message.getSummary();
    final String detail = message.getDetail();
    writer.startElement(HtmlElements.LABEL, null);
    if (clientId != null) {
      writer.writeAttribute(HtmlAttributes.FOR, clientId, false);
    }
    writer.writeAttribute(HtmlAttributes.TITLE, detail, true);
    final Markup markup = ComponentUtils.markupOfSeverity(message.getSeverity());
    writer.writeClassAttribute(Classes.create(messages, "item", markup));
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
    writer.endElement(HtmlElements.LABEL);
    writer.startElement(HtmlElements.BR, null);
    writer.endElement(HtmlElements.BR);

    message.rendered();
  }

  @Override
  public Measure getPreferredHeight(final FacesContext facesContext, final Configurable component) {
    final Measure measure = super.getPreferredHeight(facesContext, component);
    final UIMessages messages = (UIMessages) component;
    final int count = messages.createMessageList(facesContext).size();
    return measure.multiply(count);
  }
}
