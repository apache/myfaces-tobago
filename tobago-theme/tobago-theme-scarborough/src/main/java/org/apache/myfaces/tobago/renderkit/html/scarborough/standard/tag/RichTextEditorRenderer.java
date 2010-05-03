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
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UIInput;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.component.UIToolBar;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.css.Style;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.renderkit.html.util.HtmlRendererUtils;
import org.apache.myfaces.tobago.renderkit.util.RenderUtil;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

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
  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    super.decode(facesContext, component);
    String actionId = ComponentUtils.findPage(facesContext, component).getActionId();
    if (actionId != null
        && actionId.equals(component.getClientId(facesContext) + CHANGE_BUTTON)) {
      boolean state
          = ComponentUtils.getBooleanAttribute(component, Attributes.STATE_PREVIEW);
      component.getAttributes().put(Attributes.STATE_PREVIEW, !state);
      facesContext.renderResponse();

    }
    ((EditableValueHolder) component).setValid(true);
  }

  public static String contentToHtml(String content) {
    try {
      LOG.warn("richtext switched off, because of dependencies");
      return content;
//  FIXME: check dependencies
//      return WikiParser.toHtml(content);
    } catch (Exception e) {
      LOG.error("failed to parser wiki markup", e);
    }
    return content;
  }

  @Override
  public void encodeEnd(FacesContext facesContext,      UIComponent component) throws IOException {

    UIInput input = (UIInput) component;

    boolean previewState
        = ComponentUtils.getBooleanAttribute(input, Attributes.STATE_PREVIEW);
    // FIXME: remove this when i18n is ok

    String clientId = input.getClientId(facesContext);

    TobagoResponseWriter writer = HtmlRendererUtils.getTobagoResponseWriter(facesContext);

    StyleClasses containerClasses = StyleClasses.ensureStyleClassesCopy(input);
    containerClasses.addClass("richTextEditor", "container");

    writer.startElement(HtmlConstants.DIV, input);
    writer.writeClassAttribute(containerClasses);
    Style style = new Style(facesContext, input);
    writer.writeStyleAttribute(style);

    UIComponent toolbar = input.getFacet(Facets.TOOL_BAR);
    if (toolbar == null) {
      toolbar = createToolbar(facesContext, input);
    }

    facesContext.getExternalContext().getRequestMap().put(
        "tobagoRichtextPreviewState", previewState ? Boolean.TRUE : Boolean.FALSE);

    RenderUtil.encode(facesContext, toolbar);
//    renderToolBar(facesContext, writer, input);

    String content = getCurrentValue(facesContext, input);

    StyleClasses bodyClasses = StyleClasses.ensureStyleClassesCopy(input);
    bodyClasses.addClass("richTextEditor", "body");

    if (previewState) {
      writer.startElement(HtmlConstants.INPUT, input);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId);
      writer.writeAttribute(HtmlAttributes.VALUE, content, true);
      writer.endElement(HtmlConstants.INPUT);

      writer.startElement(HtmlConstants.DIV, input);
      writer.writeClassAttribute(bodyClasses);
      writer.writeIdAttribute(clientId);

      writer.writeStyleAttribute(style);
      writer.writeText("");
      writer.write(RichTextEditorRenderer.contentToHtml(content));

      writer.endElement(HtmlConstants.DIV);
    } else {
      writer.startElement(HtmlConstants.TEXTAREA, input);
      writer.writeClassAttribute(bodyClasses);
      writer.writeNameAttribute(clientId);
      writer.writeIdAttribute(clientId);
      writer.writeStyleAttribute(style);
      String onchange = HtmlUtils.generateOnchange(input, facesContext);
      if (null != onchange) {
        writer.writeAttribute(HtmlAttributes.ONCHANGE, onchange, null);
      }

      if (content != null) {
        writer.writeText(content);
      }

      writer.endElement(HtmlConstants.TEXTAREA);
    }
    writer.endElement(HtmlConstants.DIV);
  }

  private UIComponent createToolbar(FacesContext facesContext, UIInput component) {
    UIPanel toolbar = (UIPanel) CreateComponentUtils.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RendererTypes.TOOL_BAR);
    String clientId = component.getClientId(facesContext);

    component.getFacets().put(Facets.TOOL_BAR, toolbar);
    toolbar.getAttributes().put(Attributes.ICON_SIZE, UIToolBar.ICON_SMALL);
    toolbar.getAttributes().put(Attributes.LABEL_POSITION, UIToolBar.LABEL_OFF);

    UICommand //command = (UICommandBase) ComponentUtils.createComponent(
//        facesContext, UICommandBase.COMPONENT_TYPE, MENU_COMMAND);
//    toolbar.getChildren().add(command);

    command = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UISelectBooleanCommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND);
    toolbar.getChildren().add(command);

    command.getAttributes().put(Attributes.IMAGE, "image/tobago-richtext-edit.gif");
    command.setValueBinding(Attributes.DISABLED, ComponentUtils.createValueBinding("#{! tobagoRichtextPreviewState}"));
    command.setValueBinding(Attributes.VALUE, ComponentUtils.createValueBinding("#{!tobagoRichtextPreviewState}"));

    String title = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.richtexteditor.edit.title");
    command.getAttributes().put(Attributes.TIP, title);

    String onClick = "Tobago.submitAction(this, '"
        + clientId + RichTextEditorRenderer.CHANGE_BUTTON + "')";
    command.getAttributes().put(Attributes.ONCLICK, onClick);

    command = (UICommand) CreateComponentUtils.createComponent(
        facesContext, UISelectBooleanCommand.COMPONENT_TYPE, RendererTypes.MENU_COMMAND);
    toolbar.getChildren().add(command);
    //command.getAttributes().put(ATTR_COMMAND_TYPE, ToolBarSelectBooleanTag.COMMAND_TYPE);
    command.getAttributes().put(Attributes.IMAGE, "image/tobago-richtext-preview.gif");
    command.setValueBinding(Attributes.DISABLED, ComponentUtils.createValueBinding("#{tobagoRichtextPreviewState}"));
    command.setValueBinding(Attributes.VALUE, ComponentUtils.createValueBinding("#{tobagoRichtextPreviewState}"));

    title = ResourceManagerUtil.getPropertyNotNull(
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
