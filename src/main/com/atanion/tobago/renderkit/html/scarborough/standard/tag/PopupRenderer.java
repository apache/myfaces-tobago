/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPopup;
import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.context.ResourceManagerUtil;
import com.atanion.tobago.context.ClientProperties;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PopupRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(PopupRenderer.class);

  public static final String CONTENT_ID_POSTFIX = SUBCOMPONENT_SEP + "content";

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeBeginTobago(
      FacesContext facesContext, UIComponent uiComponent) throws IOException {

    TobagoResponseWriter writer = (TobagoResponseWriter) facesContext.getResponseWriter();
    UIPopup component = (UIPopup) uiComponent ;
    final String clientId = component.getClientId(facesContext);
    final String contentDivId = clientId + CONTENT_ID_POSTFIX;
    final String left = component.getLeft();
    final String top = component.getTop();

    final StringBuffer contentStyle = new StringBuffer();
    if (component.getWidth() != null) {
      contentStyle.append("width: " );
      contentStyle.append(component.getWidth());
      contentStyle.append("; ");
    }
    if (component.getHeight() != null) {
      contentStyle.append("height: " );
      contentStyle.append(component.getHeight());
      contentStyle.append("; ");
    }
    contentStyle.append("left: " );
    contentStyle.append(left != null ? left : "100");
    contentStyle.append("; ");
    contentStyle.append("top: " );
    contentStyle.append(top != null ? top : "50");
    contentStyle.append("; ");

    writer.startElement("img", component);
    writer.writeIdAttribute(clientId);
    writer.writeComponentClass( ATTR_STYLE_CLASS);
    writer.writeAttribute("onclick", "tobagoPopupBlink('" + clientId + "')", null);
    writer.writeAttribute("src", ResourceManagerUtil.getImage(facesContext, "image/1x1.gif"), null);
    writer.writeAttribute("galleryimg", "no", null);
    writer.endElement("img");
    if (ClientProperties.getInstance(facesContext).getUserAgent().isMsie()) {
      writer.startElement("iframe", component);
      writer.writeIdAttribute(clientId + SUBCOMPONENT_SEP + "iframe");
      writer.writeClassAttribute("tobago-popup-iframe");
      writer.writeAttribute("style", contentStyle.toString(), null);
      writer.endElement("iframe");
    }
    writer.startElement("div", component);
    writer.writeIdAttribute(contentDivId);
    writer.writeClassAttribute("tobago-popup-content");


    writer.writeAttribute("style", contentStyle.toString(), null);
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    UIPopup component = (UIPopup) uiComponent ;
    final String clientId = component.getClientId(facesContext);

    writer.endElement("div");

    String setupScript = "tobagoSetupPopup('" + clientId + "', '"
        + component.getLeft() + "', '" + component.getTop() + "');";
    HtmlRendererUtil.writeJavascript(writer, setupScript);

    if (ComponentUtil.getBooleanAttribute(component, ATTR_POPUP_RESET)) {
      component.setRendered(false);
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

