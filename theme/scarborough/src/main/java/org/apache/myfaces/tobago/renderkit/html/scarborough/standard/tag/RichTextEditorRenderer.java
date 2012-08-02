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
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ACTION_ONCLICK;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_ICON_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMAGE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL_POSITION;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STATE_PREVIEW;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_STYLE_BODY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_TOOL_BAR;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_MENUCOMMAND;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_TOOL_BAR;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICommand;
import org.apache.myfaces.tobago.component.UISelectBooleanCommand;
import org.apache.myfaces.tobago.context.ResourceManagerUtil;
import org.apache.myfaces.tobago.renderkit.HtmlUtils;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlConstants;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.taglib.component.ToolBarTag;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import java.io.IOException;

/** TODO: under construction */
public class RichTextEditorRenderer extends InputRendererBase {

  private static final Log LOG = LogFactory.getLog(RichTextEditorRenderer.class);

  public static final String CHANGE_BUTTON = "togleState";

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    super.decode(facesContext, component);
    String actionId = ComponentUtil.findPage(facesContext, component).getActionId();
    if (actionId != null
        && actionId.equals(component.getClientId(facesContext) + CHANGE_BUTTON)) {
      boolean state
          = ComponentUtil.getBooleanAttribute(component, ATTR_STATE_PREVIEW);
      component.getAttributes().put(ATTR_STATE_PREVIEW, !state);
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

  public void encodeEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIInput component = (UIInput) uiComponent;

    HtmlRendererUtil.createHeaderAndBodyStyles(facesContext, component);


    boolean previewState
        = ComponentUtil.getBooleanAttribute(component, ATTR_STATE_PREVIEW);
    // FIXME: remove this when i18n is ok

    String clientId = component.getClientId(facesContext);

    TobagoResponseWriter writer = HtmlRendererUtil.getTobagoResponseWriter(facesContext);

    StyleClasses containerClasses = StyleClasses.ensureStyleClassesCopy(component);
    containerClasses.addClass("richTextEditor", "container");

    writer.startElement(HtmlConstants.DIV, component);
    writer.writeClassAttribute(containerClasses);
    writer.writeStyleAttribute();
    // class, stly.width, style.height

    UIComponent toolbar = component.getFacet(FACET_TOOL_BAR);
    if (toolbar == null) {
      toolbar = createToolbar(facesContext, component);
    }

    facesContext.getExternalContext().getRequestMap().put(
        "tobagoRichtextPreviewState", previewState ? Boolean.TRUE : Boolean.FALSE);

    RenderUtil.encode(facesContext, toolbar);
//    renderToolBar(facesContext, writer, component);

    String content = getCurrentValue(facesContext, component);

    StyleClasses bodyClasses = StyleClasses.ensureStyleClassesCopy(component);
    bodyClasses.addClass("richTextEditor", "body");

    if (previewState) {
      writer.startElement(HtmlConstants.INPUT, component);
      writer.writeAttribute(HtmlAttributes.TYPE, "hidden", false);
      writer.writeNameAttribute(clientId);
      writer.writeAttribute(HtmlAttributes.VALUE, content, true);
      writer.endElement(HtmlConstants.INPUT);

      writer.startElement(HtmlConstants.DIV, component);
      writer.writeClassAttribute(bodyClasses);
      writer.writeIdAttribute(clientId);

      writer.writeStyleAttribute();
      writer.writeText("");
      writer.write(RichTextEditorRenderer.contentToHtml(content));

      writer.endElement(HtmlConstants.DIV);
    } else {
      writer.startElement(HtmlConstants.TEXTAREA, component);
      writer.writeClassAttribute(bodyClasses);
      writer.writeNameAttribute(clientId);
      writer.writeIdAttribute(clientId);
      writer.writeAttribute(HtmlAttributes.STYLE, null, ATTR_STYLE_BODY);
      String onchange = HtmlUtils.generateOnchange(component, facesContext);
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
    UIPanel toolbar = (UIPanel) ComponentUtil.createComponent(
        facesContext, UIPanel.COMPONENT_TYPE, RENDERER_TYPE_TOOL_BAR);
    String clientId = component.getClientId(facesContext);

    component.getFacets().put(FACET_TOOL_BAR, toolbar);
    toolbar.getAttributes().put(ATTR_ICON_SIZE, ToolBarTag.ICON_SMALL);
    toolbar.getAttributes().put(ATTR_LABEL_POSITION, ToolBarTag.LABEL_OFF);

    UICommand //command = (UICommand) ComponentUtil.createComponent(
//        facesContext, UICommand.COMPONENT_TYPE, RENDERER_TYPE_MENUCOMMAND);
//    toolbar.getChildren().add(command);

    command = (UICommand) ComponentUtil.createComponent(
        facesContext, UISelectBooleanCommand.COMPONENT_TYPE, RENDERER_TYPE_MENUCOMMAND);
    toolbar.getChildren().add(command);

    command.getAttributes().put(ATTR_IMAGE, "image/tobago-richtext-edit.gif");
    command.setValueBinding(ATTR_DISABLED, ComponentUtil.createValueBinding("#{! tobagoRichtextPreviewState}"));
    command.setValueBinding(ATTR_VALUE, ComponentUtil.createValueBinding("#{!tobagoRichtextPreviewState}"));

    String title = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.richtexteditor.edit.title");
    command.getAttributes().put(ATTR_TIP, title);

    String onClick = "Tobago.submitAction2(this, '" 
        + clientId + RichTextEditorRenderer.CHANGE_BUTTON + "', null, null)";
    command.getAttributes().put(ATTR_ACTION_ONCLICK, onClick);

    command = (UICommand) ComponentUtil.createComponent(
        facesContext, UISelectBooleanCommand.COMPONENT_TYPE, RENDERER_TYPE_MENUCOMMAND);
    toolbar.getChildren().add(command);
    //command.getAttributes().put(ATTR_COMMAND_TYPE, ToolBarSelectBooleanTag.COMMAND_TYPE);
    command.getAttributes().put(ATTR_IMAGE, "image/tobago-richtext-preview.gif");
    command.setValueBinding(ATTR_DISABLED, ComponentUtil.createValueBinding("#{tobagoRichtextPreviewState}"));
    command.setValueBinding(ATTR_VALUE, ComponentUtil.createValueBinding("#{tobagoRichtextPreviewState}"));

    title = ResourceManagerUtil.getPropertyNotNull(
        facesContext, "tobago", "tobago.richtexteditor.preview.title");
    command.getAttributes().put(ATTR_TIP, title);
    command.getAttributes().put(ATTR_ACTION_ONCLICK, onClick);

    command = (UICommand) ComponentUtil.createComponent(
        facesContext, UICommand.COMPONENT_TYPE, RENDERER_TYPE_MENUCOMMAND);
    toolbar.getChildren().add(command);
    command.getAttributes().put(ATTR_IMAGE, "image/config.gif");
    command.getAttributes().put(ATTR_ACTION_ONCLICK, "Tobago.doEditorCommand(this);");

    return toolbar;
  }

}
