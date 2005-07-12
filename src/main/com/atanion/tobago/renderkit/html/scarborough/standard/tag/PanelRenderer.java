/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 28.04.2003 at 15:29:36.
  * $Id$
  */
package com.atanion.tobago.renderkit.html.scarborough.standard.tag;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.BodyContentHandler;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.renderkit.RendererBase;
import com.atanion.tobago.renderkit.RenderUtil;
import com.atanion.tobago.renderkit.html.HtmlRendererUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

public class PanelRenderer extends RendererBase {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(PanelRenderer.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public boolean getRendersChildren() {
    return true;
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    // wenn hoehe gesetzt dann diese,
    // sonst wenn layout vorhanden dieses fragen:
    //       -> aus rowLayout berechnen
    // sonst Warnung ausgebenn und addition der children's fixedHeight

    int height =
        ComponentUtil.getIntAttribute(component, TobagoConstants.ATTR_HEIGHT, -1);

    if (height == -1) {
      height = getFixedHeightForPanel(component, facesContext);
    }
    return height;
  }

  public static int getFixedHeightForPanel(UIComponent component, FacesContext facesContext) {
    int height = -1;
    // first ask layoutManager
    UIComponent layout = component.getFacet("layout");
    if (layout != null) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, layout);
      height = renderer.getFixedHeight(facesContext, component);
    }
    if (height < 0) {

      if (component.getChildren().size() == 0) {
        height = 0;
      }
      else {

        if (LOG.isDebugEnabled()) {
          LOG.debug("Can't calculate fixedHeight! "
              + "using estimation by contained components. for "
              + component.getClientId(facesContext) + " = "
              + component.getClass().getName() + " "
              + component.getRendererType());
        }

        height = 0;
        for (Iterator iterator = component.getChildren().iterator(); iterator.hasNext();) {
          UIComponent child = (UIComponent) iterator.next();
          RendererBase renderer = ComponentUtil.getRenderer(facesContext, child);
          if (renderer == null
              && child instanceof UINamingContainer
              && child.getChildren().size() > 0) {
            // this is a subview component ??
            renderer = ComponentUtil.getRenderer(facesContext, (UIComponent) child.getChildren().get(0));
          }
          if (renderer != null) {
            int h = renderer.getFixedHeight(facesContext, child);
            if (h > 0) {
              height += h;
            }
          }
        }
      }
    }
    return height;
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

// ///////////////////////////////////////////// bean getter + setter

}

