/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * : $
 */
package org.apache.myfaces.tobago.renderkit.html.scarborough.opera.tag;

import org.apache.myfaces.tobago.TobagoConstants;
import org.apache.myfaces.tobago.webapp.TobagoResponseWriter;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.context.UserAgent;
import org.apache.myfaces.tobago.renderkit.html.HtmlRendererUtil;
import org.apache.myfaces.tobago.renderkit.RenderUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class BoxRenderer extends org.apache.myfaces.tobago.renderkit.html.scarborough.standard.tag.BoxRenderer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void encodeBeginTobago(FacesContext facesContext,
      UIComponent component) throws IOException {

    HtmlRendererUtil.prepareInnerStyle(component);

    UIComponent label = component.getFacet(TobagoConstants.FACET_LABEL);
    String labelString
        = (String) component.getAttributes().get(TobagoConstants.ATTR_LABEL);

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();

    writer.startElement("fieldset", component);
    writer.writeComponentClass();
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);

    if (label != null || labelString != null) {
      writer.startElement("legend", component);
      writer.writeComponentClass();

      writer.startElement("b", null);
      writer.writeText("", null);
      if (label != null) {
        RenderUtil.encode(facesContext, label);
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
    writer.writeComponentClass();
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE_INNER);
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return 4;
  }

// ///////////////////////////////////////////// bean getter + setter

}

