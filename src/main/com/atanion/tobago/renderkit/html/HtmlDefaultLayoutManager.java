/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 01.09.2003 at 17:53:13.
  * $Id$
  */
package com.atanion.tobago.renderkit.html;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.util.LayoutUtil;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.LayoutManager;
import com.atanion.tobago.renderkit.RendererBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.render.Renderer;
import java.util.Iterator;

public class HtmlDefaultLayoutManager implements LayoutManager {

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(HtmlDefaultLayoutManager.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code

  public void layoutBegin(FacesContext facesContext, UIComponent component) {

//    layoutSpace(component, facesContext, true);
//

    RendererBase renderer = ComponentUtil.getRenderer(component, facesContext);

    int width = getWidthAsInt(component);
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
//      layoutSpace(component, facesContext, false);
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
    return getVaueAsInt(component, TobagoConstants.ATTR_HEIGHT,
        TobagoConstants.ATTR_LAYOUT_HEIGHT);
  }

  private static int getWidthAsInt(UIComponent component){
    return getVaueAsInt(component, TobagoConstants.ATTR_WIDTH,
        TobagoConstants.ATTR_LAYOUT_WIDTH);
  }

  private static int getVaueAsInt(UIComponent component, String name,
      String layoutName){
//    String value = (String) component.getAttributes().get(name);
    String value = LayoutUtil.getLayoutSpace(component, name, layoutName);
    if (value != null && value.length() > 0) {
      try {
        return Integer.parseInt(value.replaceAll("\\D",""));
      } catch (NumberFormatException e) {
        if (LOG.isWarnEnabled()) {
          LOG.warn("NumberFormatException: " + e.getMessage(), e);
        }

      }
    }
    return -1;
  }




  public static void layoutSpace(UIComponent component, FacesContext facesContext,
      boolean width) {
    String spaceString;
    String componentAttribute;
    String layoutAttribute;
    String innerAttribute;
    String styleAttribute;
    if (width) {
      spaceString = LayoutUtil.getLayoutWidth(component);
      componentAttribute = TobagoConstants.ATTR_WIDTH;
      layoutAttribute = TobagoConstants.ATTR_LAYOUT_WIDTH;
      innerAttribute = TobagoConstants.ATTR_INNER_WIDTH;
      styleAttribute = "width";
    } else {
      spaceString = LayoutUtil.getLayoutHeight(component);
      componentAttribute = TobagoConstants.ATTR_HEIGHT;
      layoutAttribute = TobagoConstants.ATTR_LAYOUT_HEIGHT;
      innerAttribute = TobagoConstants.ATTR_INNER_HEIGHT;
      styleAttribute = "height";
    }
    String componentSpace = (String)
//        component.getAttributes().get(componentAttribute);
      LayoutUtil.getLayoutSpace(component, componentAttribute,  layoutAttribute);
    int space = -1;
    if (spaceString != null) {
      space = Integer.parseInt(spaceString.replaceAll("\\D", ""));
    }
    if (space == -1 && (!"Text".equals(component.getRendererType()))) {
      UIComponent parent = component.getParent();
//      if (parent instanceof UIComponentBase) { // don't know why, todo: ex me
      space = LayoutUtil.getInnerSpace(facesContext, parent, width);
      if (space > 0 && !ComponentUtil.isFacetOf(component, parent)) {
        component.getAttributes().put(layoutAttribute, "" + space + "px");
      }
//      }
    }
    if (space > 0) {
      RendererBase renderer = ComponentUtil.getRenderer(component,
          facesContext);
      Integer innerSpaceInteger
          = null;
//          = (Integer) component.getAttributes().get(innerAttribute);
      int bodySpace = -1;
      int headerSpace = -1;
//      if (innerSpaceInteger == null) {
      int innerSpace = 0;
      bodySpace = 0;
      headerSpace = 0;
      if (width) {
        innerSpace = LayoutUtil.getInnerSpace(facesContext, component, space,
            width);
      } else {
        headerSpace = ((HeightLayoutRenderer) renderer)
            .getHeaderHeight(facesContext, component);
        bodySpace = space - headerSpace;
        innerSpace = LayoutUtil.getInnerSpace(facesContext, component,
            space, width);
      }
      innerSpaceInteger = new Integer(innerSpace);
      component.getAttributes().put(innerAttribute, innerSpaceInteger);
//      }
      if (componentSpace != null
          || !ComponentUtil.getBooleanAttribute(component,
              TobagoConstants.ATTR_INLINE)) {
        int styleSpace = space;
        if (width) {
          styleSpace -= ComponentUtil.getRenderer(component, facesContext)
              .getComponentExtraWidth(facesContext, component);
        } else {
          styleSpace -= ComponentUtil.getRenderer(component, facesContext)
              .getComponentExtraHeight(facesContext, component);
        }
//        if (log.isDebugEnabled()) {
//          log.debug("width '" + width + "'");
//          log.debug("stype '" + styleSpace + "'");
//        }
        String style = (String)
            component.getAttributes().get(TobagoConstants.ATTR_STYLE);
        style = style != null ? style : "";
        style = style.replaceAll(styleAttribute + ":\\s\\d+px;", "").trim();

        if (renderer instanceof HeightLayoutRenderer) {
          String headerStyle;
          String bodyStyle;
          if (width) {
            headerStyle = style + " width: " + styleSpace + "px;";
            bodyStyle = style + " width: " + styleSpace + "px;";
          } else {
            headerStyle =
                (String)
                component.getAttributes().get(
                    TobagoConstants.ATTR_STYLE_HEADER);
            if (headerStyle == null) {
              LOG.warn("headerStyle attribute == null, set to empty String");
              headerStyle = "";
            }
            if (headerSpace != -1) {
              headerStyle =
                  headerStyle.replaceAll("height:\\s\\d+px;", "").trim();
              headerStyle += " height: " + headerSpace + "px;";
            }
            bodyStyle =
                (String)
                component.getAttributes().get(
                    TobagoConstants.ATTR_STYLE_HEADER);
            if (bodyStyle == null) {
              LOG.warn("bodyStyle attribute == null, set to empty String");
              bodyStyle = "";
            }
            if (bodySpace != -1) {
              bodyStyle = bodyStyle.replaceAll("height:\\s\\d+px;", "").trim();
              bodyStyle += " height: " + bodySpace + "px;";
            }
          }
          component.getAttributes().put(TobagoConstants.ATTR_STYLE_HEADER,
              headerStyle);
          component.getAttributes().put(TobagoConstants.ATTR_STYLE_BODY,
              bodyStyle);
        }

        component.getAttributes().put(TobagoConstants.ATTR_STYLE, style + " "
            + styleAttribute + ": " + styleSpace + "px;");
//        if (log.isDebugEnabled()) {
//          log.debug("style = '" + style + "'");
//        }
        String innerStyle;
        if (width) {
          innerStyle = style;
        } else {
          innerStyle =
              (String) component.getAttributes().get(
                  TobagoConstants.ATTR_STYLE_INNER);
        }
        component.getAttributes().put(TobagoConstants.ATTR_STYLE_INNER,
            innerStyle + " " + styleAttribute + ": " + innerSpaceInteger +
            "px;");
      }
      UIComponent layout = component.getFacet("layout");
      if (layout != null) {
        int layoutSpace = LayoutUtil.getInnerSpace(facesContext, component,
            width);
        if (layoutSpace > 0) {
          layout.getAttributes().put(layoutAttribute, "" + layoutSpace + "px");
        }
      }
    }
  }








// ///////////////////////////////////////////// bean getter + setter

}

