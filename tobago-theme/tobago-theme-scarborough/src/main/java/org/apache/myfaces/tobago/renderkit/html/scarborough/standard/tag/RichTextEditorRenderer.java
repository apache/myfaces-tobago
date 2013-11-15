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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIIn;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManagerUtils;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Classes;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;
import org.apache.myfaces.tobago.renderkit.html.HtmlInputTypes;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.util.CreateComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;

/** TODO: under construction */
public class RichTextEditorRenderer extends InputRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(RichTextEditorRenderer.class);

  public static final String CHANGE_BUTTON = "togleState";

  @Override
  public void decode(final FacesContext facesContext, final UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    super.decode(facesContext, component);
    final String actionId = ComponentUtils.findPage(facesContext, component).getActionId();
    if (actionId != null
        && actionId.equals(component.getClientId(facesContext) + CHANGE_BUTTON)) {
      final boolean state
          = ComponentUtils.getBooleanAttribute(component, Attributes.STATE_PREVIEW);
      component.getAttributes().put(Attributes.STATE_PREVIEW, !state);
      facesContext.renderResponse();

    }
    ((EditableValueHolder) component).setValid(true);
  }

  public static String contentToHtml(final String content) {
    try {
      LOG.warn("richtext switched off, because of dependencies");
      return content;
//  FIXME: check dependencies
//      return WikiParser.toHtml(content);
    } catch (final Exception e) {
      LOG.error("failed to parser wiki markup", e);
    }
    return content;
  }

  @Override
  public void encodeEnd(final FacesContext facesContext,      final UIComponent component) throws IOException {

    final UIIn input = (UIIn) component;

    final boolean previewState
        = ComponentUtils.getBooleanAttribute(input, Attributes.STATE_PREVIEW);
    // FIXME: remove this when i18n is ok

    final String clientId = input.getClientId(facesContext);

    final TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    writer.startElement(HtmlElements.DIV, input);
    writer.writeClassAttribute(Classes.create(input, "container"));
    HtmlRendererUtils.writeDataAttributes(facesContext, writer, input);
    final Style style = new Style(facesContext, input);
    writer.writeStyleAttribute(style);

    UIComponent toolbar = input.getFacet(Facets.TOOL_BAR);
    if (toolbar == null) {
      toolbar = createToolbar(facesContext, input);
    }

    facesContext.getExternalContext().getRequestMap().put(
        "tobagoRichtextPreviewState", previewState ? Boolean.TRUE : Boolean.FALSE);

    RenderUtils.encode(facesContext, toolbar);
//    renderToolBar(facesContext, writer, input);

    final String content = getCurrentValue(facesContext, input);

    if (previewState) {
      writer.startElement(HtmlElements.INPUT, input);
      writer.writeAttribute(HtmlAttributes.TYPE, HtmlInputTypes.HIDDEN, false);
      writer.writeNameAttribute(clientId);
      writer.writeAttribute(HtmlAttributes.VALUE, content, true);
      writer.endElement(HtmlElements.INPUT);

      writer.startElement(HtmlElements.DIV, input);
      writer.writeClassAttribute(Classes.create(input, "body"));
      writer.writeIdAttribute(clientId);

      writer.writeStyleAttribute(style);
      writer.flush(); // is needed in some cases, e. g. TOBAGO-1094
      writer.write(RichTextEditorRenderer.contentToHtml(content));

      writer.endElement(HtmlElements.DIV);
    } else {
      writer.startElement(HtmlElements.TEXTAREA, input);
      writer.writeClassAttribute(Classes.create(input, "body"));
      writer.writeNameAttribute(clientId);
      writer.writeIdAttribute(clientId);
      writer.writeStyleAttribute(style);
      final String onchange = HtmlUtils.generateOnchange(input, facesContext);
      if (null != onchange) {
        writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
      }

      if (content != null) {
        writer.writeText(content);
      }

      writer.endElement(HtmlElements.TEXTAREA);
    }
    writer.endElement(HtmlElements.DIV);
  }

  private UIComponent createToolbar(final FacesContext facesContext, final UIIn component) {
    final UIPanel toolbar = (UIPanel) CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.TOOL_BAR);
    final String clientId = component.getClientId(facesContext);

    component.getFacets().put(Facets.TOOL_BAR, toolbar);
    toolbar.getAttributes().put(Attributes.ICON_SIZE, UIToolBar.ICON_SMALL);
    toolbar.getAttributes().put(Attributes.LABEL_POSITION, UIToolBar.LABEL_OFF);

    UICommand //command = (AbstractUICommandBase) ComponentUtils.createComponent(
//        facesContext, AbstractUICommandBase.COMPONENT_TYPE, MENU_COMMAND);
//    toolbar.getChildren().add(command);

    command = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UISelectBooleanCommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND);
    toolbar.getChildren().add(command);

    command.getAttributes().put(Attributes.IMAGE, "image/tobago-richtext-edit.gif");
    command.setValueBinding(Attributes.DISABLED, ComponentUtils.createValueBinding("#{! tobagoRichtextPreviewState}"));
    command.setValueBinding(Attributes.VALUE, ComponentUtils.createValueBinding("#{!tobagoRichtextPreviewState}"));

    String title = ResourceManagerUtils.getPropertyNotNull(
        facesContext, "tobago", "tobago.richtexteditor.edit.title");
    command.getAttributes().put(Attributes.TIP, title);

    final String onClick
        = HtmlRendererUtils.createSubmitAction(clientId + RichTextEditorRenderer.CHANGE_BUTTON, true, null, null);
    command.getAttributes().put(Attributes.ONCLICK, onClick);

    command = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UISelectBooleanCommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND);
    toolbar.getChildren().add(command);
    //command.getAttributes().put(ATTR_COMMAND_TYPE, ToolBarSelectBooleanTag.COMMAND_TYPE);
    command.getAttributes().put(Attributes.IMAGE, "image/tobago-richtext-preview.gif");
    command.setValueBinding(Attributes.DISABLED, ComponentUtils.createValueBinding("#{tobagoRichtextPreviewState}"));
    command.setValueBinding(Attributes.VALUE, ComponentUtils.createValueBinding("#{tobagoRichtextPreviewState}"));

    title = ResourceManagerUtils.getPropertyNotNull(
        facesContext, "tobago", "tobago.richtexteditor.preview.title");
    command.getAttributes().put(Attributes.TIP, title);
    command.getAttributes().put(Attributes.ONCLICK, onClick);

    command = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UICommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND);
    toolbar.getChildren().add(command);
    command.getAttributes().put(Attributes.IMAGE, "image/config.gif");
    command.getAttributes().put(Attributes.ONCLICK, "Tobago.doEditorCommand(this);");

    return toolbar;
  }

}
