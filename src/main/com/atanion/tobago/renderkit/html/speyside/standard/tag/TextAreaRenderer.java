/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.webapp.TobagoResponseWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class TextAreaRenderer extends InputRendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(TextAreaRenderer.class);

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

    return space;
  }

  public int getLabelWidth() {
    return 120;
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return 0;
  }

//  public int getComponentExtraHeight(FacesContext facesContext, UIComponent component) {
//    return 2; // fixme: label is to hight for layout,
//  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    TobagoResponseWriter writer = (TobagoResponseWriter)
        facesContext.getResponseWriter();

    String image = TobagoResource.getImage(facesContext, "1x1.gif");
    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);

    writer.startElement("table", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("summary", "", null);
    writer.startElement("tr", null);
    if (label != null) {
      String height = evaluateHeight(component);
      writer.startElement("td", null);
      writer.writeAttribute("class", "tobago-label-td", null);
      writer.writeAttribute("style", height, null);
      writer.writeText("", null); // to ensure that the start-tag is closed!
      RenderUtil.encode(facesContext, label);
      writer.endElement("td");
      writer.startElement("td", null);
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("heigth", "1", null);
      writer.writeAttribute("width", "5", null);
      writer.endElement("img");
      writer.endElement("td");
    }
    writer.startElement("td", null);
    writer.writeAttribute("valign", "top", null);
    writer.writeAttribute("rowspan", "2", null);
    writer.writeText("", null); // to ensure that the start-tag is closed!
    com.atanion.tobago.renderkit.html.scarborough.standard.tag.TextAreaRenderer
        .renderMain(facesContext, (UIInput) component, writer);
    writer.endElement("td");
    writer.endElement("tr");
    writer.startElement("tr", null);
    if (label != null) {
      writer.startElement("td", null);
      writer.writeAttribute("class", "tobago-label-td-underline-label", null);
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("heigth", "1", null);
      writer.endElement("img");
      writer.endElement("td");
      writer.startElement("td", null);
      writer.writeAttribute("class", "tobago-label-td-underline-spacer", null);
      writer.startElement("img", null);
      writer.writeAttribute("src", image, null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("heigth", "1", null);
      writer.endElement("img");
      writer.endElement("td");
    }
    writer.endElement("tr");
    writer.endElement("table");
  }

  private String evaluateHeight(UIComponent component) {
    String height;
    height = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE);
    LOG.debug("height = " + height);
    if (height != null && height.matches(".*height\\s*?:\\s*?\\d+px;.*")) {
      LOG.debug("height matches");
      String[] styles = height.split(";");
      for (int i = 0; i < styles.length; i++) {
        String style = styles[i];
        LOG.debug("style = " + style);
        if (style.trim().startsWith("height") ) {
          height = "height: " + (Integer.parseInt(style.replaceAll("\\D", "")) -1) + "px;";
          break;
        }
      }
    } else {
      LOG.debug("height TO null");
      height = null;
    }
    LOG.debug("height = " + height);
    return height;
  }


// ///////////////////////////////////////////// bean getter + setter

}

