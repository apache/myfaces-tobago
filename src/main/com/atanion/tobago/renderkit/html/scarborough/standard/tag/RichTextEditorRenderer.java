/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.application.Application;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.component.UIOutput;
import javax.faces.component.UIInput;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class RichTextEditorRenderer extends InputRendererBase
 implements DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(RichTextEditorRenderer.class);

  public static final String CHANGE_BUTTON = "togleState";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    super.decode(facesContext, component);
    String actionId = ComponentUtil.findPage(component).getActionId();
    if (actionId != null
        && actionId.equals(component.getClientId(facesContext) + CHANGE_BUTTON)){
      Boolean state =
          (Boolean) component.getAttributes().get(TobagoConstants.ATTR_STATE_PREVIEW);
      component.getAttributes().put(TobagoConstants.ATTR_STATE_PREVIEW,
          new Boolean(! state.booleanValue()));
      facesContext.renderResponse();

    }
    ((EditableValueHolder)component).setValid(true);
  }


  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    Boolean state = (Boolean) component.getAttributes().get(TobagoConstants.ATTR_STATE_PREVIEW);
    // <div>'s border is outside, <textarea>'s border is inside
    return state.booleanValue() ? 2 : 0 ;
  }

  public static String contentToHtml(String content) {
    content = content.replaceAll("\\*(.*)\\*","<b>$1</b>");
    content = content.replaceAll("_(.*)_","<u>$1</u>");
    content = content.replaceAll("\n","<br />");
    content = content.replaceAll(" ","&nbsp;");

    return content;
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIInput component = (UIInput) uiComponent;

    String clientId = component.getClientId(facesContext);
    String state ;

    ResponseWriter writer = facesContext.getResponseWriter();


    boolean previewState = ((Boolean)
        component.getAttributes().get(TobagoConstants.ATTR_STATE_PREVIEW)).booleanValue();
    String content = ComponentUtil.currentValue(component);

    if (previewState) {
      state = TobagoResource.getProperty(facesContext, "text", "tobago.richtexteditor.edit");
      // fixme: remove this when i18n is ok
      if (state == null) {state = "Editor";}

      writer.startElement("input", component);
      writer.writeAttribute("type", "hidden", null);
      writer.writeAttribute("name", clientId, null);
      writer.writeAttribute("value", content, null);
      writer.endElement("input");

      writer.startElement("div", component);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

      writer.write(RichTextEditorRenderer.contentToHtml(content));

      writer.endElement("div");
    }
    else {
      state = TobagoResource.getProperty(facesContext, "text", "tobago.richtexteditor.preview");
      // fixme: remove this when i18n is ok
      if (state == null) {state = "Preview";}

      writer.startElement("textarea", component);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("name", clientId, null);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
      writer.writeAttribute("onchange",
          HtmlUtils.generateOnchange(component, facesContext), null);

      writer.writeText(content, null);

      writer.endElement("textarea");
    }


    String onClick = "submitAction('"
        + ComponentUtil.findPage(component).getFormId(facesContext) + "', '"
        + clientId + RichTextEditorRenderer.CHANGE_BUTTON + "')";
    String idPrefix = clientId.replace('.', '_').replace(':', '_');

    Application application = facesContext.getApplication();

    // fixme: welche funktion hat dieses image?
    UIGraphic image = (UIGraphic)
        application.createComponent(UIGraphic.COMPONENT_TYPE);
    image.setId(idPrefix + "Image");
    image.setValue("1x1.gif");
    image.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
    image.getAttributes().put(TobagoConstants.ATTR_HEIGHT, "4px");
    image.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    image.setRendered(true);
    image.setRendererType("Image");
    RenderUtil.encode(facesContext, image);

    UICommand button = (UICommand)
        application.createComponent(UICommand.COMPONENT_TYPE);
    button.setParent(component);
    button.getAttributes().put(TobagoConstants.ATTR_TYPE,
        TobagoConstants.COMMAND_TYPE_SCRIPT);
    button.getAttributes().put(TobagoConstants.ATTR_COMMAND_NAME, onClick);
    button.setId(idPrefix + "Button");
    button.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    button.setRendered(true);
    button.setRendererType("Button");

    UIOutput text = (UIOutput)
        application.createComponent(UIOutput.COMPONENT_TYPE);
    text.setValue(state);
    text.setId(idPrefix + "ButtonText");
    text.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    text.setRendered(true);
    text.setRendererType("Text");
    button.getChildren().add(text);
    RenderUtil.encode(facesContext, button);
  }
// ///////////////////////////////////////////// bean getter + setter

}

