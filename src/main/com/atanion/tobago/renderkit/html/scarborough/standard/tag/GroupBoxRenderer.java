/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

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


    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("fieldset", component);
    writer.writeAttribute("style", null, getAttrStyleKey());
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);

    if (label != null || labelString != null) {
      writer.startElement("legend", component);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);

      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }

      writer.endElement("legend");

    }
    writer.startElement("div", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_INNER);

    writer.writeText("", null);
    RenderUtil.encodePanel(facesContext, (UIPanel) component);

    writer.endElement("div");
    writer.endElement("fieldset");
  }

  public boolean getRendersChildren() {
    return true;
  }

  protected String getAttrStyleKey() {
    return TobagoConstants.ATTR_STYLE;
  }

  public void encodeDirectChildren(FacesContext facesContext,
      UIComponent component) throws IOException {
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

// ///////////////////////////////////////////// bean getter + setter

}

