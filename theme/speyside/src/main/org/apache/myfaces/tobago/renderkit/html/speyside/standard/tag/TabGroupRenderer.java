/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit.html.speyside.standard.tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.renderkit.RenderUtil;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class TabGroupRenderer extends
    org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.TabGroupRenderer{

  private static final Log LOG = LogFactory.getLog(TabGroupRenderer.class);

  protected void encodeContent(TobagoResponseWriter writer, FacesContext facesContext, UIPanel activeTab) throws IOException {

    String bodyStyle = (String)
        activeTab.getParent().getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
    writer.startElement("tr", null);
    writer.startElement("td", null);
    if (bodyStyle != null) {
      writer.writeAttribute("style", bodyStyle, null);
    }

    writer.startElement("div", null);
    writer.writeClassAttribute("tobago-tab-shadow");
    if (bodyStyle != null) {
      writer.writeAttribute("style", bodyStyle, null);
    }


    writer.startElement("div", null);
    writer.writeClassAttribute("tobago-tab-content");

    String height = HtmlRendererUtil.getStyleAttributeValue(bodyStyle, "height");
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

