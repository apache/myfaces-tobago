/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package com.atanion.tobago.renderkit.html.scarborough.opera.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.context.ClientProperties;
import com.atanion.tobago.context.UserAgent;
import com.atanion.tobago.renderkit.BoxRendererBase;
import com.atanion.tobago.renderkit.RenderUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BoxRenderer extends com.atanion.tobago.renderkit.html.scarborough.standard.tag.BoxRenderer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("fieldset", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

    if (label != null || labelString != null) {
      writer.startElement("legend", component);
      writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);

      writer.startElement("b", null);
      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encodeHtml(facesContext, label);
      } else {
        writer.writeText(labelString, null);
      }
      writer.endElement("b");
      writer.endElement("legend");
      if (! ClientProperties.getInstance(facesContext.getViewRoot())
          .getUserAgent().equals(UserAgent.OPERA_7_11)) {
        writer.startElement("br", null);
        writer.endElement("br");
      }
    }
    writer.startElement("div", component);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_INNER);
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return 4;
  }

// ///////////////////////////////////////////// bean getter + setter

}

