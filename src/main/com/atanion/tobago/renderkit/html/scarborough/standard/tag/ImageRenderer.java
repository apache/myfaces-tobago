/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ImageRenderer extends RendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    UIGraphic graphic = (UIGraphic) component;
    String src = graphic.getValue().toString();
    if (ComponentUtil.getBooleanAttribute(graphic, TobagoConstants.ATTR_I18N)) {
      src = TobagoResource.getImage(facesContext, src);
    } else {
      if (src.toUpperCase().startsWith("HTTP:")
          || src.toUpperCase().startsWith("FTP:")) {
        // absolute Path to image : nothing to do
      } else {
        // relative path : add contextPath
        if (!src.startsWith("/")) {
          src = "/" + src;
        }
        src = facesContext.getExternalContext().getRequestContextPath() + src;
      }
    }

    String border = (String) graphic.getAttributes().get(
        TobagoConstants.ATTR_BORDER);
    if (border == null) {
      border = "0";
    }
    String alt = (String) graphic.getAttributes().get(TobagoConstants.ATTR_ALT);
    if (alt == null) {
      alt = "";
    }
    String title
        = (String) graphic.getAttributes().get(TobagoConstants.ATTR_TITLE);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("img", graphic);
    writer.writeAttribute("src", src, null);
    writer.writeAttribute("alt", alt, null);
    if (title != null) {
      writer.writeAttribute("title", title, null);
    }
    writer.writeAttribute("border", border, null);
    writer.writeAttribute("height", null, TobagoConstants.ATTR_HEIGHT);
    writer.writeAttribute("style", null, TobagoConstants.ATTR_STYLE);
    writer.writeAttribute("class", null, TobagoConstants.ATTR_STYLE_CLASS);
    writer.endElement("img");

  }

// ///////////////////////////////////////////// bean getter + setter

}

