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
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PopupRenderer extends RendererBase {

  private static final Log LOG = LogFactory.getLog(PopupRenderer.class);
  public boolean getRendersChildren() {
    return true;
  }

  public void encodeChildren(FacesContext facesContext, UIComponent component)
      throws IOException {
  }

  public void encodeChildrenTobago(FacesContext facesContext,
      UIComponent component) throws IOException {
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {
    UIPopup component = (UIPopup) uiComponent ;
    ResponseWriter writer = facesContext.getResponseWriter();
    writer.startElement("div", component);
    writer.writeAttribute("id", component.getClientId(facesContext), null);
    writer.writeAttribute("class", null, ATTR_STYLE_CLASS);
    writer.writeAttribute("onclick", "tobagoPopupBlink('" + component.getClientId(facesContext) + "')", null);
    writer.endElement("div");
    writer.startElement("div", component);
    writer.writeAttribute("class", "tobago-popup-content", null);
    writer.writeAttribute("style", "width: " + component.getWidth() + "; \n" +
        "            height: " + component.getHeight() + "; \n" +
        "            left: " + component.getLeft() + "; \n" +
        "            top: " + component.getTop() + ";", null);
    RenderUtil.encodeChildren(facesContext, component);
    writer.endElement("div");
  }

// ///////////////////////////////////////////// bean getter + setter

}

