/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPopup;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class PopupRenderer extends RendererBase {

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

    writer.writeAttribute("style", "z-index: 1;\n" +
        "            border: 1px solid blue; \n" +
        "            background: rgb(255, 255, 255) none repeat scroll 0%;\n" +
        "            position: absolute; \n" +
        "            width: " + component.getWidth() + "; \n" +
        "            height: " + component.getHeight() + "; \n" +
        "            -moz-background-clip: initial; \n" +
        "            -moz-background-origin: initial; \n" +
        "            -moz-background-inline-policy: initial; \n" +
        "            left: 191px; \n" +
        "            top: 200px;", null);
    RenderUtil.encodeChildren(facesContext, component);
    writer.endElement("div");
  }

// ///////////////////////////////////////////// bean getter + setter

}

