/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.util.LayoutUtil;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class TabGroupRenderer extends
    com.atanion.tobago.renderkit.html.scarborough.standard.tag.TabGroupRenderer{

  protected void encodeContent(ResponseWriter writer, FacesContext facesContext, UIPanel activeTab) throws IOException {

    String bodyStyle = (String)
        activeTab.getParent().getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
    writer.startElement("tr", null);
    writer.startElement("td", null);
    if (bodyStyle != null) {
      writer.writeAttribute("style", bodyStyle, null);
    }

    writer.startElement("div", null);
    writer.writeAttribute("class","tobago-tab-shadow", null);
    if (bodyStyle != null) {
      writer.writeAttribute("style", bodyStyle, null);
    }


    writer.startElement("div", null);
    writer.writeAttribute("class", "tobago-tab-content", null);

    String height = LayoutUtil.getStyleAttributeValue(bodyStyle, "height");
    if (height != null) {
      writer.writeAttribute("style", "height: "
          + (Integer.parseInt(height.replaceAll("\\D", ""))-1) + "px; overflow: auto;", null);
    }

    writer.writeText("", null);
    RenderUtil.encodeChildren(facesContext, activeTab);

    writer.endElement("div");
    writer.endElement("div");

    writer.endElement("td");
    writer.endElement("tr");

  }

}

