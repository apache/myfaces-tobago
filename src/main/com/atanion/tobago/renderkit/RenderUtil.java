/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 11.11.2003 11:16:41.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;

public class RenderUtil {

  private static Log LOG = LogFactory.getLog(RenderUtil.class);

  public static final String COMPONENT_IN_REQUEST = "com.atanion.tobago.component";
  public static final String RENDERER_IN_REQUEST = "com.atanion.tobago.renderer";

  /**
   * for the jsp-sniplets
   */
  public static Log getLog(Class clazz) {
    return getLog(clazz, "_jsp");
  }

  /**
   * for the jsp-sniplets
   */
  public static Log getLogBegin(Class clazz) {
    return getLog(clazz, "Begin_jsp");
  }

  /**
   * for the jsp-sniplets
   */
  public static Log getLogChildren(Class clazz) {
    return getLog(clazz, "Children_jsp");
  }

  /**
   * for the jsp-sniplets
   */
  private static Log getLog(Class clazz, String suffix) {
    String name = clazz.getName();
    int dot = name.lastIndexOf('.');
    String jsp = null;
    if (dot > -1) {
      String plain = name.substring(dot + 1);
      if (plain.endsWith("Renderer")) {
        jsp = plain.substring(0, 1).toLowerCase();
        jsp += plain.substring(1, plain.length() - 8);
      }
      name = name.substring(0, dot + 1);
    }
    if (jsp != null) {
      name += jsp;
    }
    name += suffix;
    return LogFactory.getLog(name);
  }

  public static boolean contains(Object[] list, Object value) {
    if (list == null) {
      return false;
    }
    for (int i = 0; i < list.length; i++) {
      if (list[i] != null && list[i].equals(value)) {
        return true;
      }
    }
    return false;
  }

  public static UIComponent getComponent(HttpServletRequest request) {

    UIComponent component
        = (UIComponent) request.getAttribute(COMPONENT_IN_REQUEST);
//    log.debug("component in request: class=" + component.getClass().getName());
    return component;
  }

  public static Renderer getRenderer(HttpServletRequest request) {

    Renderer renderer
        = (Renderer) request.getAttribute(RENDERER_IN_REQUEST);
//    log.debug("renderer in request: class=" + renderer.getClass().getName());
    return renderer;
  }

  public static void encodePanel(FacesContext facesContext, UIPanel panel)
      throws IOException {
    UIComponent layout = panel.getFacet("layout");
    if (layout != null) {
      encode(facesContext, layout);
    } else {
      for (Iterator i = panel.getChildren().iterator(); i.hasNext(); ) {
        UIComponent child = (UIComponent) i.next();
        encode(facesContext, child);
      }
    }
  }

  public static void encode(FacesContext facesContext, UIComponent component)
      throws IOException {
//    if (LOG.isDebugEnabled()) {
//      LOG.debug("component = '" + component + "'");
//      UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
//      LOG.debug("viewRoot  = '" + viewRoot + "'");
//      ComponentUtil.debug(viewRoot, 0);
//    }
    if (component.isRendered()) {
      if (ComponentUtil.getBooleanAttribute(component, TobagoConstants.ATTR_SUPPRESSED)) {
//        log.debug("!!!! rendering " + component.getRendererType() + "  " + component);

        component.encodeBegin(facesContext);
        if (component.getRendersChildren()) {
          component.encodeChildren(facesContext);
        } else {
          Iterator kids = component.getChildren().iterator();
          while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            encode(facesContext, kid);
          }
        }
        component.encodeEnd(facesContext);
      }
//      else {
//        log.debug("!!!! NOT rendering (!wasSuppressed()) " + component.getRendererType() + "  "  + component);
//
//      }
    }
//    else {
//      log.debug("!!!! NOT rendering (! isRendered()) " + component.getRendererType() + "  "  + component);
//
//    }
  }
}
