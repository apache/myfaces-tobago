/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 01.09.2003 at 17:53:13.
  * $Id$
  */
package com.atanion.tobago.renderkit.html;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.LayoutManager;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;

public class HtmlDefaultLayoutManager implements LayoutManager {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(HtmlDefaultLayoutManager.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void layoutBegin(FacesContext facesContext, UIComponent component) {

    Renderer renderer = ComponentUtil.getRenderer(component, facesContext);

    String widthString = (String) component.getAttributes().get(TobagoConstants.ATTR_WIDTH);
    int width = -1;
    if (widthString != null && widthString.length() > 0) {
      width = Integer.parseInt(widthString.replaceAll("\\D", ""));
    }
    if (width > 0) {
      int extra = 0;
      if (renderer instanceof RendererBase) {
        extra = ((RendererBase)renderer).getComponentExtraWidth(facesContext, component);
      }
      String style = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE);
      style = style != null ? style : "";
      style = style.replaceAll("width:\\s\\d+px;", "").trim();
      component.getAttributes().put(
          TobagoConstants.ATTR_STYLE, style + " width: " + (width - extra) + "px;");
    }


    if (renderer instanceof HeightLayoutRenderer) {
      HtmlDefaultLayoutManager.layoutHeight(component, facesContext);
    }
  }

  public void layoutEnd(FacesContext facesContext, UIComponent component) {

  }


  public static void layoutHeight(UIComponent component, FacesContext facesContext){
    String style = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE);
    style = style != null ? style : "";
    String styleInner = (String) component.getAttributes().get(TobagoConstants.ATTR_STYLE_INNER);
    styleInner = styleInner != null ? styleInner : "";

    String headerStyle = style;
    String bodyStyle = style;

    int heightInt = getHeightAsInt(component);
    if (heightInt != -1) {
      component.getAttributes().put(TobagoConstants.ATTR_STYLE,
          style + " height: " + heightInt + "px;");

      RendererBase renderer = ComponentUtil.getRenderer(component, facesContext);

      int headerHeight = ((HeightLayoutRenderer)
          renderer).getHeaderHeight(facesContext, component) ;

      int bodyHeight = heightInt - headerHeight;
      bodyStyle   += " height: " + bodyHeight + "px;";
      headerStyle += " height: " + headerHeight + "px;";

      int innerHeight = (bodyHeight - renderer.getPaddingHeight(facesContext, component));
      component.getAttributes().put(TobagoConstants.ATTR_INNER_HEIGHT, new Integer(innerHeight));
      styleInner += " height: " + innerHeight + "px";

    }

    component.getAttributes().put(TobagoConstants.ATTR_STYLE_BODY, bodyStyle);
    component.getAttributes().put(TobagoConstants.ATTR_STYLE_HEADER, headerStyle);
    component.getAttributes().put(TobagoConstants.ATTR_STYLE_INNER, styleInner);
  }

  private static int getHeightAsInt(UIComponent component){
    String height = (String) component.getAttributes().get(TobagoConstants.ATTR_HEIGHT);
    if (height != null && height.length() > 0) {
      try {
        return Integer.parseInt(height.replaceAll("\\D",""));
      } catch (NumberFormatException e) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("NumberFormatException", e);
        }

      }
    }
    return -1;
  }

// ///////////////////////////////////////////// bean getter + setter

}

