/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.context.Theme;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.GroupBoxRendererBase;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.HtmlUtils;
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

    String image = TobagoResource.getImage(facesContext, "1x1.gif");

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("table", component);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_HEADER);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("summary", "", null);

    writer.startElement("tr", null);
    writer.startElement("td", null);
    String classLeft = HtmlUtils.appendAttribute(
        component, TobagoConstants.ATTR_STYLE_CLASS, "tobago-groupbox-head-td-left");
    writer.writeAttribute("class", classLeft, null);

    writer.writeText("", null);
    if (label != null || labelString != null) {
      if (label != null) {
        RenderUtil.encode(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }
    } else {
      writer.startElement("img", null);
      writer.writeAttribute("border", "0", null);
      writer.writeAttribute("src", image, null);
      writer.endElement("img");
    }

    writer.endElement("td");
    writer.startElement("td", null);
    String classRight = HtmlUtils.appendAttribute(
        component, TobagoConstants.ATTR_STYLE_CLASS, "tobago-groupbox-head-td-right");
    writer.writeAttribute("class", classRight, null);

    writer.startElement("img", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("src", image, null);
    writer.endElement("img");

    writer.endElement("td");
    writer.endElement("tr");
    writer.endElement("table");

    writer.startElement("table", component);
    writer.writeAttribute("class", "tobago-groupbox-body-td-border", null);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_BODY);
    writer.writeAttribute("cellpadding", "0", null);
    writer.writeAttribute("cellspacing", "0", null);
    writer.writeAttribute("border", "0", null);
    writer.writeAttribute("summary", "", null);

    writer.startElement("tr", null);
    writer.startElement("td", null);
        String classBody = HtmlUtils.appendAttribute(
        component, TobagoConstants.ATTR_STYLE_CLASS, "tobago-groupbox-body-td");
    writer.writeAttribute("class", classBody, null);
    writer.writeAttribute("valign", "top", null);

    writer.startElement("div", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_INNER);

    writer.writeText("", null);
    RenderUtil.encodePanel(facesContext, (UIPanel) component);

    writer.endElement("div");
    writer.endElement("td");
    writer.endElement("tr");
    writer.endElement("table");
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }



// ///////////////////////////////////////////// bean getter + setter

}

