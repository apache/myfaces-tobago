/*
 * Copyright (c) 2003 Atanion GmbH, Germany
 * All rights reserved. Created 11.11.2003 11:16:41.
 * $Id$
 */
package com.atanion.tobago.renderkit;

import com.atanion.tobago.webapp.TobagoResponseWriter;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.TobagoConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Iterator;

public class RenderUtil {

  private static final Log LOG = LogFactory.getLog(RenderUtil.class);

  public static final String COMPONENT_IN_REQUEST = "com.atanion.tobago.component";

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
    UIComponent layout = panel.getFacet("layout");
    if (layout != null) {
      encode(facesContext, layout);
    } else {
      for (Iterator i = panel.getChildren().iterator(); i.hasNext();) {
        UIComponent child = (UIComponent) i.next();
        encode(facesContext, child);
      }
    }
  }

  public static void encodeHtml(FacesContext facesContext, UIComponent component)
      throws IOException {
    if (component.isRendered()) {

      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }

      prepareRender(facesContext, component);

      component.encodeBegin(facesContext);
      if (component.getRendersChildren()) {
        component.encodeChildren(facesContext);
      } else {
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
          UIComponent kid = (UIComponent) kids.next();
          encodeHtml(facesContext, kid);
        }
      }
      component.encodeEnd(facesContext);
    }

  }

  public static void prepareRender(FacesContext facesContext, UIComponent component) {
    createCssClass(facesContext, component);
    LayoutUtil.layoutWidth(facesContext, component);
    LayoutUtil.layoutHeight(facesContext, component);
  }

  public static void createCssClass(FacesContext facesContext, UIComponent component) {
      final String rendererType = component.getRendererType();
      if (rendererType != null) {
        String rendererName = ComponentUtil.getRenderer(facesContext, component).getRendererName(rendererType);
        LayoutUtil.createClassAttribute(component, rendererName);
      }

  }

  public static void encode(FacesContext facesContext, UIComponent component) throws IOException {
    if (component.isRendered()) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("rendering " + component.getRendererType() + " " + component);
      }

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

  public static void writeLabelWithAccessKey(TobagoResponseWriter writer,
      LabelWithAccessKey label)
      throws IOException {
    int pos = label.getPos();
    String text = label.getText();
    if (pos == -1) {
      writer.writeText(text, null);
    } else {
      writer.writeText(text.substring(0, pos), null);
      writer.write("<u>");
      writer.writeText(new Character(text.charAt(pos)), null);
      writer.write("</u>");
      writer.writeText(text.substring(pos + 1), null);
    }
  }

}
