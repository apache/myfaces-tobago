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
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;

public class RichTextEditorRenderer extends InputRendererBase
 implements DirectRenderer, HeightLayoutRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(RichTextEditorRenderer.class);

  public static final String CHANGE_BUTTON = "togleState";

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }


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

    String clientId = component.getClientId(facesContext);

    ResponseWriter writer = facesContext.getResponseWriter();

    String classes
        = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_CLASS);

    writer.startElement("div", component);
    writer.writeAttribute("class", classes + " tobago-richtexteditor-container", null);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    // class, stly.width, style.height

    writer.startElement("div", component);
    writer.writeAttribute("class", classes + " tobago-richtexteditor-header-div", null);
    createToolbarButton("Editor", previewState, writer, component, facesContext, clientId);
    createToolbarButton("Preview", !previewState, writer, component, facesContext, clientId);
    writer.endElement("div");

    String content = getCurrentValue(facesContext, component);

    if (previewState) {
      writer.startElement("input", component);
      writer.writeAttribute("type", "hidden", null);
      writer.writeAttribute("name", clientId, null);
      writer.writeAttribute("value", content, null);
      writer.endElement("input");

      writer.startElement("div", component);
      writer.writeAttribute("class", classes + " tobago-richtexteditor-body", null);
      writer.writeAttribute("id", clientId, null);

      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_BODY);

      writer.write(RichTextEditorRenderer.contentToHtml(content));

      writer.endElement("div");
    }
    else {
      writer.startElement("textarea", component);
      writer.writeAttribute("class", classes + " tobago-richtexteditor-body", null);
      writer.writeAttribute("name", clientId, null);
      writer.writeAttribute("id", clientId, null);
      writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_BODY);
      String onchange = HtmlUtils.generateOnchange(component, facesContext);
      if (null != onchange) {
        writer.writeAttribute("onchange", onchange, null);
      }

      writer.writeText(content, null);

      writer.endElement("textarea");
    }
    writer.endElement("div");
  }

  private void createToolbarButton(String label, boolean enabled,
      ResponseWriter writer, UIInput component,
      FacesContext facesContext, String clientId)
      throws IOException {
    String onClick = "submitAction('"
        + ComponentUtil.findPage(component).getFormId(facesContext) + "', '"
        + clientId + RichTextEditorRenderer.CHANGE_BUTTON + "')";
    writer.startElement("span", component);
    String buttonStyle = "tobago-richtexteditor-toolbar-button-span"
        + (enabled ? "-enabled" : "-disabled");
    writer.writeAttribute("class", buttonStyle, null);
    writer.writeAttribute("onclick", onClick, null);
    writer.writeAttribute("unselectable", "on", null);

    Application application = facesContext.getApplication();
    UIComponent image = application.createComponent(UIGraphic.COMPONENT_TYPE);
    image.getAttributes().put(TobagoConstants.ATTR_VALUE,
        "tobago-richtext-mode.gif");
    image.getAttributes().put(TobagoConstants.ATTR_I18N, Boolean.TRUE);
    image.getAttributes().put(TobagoConstants.ATTR_SUPPRESSED, Boolean.TRUE);
    RenderUtil.encode(facesContext, image);
    writer.writeText(label, null);
    writer.endElement("span");
  }
// ///////////////////////////////////////////// bean getter + setter

}
