/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.TextBoxRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TextBoxRenderer extends TextBoxRendererBase
    implements DirectRenderer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
    int space = 0;

    if (component.getFacet(TobagoConstants.FACET_LABEL) != null) {
      int labelWidht = LayoutUtil.getLabelWidth(component);
      space += labelWidht != 0 ? labelWidht : getLabelWidth();
//      space += 10; // padding (5px) + space (5px)
      space += 5; // space (5px)
    }
    if (component.getFacet("picker") != null) {
      int pickerWidth = 20; // todo: configurable
      space += pickerWidth;
    }

    return space;
  }

  public int getLabelWidth() {
    return 120;
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    UIInput input = (UIInput) component;
    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    boolean isInline = ComponentUtil.isInline(input); // fixme
    String image = TobagoResource.getImage(facesContext, "1x1.gif");
    UIComponent label = input.getFacet(TobagoConstants.FACET_LABEL);

    if (!isInline) {
      writer.startElement("table", null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("cellspacing", "0", null);
      writer.writeAttribute("cellpadding", "0", null);
      writer.writeAttribute("summary", "", null);
      writer.startElement("tr", null);
      if (label != null) {
        writer.startElement("td", null);
        writer.writeAttribute("class", "tobago-label-td", null);
        writer.writeAttribute("valign", "top", null);
        writer.writeText("", null); // to ensure that the start-tag is closed!
        RenderUtil.encode(facesContext, label);
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
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

