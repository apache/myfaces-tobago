/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPopup;
import com.atanion.tobago.component.UIPage;

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

    ResponseWriter writer = facesContext.getResponseWriter();
    UIPopup component = (UIPopup) uiComponent ;
    final String clientId = component.getClientId(facesContext);
    final String contentDivId = clientId + CONTENT_ID_POSTFIX;
    final String left = component.getLeft();
    final String top = component.getTop();
    writer.startElement("div", component);
    writer.writeAttribute("id", clientId, null);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.writeAttribute("onclick", "tobagoPopupBlink('" + clientId + "')", null);
    writer.endElement("div");
    writer.startElement("div", component);
    writer.writeAttribute("id", contentDivId, null);
    writer.writeAttribute("class", "tobago-popup-content", null);

    StringBuffer style = new StringBuffer();
    if (component.getWidth() != null) {
      style.append("width: " );
      style.append(component.getWidth());
      style.append("; ");
    }
    if (component.getHeight() != null) {
      style.append("height: " );
      style.append(component.getHeight());
      style.append("; ");
    }
    style.append("left: " );
    style.append(left != null ? left : "100");
    style.append("; ");
    style.append("top: " );
    style.append(top != null ? top : "50");
    style.append("; ");

    writer.writeAttribute("style", style.toString(), null);
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    ResponseWriter writer = facesContext.getResponseWriter();
    UIPopup component = (UIPopup) uiComponent ;
    final String contentDivId
        = component.getClientId(facesContext) + CONTENT_ID_POSTFIX;

    writer.endElement("div");

    String setupScript = "tobagoSetupPopup('" + contentDivId + "', '"
        + component.getLeft() + "', '" + component.getTop() + "');";
    writer.startElement("script", null);
    writer.writeAttribute("type", "text/javascript", null);
    writer.write("\n<!--\n");
    writer.write(setupScript);
    writer.write("\n// -->\n");
    writer.endElement("script");
  }

// ///////////////////////////////////////////// bean getter + setter

}

