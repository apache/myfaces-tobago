/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 07.02.2003 16:00:00.
 * $Id$
 */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPage;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.component.UIGraphic;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageRenderer extends RendererBase {
  
// ------------------------------------------------------------------ constants

  private static final Log LOG = LogFactory.getLog(ImageRenderer.class);

// ----------------------------------------------------------------- interfaces


// ---------------------------- interface TobagoRenderer

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
    UIGraphic graphic = (UIGraphic) component;
    final String value = graphic.getUrl();
    String src = value;
    if (src != null) {
      final String ucSrc = src.toUpperCase();
      if (ucSrc.startsWith("HTTP:") || ucSrc.startsWith("FTP:")
          || ucSrc.startsWith("/")) {
        // absolute Path to image : nothing to do
      } else {
        src = null;
        if (isDisabled(graphic)) {
          src = ResourceManagerUtil.getImage(
              facesContext, createSrc(value, "Disabled"), true);
        }
        if (src == null) {
          src = ResourceManagerUtil.getImage(facesContext, value);
        }
        addImageSources(facesContext, graphic);
      }
    }

    String border = (String) graphic.getAttributes().get(ATTR_BORDER);
    if (border == null) {
      border = "0";
    }
    String alt = (String) graphic.getAttributes().get(ATTR_ALT);
    if (alt == null) {
      alt = "";
    }
    String tip = (String) graphic.getAttributes().get(ATTR_TIP);

    ResponseWriter writer = facesContext.getResponseWriter();

    writer.startElement("img", graphic);
    final String clientId = graphic.getClientId(facesContext);
    writer.writeAttribute("id", clientId, null);
    if (ComponentUtil.isHoverEnabled(graphic) && !isDisabled(graphic)) {
      writer.writeAttribute("onmouseover",
          "tobagoImageMouseover('" + clientId + "')", null);
      writer.writeAttribute("onmouseout",
          "tobagoImageMouseout('" + clientId + "')", null);
    }
    if (src != null) {
      writer.writeAttribute("src", src, null);
    }
    writer.writeAttribute("alt", alt, null);
    if (tip != null) {
      writer.writeAttribute("title", tip, null);
    }
    writer.writeAttribute("border", border, null);
    writer.writeAttribute("height", null, ATTR_HEIGHT);
    writer.writeAttribute("style", null, ATTR_STYLE);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.endElement("img");
  }

// ----------------------------------------------------------- business methods

  public static void addImageSources(
      FacesContext facesContext, UIGraphic graphic) {
    addImageSources(facesContext, ComponentUtil.findPage(graphic),
        graphic.getUrl(), graphic.getClientId(facesContext));
  }
  public static void addImageSources(
      FacesContext facesContext, UIPage page, String src, String id) {
    page.getOnloadScripts().add("addImageSources('" + id + "','"
        + ResourceManagerUtil.getImage(facesContext, src, false) + "','"
        + ResourceManagerUtil.getImage(facesContext, createSrc(src, "Disabled"),
            true) + "','"
        + ResourceManagerUtil.getImage(facesContext, createSrc(src, "Hover"),
            true) + "');");
  }

  public static String createSrc(String src, String ext) {
    int dot = src.lastIndexOf('.');
    if (dot == -1) {
      LOG.warn("Image src without extension: '" + src + "'");
      return src;
    } else {
      return src.substring(0, dot) + ext + src.substring(dot);
    }
  }

  private boolean isDisabled(UIGraphic graphic) {
    boolean disabled = ComponentUtil.getBooleanAttribute(graphic,
        ATTR_DISABLED);
    if (!disabled && graphic.getParent() instanceof UICommand) {
      disabled =
          ComponentUtil.getBooleanAttribute(graphic.getParent(), ATTR_DISABLED);
    }
    return disabled;
  }
}

