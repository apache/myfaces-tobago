/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.TobagoResource;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ImageRenderer extends RendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(ImageRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent component) throws IOException {

    UIGraphic graphic = (UIGraphic) component;
    final String value = graphic.getValue().toString();
    String src = value;
    if (ComponentUtil.getBooleanAttribute(graphic, TobagoConstants.ATTR_I18N)) {
      src = null;
      if (isDisabled(graphic)) {
        src = TobagoResource.getImage(facesContext, createSrc(value, "-disabled"), true);
      }
      if (src == null) {
        src = TobagoResource.getImage(facesContext, value);
      }
      addImageSources(facesContext, graphic);
    } else {
      final String ucSrc = src.toUpperCase();
      if (ucSrc.startsWith("HTTP:") || ucSrc.startsWith("FTP:")) {
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
    final String clientId = graphic.getClientId(facesContext);
    writer.writeAttribute("id", clientId, null);
    if (ComponentUtil.isHoverEnabled(graphic) && ! isDisabled(graphic)) {
      writer.writeAttribute("onmouseover", "tobagoImageMouseover('" + clientId + "')", null);
      writer.writeAttribute("onmouseout", "tobagoImageMouseout('" + clientId + "')", null);
    }
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

  private boolean isDisabled(UIGraphic graphic) {
    boolean disabled = ComponentUtil.isDisabled(graphic);
    if (! disabled && graphic.getParent() instanceof UICommand) {
      disabled = ComponentUtil.isDisabled(graphic.getParent());
    }
    return disabled;
  }

  public String createSrc(String src, String ext) {
    int dot = src.lastIndexOf('.');
    return src.substring(0, dot) + ext + src.substring(dot);
  }


  public void addImageSources(FacesContext facesContext, UIGraphic graphic) {
    final String src = graphic.getValue().toString();
    final UIPage page = ComponentUtil.findPage(graphic);
    page.getOnloadScripts().add("addImageSources('"
        + graphic.getClientId(facesContext) + "','"
        + TobagoResource.getImage(facesContext, src, false) + "','"
        + TobagoResource.getImage(
            facesContext, createSrc(src, "-disabled"), true) + "','"
        + TobagoResource.getImage(
            facesContext, createSrc(src, "-hover"), true) + "')");
  }
// ///////////////////////////////////////////// bean getter + setter

}

