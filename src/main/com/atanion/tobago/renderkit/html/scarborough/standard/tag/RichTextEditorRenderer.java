/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import java.io.IOException;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.Application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atanion.lib.richtext.WikiParser;
import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;

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
    // todo: refactor
    Boolean state = (Boolean) component.getAttributes().get(TobagoConstants.ATTR_STATE_PREVIEW);
    // <div>'s border is outside, <textarea>'s border is inside
    return state.booleanValue() ? 2 : 0 ;
  }

  public static String contentToHtml(String content) {
    try {
      return WikiParser.toHtml(content);
    } catch (Exception e) {
      LOG.error("failed to parser wiki markup", e);
    }
    return content;
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIInput component = (UIInput) uiComponent;

    boolean previewState = ((Boolean)
        component.getAttributes().get(TobagoConstants.ATTR_STATE_PREVIEW)).booleanValue();
    // fixme: remove this when i18n is ok
    String state = (previewState) ? "Editor" : "Preview";

    String clientId = component.getClientId(facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-richtext-div", null);
    // class, stly.width, style.height

    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-richtext-toolbar-div", null);
    createToolbarButton(writer, component, facesContext, clientId, state);
    writer.endElement("div");

    String content = ComponentUtil.currentValue(component);

    if (previewState) {
      state = TobagoResource.getProperty(facesContext, "text", "tobago.richtexteditor.edit");

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

      writer.startElement("textarea", component);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
      writer.writeAttribute("name", clientId, null);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
      String onchange = HtmlUtils.generateOnchange(component, facesContext);
      if (null != onchange) {
        writer.writeAttribute("onchange", onchange, null);
      }

      writer.writeText(content, null);

      writer.endElement("textarea");
    }
    writer.endElement("div");
  }

  private void createToolbarButton(ResponseWriter writer, UIInput component,
      FacesContext facesContext, String clientId, String state) throws IOException {
    String onClick = "submitAction('"
        + ComponentUtil.findPage(component).getFormId(facesContext) + "', '"
        + clientId + RichTextEditorRenderer.CHANGE_BUTTON + "')";
    writer.startElement("span", component);
    writer.writeAttribute("class", "tobago-richtext-toolbar-button-span", null);
    writer.writeAttribute("onclick", onClick, null);
    writer.writeAttribute("unselectable", "on", null);

    Application application = facesContext.getApplication();
    UIComponent image = application.createComponent(UIGraphic.COMPONENT_TYPE);
    image.getAttributes().put(TobagoConstants.ATTR_VALUE,
        "tobago-richtext-mode.gif");
    image.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
    image.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    RenderUtil.encode(facesContext, image);
    writer.writeText(state, null);
    writer.endElement("span");
  }
// ///////////////////////////////////////////// bean getter + setter

}
