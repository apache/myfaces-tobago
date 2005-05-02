/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 01.07.2003 at 17:52:10.
  * Id:  $
  */
package com.atanion.tobago.util;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.component.UIForm;
import com.atanion.tobago.component.UIPanel;
import com.atanion.tobago.component.UITabGroup;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.awt.*;

public class LayoutUtil implements TobagoConstants{

// ///////////////////////////////////////////// constant

  private static final Log LOG = LogFactory.getLog(LayoutUtil.class);

// ///////////////////////////////////////////// attribute

// ///////////////////////////////////////////// constructor

// ///////////////////////////////////////////// code


  public static int getInnerSpace(FacesContext facesContext,
      UIComponent component, boolean width) {
    String attribute;
    if (width) {
      attribute = TobagoConstants.ATTR_INNER_WIDTH;
    } else {
      attribute = TobagoConstants.ATTR_INNER_HEIGHT;
    }
    Integer innerSpace = (Integer) component.getAttributes().get(attribute);

    if (innerSpace == null) {
      int space = -1;

      Integer  spaceInteger;
      if (width) {
        spaceInteger = getLayoutWidth(component);
      } else {
        spaceInteger = getLayoutHeight(component);
      }
      if (spaceInteger != null) {
        space = spaceInteger.intValue();
      }

//      if (space < 0 && component.getParent() instanceof UIComponentBase) {
      if (space < 0 && component.getParent() != null) {
        space = getInnerSpace(facesContext, component.getParent(), width);
      }

      if (space != -1) {
        innerSpace =
            new Integer(getInnerSpace(facesContext, component, space, width));
      } else {
        innerSpace = new Integer(-1);
      }

      component.getAttributes().put(attribute, innerSpace);
    }

    return innerSpace.intValue();
  }

