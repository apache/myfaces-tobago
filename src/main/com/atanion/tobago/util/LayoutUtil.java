/*
  * Copyright (c) 2002 Atanion GmbH, Germany
  * All rights reserved. Created 01.07.2003 at 17:52:10.
  * Id:  $
  */
package com.atanion.tobago.util;

import com.atanion.tobago.TobagoConstants;
import com.atanion.tobago.component.ComponentUtil;
import com.atanion.tobago.renderkit.HeightLayoutRenderer;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class LayoutUtil {

// ///////////////////////////////////////////// constant

  private static final Log log = LogFactory.getLog(LayoutUtil.class);

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

      String spaceString;
      if (width) {
        spaceString = getLayoutWidth(component);
      } else {
        spaceString = getLayoutHeight(component);
      }
      if (spaceString != null && spaceString.length() > 0) {
        space = Integer.parseInt(spaceString.replaceAll("\\D", ""));
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
        RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(
            "javax.faces.render.RenderKitFactory");
        RenderKit renderKit = rkFactory.getRenderKit(facesContext,
            facesContext.getViewRoot().getRenderKitId());
        RendererBase renderer = (RendererBase) renderKit.getRenderer(component.getFamily(),
            component.getRendererType());
        if (width) {
          margin = renderer.getPaddingWidth(facesContext, component);
          margin += renderer.getComponentExtraWidth(facesContext, component);
        } else {
          if (renderer instanceof HeightLayoutRenderer) {
            margin =
                ((HeightLayoutRenderer) renderer).getHeaderHeight(facesContext,
                    component);
          }
          margin += renderer.getPaddingHeight(facesContext, component);
          margin += renderer.getComponentExtraHeight(facesContext, component);
        }
      } catch (Exception e) {
        if (log.isDebugEnabled()) {
          log.debug("cant find margin", e);
        }
      }
    } else {
      if (log.isDebugEnabled()) {
        log.debug("rendertype = null, component: " + component);
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
        if (labelWidth != null && labelWidth.endsWith("px")) {
          try {
            width =
                Integer.parseInt(
                    labelWidth.substring(0, labelWidth.length() - 2));
          } catch (NumberFormatException e) {
            log.warn("Can't parse label width, using default value", e);
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
      width = getDefaultLabelWidth(facesContext);
    }
    return width;
  }

  private static int getDefaultLabelWidth(FacesContext facesContext) {
    int width = 0;
    try {
      RenderKitFactory rkFactory = (RenderKitFactory) FactoryFinder.getFactory(
          "javax.faces.render.RenderKitFactory");
      RenderKit renderKit = rkFactory.getRenderKit(facesContext,
          facesContext.getViewRoot().getRenderKitId());
      InputRendererBase renderer = (InputRendererBase) renderKit.getRenderer(UIInput.COMPONENT_FAMILY,
          "TextBox");
      width = renderer.getLabelWidth();
    } catch (Exception e) {
      if (log.isWarnEnabled()) {
        log.warn("can't find Label Width", e);
      }
    }
    return width;
  }

  public static int getDefaultLabelWidth() {
    return getDefaultLabelWidth(FacesContext.getCurrentInstance());
  }


  public static String getLayoutSpaceStyle(UIComponent component) {
    StringBuffer sb = new StringBuffer();
    String space = getLayoutSpace(component, TobagoConstants.ATTR_LAYOUT_WIDTH,
        TobagoConstants.ATTR_LAYOUT_WIDTH);
    if (space != null) {
      sb.append(" width: ");
      sb.append(space);
      sb.append(";");
    }
    space = getLayoutSpace(component, TobagoConstants.ATTR_LAYOUT_HEIGHT,
        TobagoConstants.ATTR_LAYOUT_HEIGHT);
    if (space != null) {
      sb.append(" height: ");
      sb.append(space);
      sb.append(";");
    }
    return sb.toString();
  }

  public static String getLayoutWidth(UIComponent component) {
    return getLayoutSpace(component, TobagoConstants.ATTR_WIDTH,
        TobagoConstants.ATTR_LAYOUT_WIDTH);
  }

  public static String getLayoutHeight(UIComponent component) {
    return getLayoutSpace(component, TobagoConstants.ATTR_HEIGHT,
        TobagoConstants.ATTR_LAYOUT_HEIGHT);
  }

  public static String getLayoutSpace(UIComponent component, String attribute,
      String layoutAttribute) {
    String width = (String) component.getAttributes().get(attribute);
    if (width == null) {
      width = (String) component.getAttributes().get(layoutAttribute);
    }
    // fixme:
// no longer needed
//    if (width == null) {
//      UIComponent parent = LayoutUtil.getNonTransparentParent(component);
//      if (parent instanceof UIOutput
//          && parent.getRendererType().equals("Include")) {
//        width = (String) parent.getAttributes().get(layoutAttribute);
//      }
//    }
    return width;
  }


  public static Vector addChildren(Vector children, UIComponent panel) {
    for (Iterator iter = panel.getChildren().iterator(); iter.hasNext();) {
      UIComponent child = (UIComponent) iter.next();
      if (ComponentUtil.getBooleanAttribute(child,
          TobagoConstants.ATTR_LAYOUT_TRANSPARENT)) {
        addChildren(children, child);
      } else {
        children.add(child);
      }
    }
    return children;
  }

  public static UIComponent getNonTransparentParent(UIComponent component) {
    UIComponent parent = component.getParent();
    while (parent != null
        &&
        ComponentUtil.getBooleanAttribute(parent,
            TobagoConstants.ATTR_LAYOUT_TRANSPARENT)) {
      parent = parent.getParent();
    }
    return parent;
  }

  public static void maybeSetLayoutAttribute(UIComponent cell, String attribute,
      String value) {
    if ("Text".equals(cell.getRendererType())) {
      return;
    }
    if (log.isDebugEnabled()) {
      log.debug("set " + value + " to " + cell.getRendererType());
    }
    cell.getAttributes().put(attribute, value);
    if (cell instanceof UIPanel
        && ComponentUtil.getBooleanAttribute(cell,
            TobagoConstants.ATTR_LAYOUT_DIRECTIVE)) {
      Vector children = LayoutUtil.addChildren(new Vector(), cell);
      for (Iterator childs = children.iterator(); childs.hasNext();) {
        UIComponent component = (UIComponent) childs.next();
        maybeSetLayoutAttribute(component, attribute, value);
      }
    }
  }

  public static String getStyleAttributeValue(String style, String name) {
    if (style == null) {
      return null;
    }
    String value = null;
    StringTokenizer st = new StringTokenizer(style, ";");
    while (st.hasMoreTokens()) {
      String attribute = st.nextToken().trim();
      if (attribute.startsWith(name)) {
        value = attribute.substring(attribute.indexOf(':') + 1).trim();
      }
    }
    return value;
  }

  public static String replaceStyleAttribute(String style, String name,
      String value) {
    style = removeStyleAttribute(style, name);
    return style + " " + name + ": " + value + ";";
  }

  public static String removeStyleAttribute(String style, String name) {
    if (style == null) {
      return null;
    }
    String pattern = name + "\\s*?:[^;]*?;";
    return style.replaceAll(pattern, "").trim();
  }


// ///////////////////////////////////////////// bean getter + setter


}

