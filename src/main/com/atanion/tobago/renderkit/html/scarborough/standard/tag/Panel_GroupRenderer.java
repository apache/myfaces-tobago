/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.renderkit.DirectRenderer;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

public class Panel_GroupRenderer extends RendererBase
    implements HeightLayoutRenderer, DirectRenderer {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(Panel_GroupRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "headerHeight");
  }

  public void encodeDirectChildren(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel component = (UIPanel) uiComponent ;
    for (Iterator i = component.getChildren().iterator(); i.hasNext(); ) {
      UIComponent child = (UIComponent) i.next();
      RenderUtil.encode(facesContext, child);
    }
  }

  public void encodeDirectEnd(FacesContext facesContext,
      UIComponent uiComponent) throws IOException {

    UIPanel component = (UIPanel) uiComponent ;
    BodyContentHandler bodyContentHandler = (BodyContentHandler)
        component.getAttributes().get(TobagoConstants.ATTR_BODY_CONTENT);

    if (bodyContentHandler != null) {
      ResponseWriter writer = facesContext.getResponseWriter();
      writer.write(bodyContentHandler.getBodyContent());
    }
  }

// ///////////////////////////////////////////// bean getter + setter

}

