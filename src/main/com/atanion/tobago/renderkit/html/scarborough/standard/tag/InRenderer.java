/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.renderkit.InRendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class InRenderer extends InRendererBase{
// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component)
      throws IOException {
    super.encodeEndTobago(facesContext, component);
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    UIComponent picker = component.getFacet("picker");
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    if (label != null || picker != null) {
      writer.startElement("table", null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.startElement("tr", null);
      writer.startElement("td", null);
      writer.writeText("", null);
    }
    if (label != null) {
      HtmlRendererUtil.encodeHtml(facesContext, label);

      writer.endElement("td");
      writer.startElement("td", null);
    }

    renderMain(facesContext, (UIInput) component, writer);


    if (picker != null) {
      writer.endElement("td");
      writer.startElement("td", null);
      writer.writeAttribute("style", "padding-left: 5px;", null);
      renderPicker(facesContext, component, picker);
    }

    if (label != null || picker != null) {
      writer.endElement("td");
      writer.endElement("tr");
      writer.endElement("table");
    }
    HtmlRendererUtil.renderFocusId(facesContext, component);
  }

// ----------------------------------------------------------- business methods

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;
    if (component.getFacet(TobagoConstants.FACET_LABEL) != null
      || component.getAttributes().get(TobagoConstants.ATTR_LABEL) != null) {
      int labelWidth = LayoutUtil.getLabelWidth(component);
      space += labelWidth != 0 ? labelWidth : getLabelWidth(facesContext, component);
    }
    if (component.getFacet("picker") != null) {
      int pickerWidth = getConfiguredValue(facesContext, component, "pickerWidth");

      space += pickerWidth;
    }
    return space;
  }
}

