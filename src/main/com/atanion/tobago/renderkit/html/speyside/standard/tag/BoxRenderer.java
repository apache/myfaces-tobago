/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.taglib.component.ToolBarTag;
import com.atanion.tobago.renderkit.BoxRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

public class BoxRenderer extends BoxRendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(BoxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    return super.getFixedHeight(facesContext, component);
  }

  public void encodeEndTobago(FacesContext facesContext,
                              UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("div", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);


    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-box-header", null);
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    writer.startElement("span", null);
    writer.writeAttribute("class", "tobago-box-header-label", null);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    } else if (labelString != null) {
      writer.writeText(labelString, null);
    }
    writer.endElement("span");
    UIPanel toolbar = (UIPanel) component.getFacet(FACET_TOOL_BAR);
    if (toolbar != null) {
      final Map attributes = toolbar.getAttributes();
      String className = "tobago-box-header-toolbar-div";
      if (ToolBarTag.LABEL_OFF.equals(attributes.get(ATTR_LABEL_POSITION))) {
        className += " tobago-box-header-toolbar-label_off";
      }
      writer.startElement("div", null);
      writer.writeAttribute("class", className, null);
//      writer.startElement("span", null);
//      writer.writeAttribute("class", "tobago-box-header-toolbar-span", null);
      attributes.put(
          TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER, Boolean.TRUE);
      if (ToolBarTag.LABEL_BOTTOM.equals(attributes.get(ATTR_LABEL_POSITION))) {
        attributes.put(ATTR_LABEL_POSITION, ToolBarTag.LABEL_RIGHT);
      }
      if (ToolBarTag.ICON_BIG.equals(attributes.get(ATTR_ICON_SIZE))) {
        attributes.put(ATTR_ICON_SIZE, ToolBarTag.ICON_SMALL);
      }
      RenderUtil.encode(facesContext, toolbar);
//      writer.endElement("span");
      writer.endElement("div");
    }

    writer.endElement("div");

    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-box-content", null);
//    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_BODY);
    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-box-content-inner", null);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_INNER);
    RenderUtil.encodeChildren(facesContext, (UIPanel) component);
    writer.endElement("div");
    writer.endElement("div");

    writer.endElement("div");
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

}

