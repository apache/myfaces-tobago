/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 11.11.2003 11:16:41.
 * $Id$
 */
package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UILayout;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;

public class RenderUtil {

  private static final Log LOG = LogFactory.getLog(RenderUtil.class);

  public static final String COMPONENT_IN_REQUEST = "org.apache.myfaces.tobago.component";

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
    return component;
  }

  public static void encodeChildren(FacesContext facesContext,
      UIComponent panel)
      throws IOException {
//    UIComponent layout = panel.getFacet("layout");
    UILayout layout = UILayout.getLayout(panel);
    if (layout != null) {
      layout.encodeChildrenOfComponent(facesContext, panel);
    } else {
      for (Iterator i = panel.getChildren().iterator(); i.hasNext();) {
        UIComponent child = (UIComponent) i.next();
        encode(facesContext, child);
      }
    }
  }

  public static void encode(FacesContext facesContext, UIComponent component) throws IOException {
    if (component.isRendered()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }

      LayoutRenderer layoutRenderer = (LayoutRenderer)
          ComponentUtil.getRenderer(facesContext, UILayout.getLayout(component));
      layoutRenderer.prepareRender(facesContext, component);

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
  }



  public static String addMenuCheckToggle(String clientId, String onClick) {
    if (onClick != null) {
      onClick = " ; " + onClick;
    } else {
      onClick = "";
    }

    onClick = "menuCheckToggle('" + clientId + "')" + onClick;

    return onClick;
  }

}
