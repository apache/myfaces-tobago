/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id: $
 */
package com.atanion.tobago.renderkit.html.scarborough.mozilla_4_7.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.renderkit.BoxRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BoxRenderer extends BoxRendererBase {


  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent component) throws IOException {


    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);


    ResponseWriter writer = facesContext.getResponseWriter();

    // todo: move fix style attributes to style.css (border, padding, align, etc)

    writer.startElement("table", component);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    writer.writeAttribute("border", "1", null);
    writer.writeAttribute("cellpadding", "5", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("summary", "", null);

    if (label != null || labelString != null) {

      writer.startElement("tr", null);
      writer.startElement("th", null);
      writer.writeAttribute("align", "left", null);
      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }
      writer.endElement("th");
      writer.endElement("tr");
    }

    writer.startElement("tr", null);
    writer.startElement("td", null);

  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.endElement("td");
    writer.endElement("tr");
    writer.endElement("table");
  }

  public void encodeChildrenTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public int getPaddingHeight(FacesContext facesContext, UIComponent component) {
    return 10
        + (component.getFacet(TobagoConstants.FACET_LABEL) != null ? 25 : 0);
  }

// ///////////////////////////////////////////// bean getter + setter

}

