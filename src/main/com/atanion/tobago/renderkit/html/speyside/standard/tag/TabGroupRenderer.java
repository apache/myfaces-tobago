/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.speyside.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.component.UITabGroup;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.StyleAttribute;
import com.atanion.tobago.renderkit.html.HtmlDefaultLayoutManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletRequest;
import java.io.IOException;

public class TabGroupRenderer extends
    com.atanion.tobago.renderkit.html.scarborough.standard.tag.TabGroupRenderer{

  protected void encodeContent(ResponseWriter writer, FacesContext facesContext, UIPanel activeTab) throws IOException {

    String bodyStyle = (String)
        activeTab.getParent().getAttributes().get(TobagoConstants.ATTR_STYLE_BODY);
    writer.startElement("tr", null);
    writer.startElement("td", null);
    writer.writeAttribute("style", bodyStyle, null);

    writer.startElement("div", null);
    writer.writeAttribute("class","tobago-tab-shadow", null);
    writer.writeAttribute("style", bodyStyle, null);


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

