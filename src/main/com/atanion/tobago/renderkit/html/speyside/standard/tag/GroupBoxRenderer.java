/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.GroupBoxRendererBase;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class GroupBoxRenderer extends GroupBoxRendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static Log LOG = LogFactory.getLog(GroupBoxRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeDirectEnd(FacesContext facesContext,
                              UIComponent component) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("div", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);


    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-groupbox-header", null);
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    writer.startElement("span", null);
    writer.writeAttribute("class", "tobago-groupbox-header-label", null);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);
    if (label != null) {
      RenderUtil.encode(facesContext, label);
    } else if (labelString != null) {
      writer.writeText(labelString, null);
    }
    writer.endElement("span");
    UIPanel toolbar = (UIPanel) component.getFacet("toolbar");
    if (toolbar != null) {
      writer.startElement("span", null);
      writer.writeAttribute("class", "tobago-groupbox-header-toolbar", null);
      toolbar.getAttributes().put(
          TobagoConstants.ATTR_SUPPPRESS_TOOLBAR_CONTAINER, Boolean.TRUE);
      RenderUtil.encode(facesContext, toolbar);
      writer.endElement("span");
    }

    writer.endElement("div");

    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-groupbox-content", null);
//    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_BODY);
    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-groupbox-content-inner", null);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_INNER);
    RenderUtil.encodeChildren(facesContext, (UIPanel) component);
    writer.endElement("div");
    writer.endElement("div");

    writer.endElement("div");
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }



// ///////////////////////////////////////////// bean getter + setter

}

