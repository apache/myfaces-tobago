/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.html.InRendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class InRenderer extends InRendererBase {
// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIInput input = (UIInput) component;
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    boolean inline = ComponentUtil.getBooleanAttribute(component, ATTR_INLINE);
    String image = ResourceManagerUtil.getImage(facesContext, "image/1x1.gif");
    UIComponent label = input.getFacet(FACET_LABEL);
    UIComponent picker = input.getFacet("picker");

    if (!inline) {
      writer.startElement("table", input);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.writeAttribute("title", null, ATTR_TIP);
      writer.startElement("tr", null);
      if (label != null) {
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td", null);
        writer.writeAttribute("valign", "top", null);
        writer.writeText("", null); // to ensure that the start-tag is closed!
        HtmlRendererUtil.encodeHtml(facesContext, label);
        writer.endElement("td");
        writer.startElement("td", null);
        writer.startElement("img", null);
        writer.writeAttribute("src", image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.writeAttribute("width", "5", null);
        writer.endElement("img");
        writer.endElement("td");
      }
      writer.startElement("td", null);
      writer.writeAttribute("valign", "top", null);
      writer.writeAttribute("rowspan", "2", null);
      writer.writeText("", null); // to ensure that the start-tag is closed!
      renderMain(facesContext, input, writer);
      if (picker != null) {
        writer.endElement("td");
        writer.startElement("td", null);
        writer.writeAttribute("valign", "top", null);
        writer.writeAttribute("rowspan", "2", null);
        writer.writeAttribute("style", "padding-left: 5px;", null);

        renderPicker(facesContext, input, picker);
      }
      writer.endElement("td");
      writer.endElement("tr");
      writer.startElement("tr", null);
      if (label != null) {
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td-underline-label", null);
        writer.startElement("img", null);
        writer.writeAttribute("src", image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.endElement("img");
        writer.endElement("td");
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td-underline-spacer", null);
        writer.startElement("img", null);
        writer.writeAttribute("src", image, null);
        writer.writeAttribute("border", "0", null);
        writer.writeAttribute("height", "1", null);
        writer.endElement("img");
        writer.endElement("td");
      }
      writer.endElement("tr");
      writer.endElement("table");
    } else {
      renderMain(facesContext, input, writer);
      renderPicker(facesContext, input, picker);
    }
    HtmlRendererUtil.renderFocusId(facesContext, component);
  }

// ----------------------------------------------------------- business methods

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0 ? labelWidht : getLabelWidth(facesContext, component);
      space += getConfiguredValue(facesContext, component, "labelSpace");
    }
    if (component.getFacet("picker") != null) {
      int pickerWidth = getConfiguredValue(facesContext, component, "pickerWidth");
      space += pickerWidth;
    }

    return space;
  }
}

