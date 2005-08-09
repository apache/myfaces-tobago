/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.wml.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class PanelRenderer extends RendererBase {

  public boolean getRendersChildren() {
    return true;
  }

  public void encodeChildrenTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel component = (UIPanel) uiComponent ;
    for (Iterator i = component.getChildren().iterator(); i.hasNext(); ) {
      UIComponent child = (UIComponent) i.next();
      RenderUtil.encode(facesContext, child);
    }
  }

  public void encodeEndTobago(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel component = (UIPanel) uiComponent ;
    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);

    if (bodyContentHandler != null) {
      ResponseWriter writer = facesContext.getResponseWriter();
      writer.write(bodyContentHandler.getBodyContent());
    }
  }

}