  public static int getInnerSpace(FacesContext facesContext, UIComponent component,
      int outerSpace, boolean width) {
    int margin = 0;
    if (component.getRendererType() != null) {
      try {

        RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);

        if (width) {
          margin += renderer.getPaddingWidth(facesContext, component);
          margin += renderer.getComponentExtraWidth(facesContext, component);
        } else {
          margin += renderer.getHeaderHeight(facesContext, component);
          margin += renderer.getPaddingHeight(facesContext, component);
          margin += renderer.getComponentExtraHeight(facesContext, component);
        }
      } catch (Exception e) {
        if (LOG.isDebugEnabled()) {
          LOG.debug("cant find margin", e);
        }
      }
    } else {
      if (LOG.isDebugEnabled()) {
        LOG.debug("renderertype = null, component: " + component);
      }
    }
    return outerSpace - margin;
  }


  public static int getLabelWidth(UIComponent component) {
    int width = 0;

    if (component != null) {
      UIComponent label = component.getFacet("label");
      if (label != null) {
        String labelWidth = (String) label.getAttributes().get(
            TobagoConstants.ATTR_WIDTH);
        if (labelWidth != null) {
          try {
            width = Integer.parseInt(labelWidth.replaceAll("\\D", ""));
          } catch (NumberFormatException e) {
            LOG.warn("Can't parse label width, using default value", e);
          }
        }
      }
    }
    return width;
  }

  public static int getLabelWidth(FacesContext facesContext,
      UIComponent component) {

    int width = getLabelWidth(component);

    if (width == 0) {
      width = getDefaultLabelWidth(facesContext, component);
    }
    return width;
  }

  private static int getDefaultLabelWidth(
      FacesContext facesContext, UIComponent component) {
    int width = 0;
    try {
      RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(
          "javax.faces.render.RenderKitFactory");
      RenderKit renderKit = rkFactory.getRenderKit(facesContext,
          facesContext.getViewRoot().getRenderKitId());
      InputRendererBase renderer = (InputRendererBase)
          renderKit.getRenderer(UIInput.COMPONENT_FAMILY, TobagoConstants.RENDERER_TYPE_IN);
      width = renderer.getLabelWidth(facesContext, component);
    } catch (Exception e) {
      if (LOG.isWarnEnabled()) {
        LOG.warn("can't find Label Width", e);
      }
    }
    return width;
  }

  public static int getDefaultLabelWidth() {
    return getDefaultLabelWidth(FacesContext.getCurrentInstance(), null);
  }


  public static Integer getLayoutWidth(UIComponent component) {
    return getLayoutSpace(component, TobagoConstants.ATTR_WIDTH,
        TobagoConstants.ATTR_LAYOUT_WIDTH);
  }

  public static Integer getLayoutHeight(UIComponent component) {
    return getLayoutSpace(component, TobagoConstants.ATTR_HEIGHT,
        TobagoConstants.ATTR_LAYOUT_HEIGHT);
  }

  public static Integer getLayoutSpace(UIComponent component,
      String sizeAttribute, String layoutAttribute) {
    Object value = ComponentUtil.getAttribute(component, sizeAttribute);
    if (value != null) {
      if (value instanceof String) {
        return new Integer(((String) value).replaceAll("\\D", ""));
      } else {
        return (Integer) value;
      }
    } else if (!ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {

      value = ComponentUtil.getAttribute(component, layoutAttribute);
      return (Integer) value ;
    }
    return null;
  }

  public static List addChildren(List children, UIComponent panel) {
    for (Iterator iter = panel.getChildren().iterator(); iter.hasNext();) {
      UIComponent child = (UIComponent) iter.next();
      if (isTransparentForLayout(child)) {
        addChildren(children, child);
      } else {
        children.add(child);
      }
    }
    return children;
  }

  public static boolean isTransparentForLayout(UIComponent component) {

//    SubViewTag's component is UINamingContainer with 'null' rendererType
//    is transparent for layouting

    if (component instanceof UINamingContainer
        && component.getRendererType() == null) {
      return true;
    }

//  also Forms are transparent for layouting

    if (component instanceof UIForm) {
      return true;
    }

    return false;
  }

  public static UIComponent getLayoutParent(UIComponent component) {
    UIComponent parent = component.getParent();
    while (parent != null && isTransparentForLayout(parent)) {
      parent = parent.getParent();
    }
    return parent;
  }

  public static void maybeSetLayoutAttribute(UIComponent cell, String attribute,
      Integer value) {
    if (TobagoConstants.RENDERER_TYPE_OUT.equals(cell.getRendererType())) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("set " + value + " to " + cell.getRendererType());
    }
    cell.getAttributes().put(attribute, value);
    if (TobagoConstants.ATTR_LAYOUT_WIDTH.equals(attribute)) {
      cell.getAttributes().remove(TobagoConstants.ATTR_INNER_WIDTH);
    } else if (TobagoConstants.ATTR_LAYOUT_HEIGHT.equals(attribute)){
      cell.getAttributes().remove(TobagoConstants.ATTR_INNER_HEIGHT);
    }
    if (cell instanceof UIPanel
        && ComponentUtil.getBooleanAttribute(cell,
            TobagoConstants.ATTR_LAYOUT_DIRECTIVE)) {
      List children = LayoutUtil.addChildren(new ArrayList(), cell);
      for (Iterator childs = children.iterator(); childs.hasNext();) {
        UIComponent component = (UIComponent) childs.next();
        maybeSetLayoutAttribute(component, attribute, value);
      }
    }
  }


  public static int calculateFixedHeightForChildren(FacesContext facesContext, UIComponent component) {
    int height = 0;
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
    return height;
  }


  //  ////////////////////////////////////////////////////////////////

  public static Dimension getMinimumSize(
      FacesContext facesContext, UIComponent component) {
    Dimension dimension
        = (Dimension) component.getAttributes().get(ATTR_MINIMUM_SIZE);
    if (dimension == null) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
      if (renderer != null) {
        dimension = renderer.getMinimumSize(facesContext, component);
      }
    }
    if (dimension == null) {
      dimension = new Dimension(-1, -1);
    }    
    return dimension;
  }

}

