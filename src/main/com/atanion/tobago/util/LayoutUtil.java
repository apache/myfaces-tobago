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
import com.atanion.tobago.component.UILayout;
import com.atanion.tobago.renderkit.InputRendererBase;
import com.atanion.tobago.renderkit.RendererBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang.StringUtils;

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
import java.util.Map;
import java.util.StringTokenizer;

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

      Integer  spaceString;
      if (width) {
        spaceString = getLayoutWidth(component);
      } else {
        spaceString = getLayoutHeight(component);
      }
      if (spaceString != null) {
        space = spaceString.intValue();
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
        if (labelWidth != null && labelWidth.endsWith("px")) {
          try {
            width =
                Integer.parseInt(
                    labelWidth.substring(0, labelWidth.length() - 2));
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


  public static String getLayoutSpaceStyle(UIComponent component) {
    StringBuffer sb = new StringBuffer();
    Integer  space = getLayoutSpace(component, TobagoConstants.ATTR_LAYOUT_WIDTH,
        TobagoConstants.ATTR_LAYOUT_WIDTH);
    if (space != null) {
      sb.append(" width: ");
      sb.append(space);
      sb.append("px;");
    }
    space = getLayoutSpace(component, TobagoConstants.ATTR_LAYOUT_HEIGHT,
        TobagoConstants.ATTR_LAYOUT_HEIGHT);
    if (space != null) {
      sb.append(" height: ");
      sb.append(space);
      sb.append("px;");
    }
    return sb.toString();
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
    } else {
      value = ComponentUtil.getAttribute(component, layoutAttribute);
      return (Integer) value ;
    }
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
    style = removeStyleAttribute(style != null ? style : "", name);
    return style + " " + name + ": " + value + ";";
  }

  public static String removeStyleAttribute(String style, String name) {
    if (style == null) {
      return null;
    }
    String pattern = name + "\\s*?:[^;]*?;";
    return style.replaceAll(pattern, "").trim();
  }

  public static void addCssClass(UIComponent component, String newClass) {
    final Map attributes = component.getAttributes();
    String cssClass = (String) attributes.get(TobagoConstants.ATTR_STYLE_CLASS);
    if (cssClass == null) {
      attributes.put(TobagoConstants.ATTR_STYLE_CLASS, newClass);
    } else if (cssClass.indexOf(newClass + " ") == -1
        || !cssClass.equals(newClass) || !cssClass.endsWith(newClass)) {
      attributes.put(TobagoConstants.ATTR_STYLE_CLASS, cssClass += " " + newClass);
    }
  }


  public static void layoutWidth(FacesContext facesContext, UIComponent component) {
    layoutSpace(facesContext, component, true);
  }
  public static void layoutHeight(FacesContext facesContext, UIComponent component) {
    layoutSpace(facesContext, component, false);
  }

  public static void layoutSpace(FacesContext facesContext, UIComponent component,
      boolean width) {

    // prepare html 'style' attribute

    Integer layoutSpace;
    String layoutAttribute;
    String innerAttribute;
    String styleAttribute;
    if (width) {
      layoutSpace = LayoutUtil.getLayoutWidth(component);
      layoutAttribute = ATTR_LAYOUT_WIDTH;
      innerAttribute = ATTR_INNER_WIDTH;
      styleAttribute = "width";
    } else {
      layoutSpace = LayoutUtil.getLayoutHeight(component);
      layoutAttribute = ATTR_LAYOUT_HEIGHT;
      innerAttribute = ATTR_INNER_HEIGHT;
      styleAttribute = "height";
    }
    int space = -1;
    if (layoutSpace != null) {
      space = layoutSpace.intValue();
    }
    if (space == -1 && (!RENDERER_TYPE_OUT.equals(component.getRendererType()))) {
      UIComponent parent = component.getParent();
//      if (parent instanceof UIComponentBase) { // don't know why, todo: ex me
      space = LayoutUtil.getInnerSpace(facesContext, parent, width);
      if (space > 0 && !ComponentUtil.isFacetOf(component, parent)) {
        component.getAttributes().put(layoutAttribute, new Integer(space));
      }
//      }
    }
    if (space > 0) {
      RendererBase renderer = ComponentUtil.getRenderer(facesContext, component);
//          = (Integer) component.getAttributes().get(innerAttribute);
      int bodySpace = -1;
      int headerSpace = -1;
//      if (innerSpaceInteger == null) {
      bodySpace = 0;
      headerSpace = 0;
      if (! width) {
        if (renderer != null) {
          headerSpace = renderer.getHeaderHeight(facesContext, component);
        }
        bodySpace = space - headerSpace;
      }
//      }
      if (layoutSpace != null
          || !ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
        int styleSpace = space;
        if (renderer != null) {
          if (width) {
            styleSpace -= renderer.getComponentExtraWidth(facesContext, component);
          } else {
            styleSpace -= renderer.getComponentExtraHeight(facesContext, component);
          }
        }
//        if (log.isDebugEnabled()) {
//          log.debug("width '" + width + "'");
//          log.debug("stype '" + styleSpace + "'");
//        }
        String style = (String)
            component.getAttributes().get(ATTR_STYLE);
        style = style != null ? style : "";
        style = style.replaceAll(styleAttribute + ":\\s\\d+px;", "").trim();

        if ( RENDERER_TYPE_SHEET.equals(component.getRendererType())/*
          || component.getRendererType().equals("TabGroup")
          || component.getRendererType().equals("Tab")*/) { // fixme: not nice;: equals("Sheet"
          String headerStyle;
          String bodyStyle;
          if (width) {
            headerStyle = style + " width: " + styleSpace + "px;";
            bodyStyle = style + " width: " + styleSpace + "px;";
          } else {
            headerStyle =
                (String)
                component.getAttributes().get(ATTR_STYLE_HEADER);
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
                component.getAttributes().get(ATTR_STYLE_HEADER);
            if (bodyStyle == null) {
              LOG.warn("bodyStyle attribute == null, set to empty String");
              bodyStyle = "";
            }
            if (bodySpace != -1) {
              bodyStyle = bodyStyle.replaceAll("height:\\s\\d+px;", "").trim();
              bodyStyle += " height: " + bodySpace + "px;";
            }
          }
          component.getAttributes().put(ATTR_STYLE_HEADER, headerStyle);
          component.getAttributes().put(ATTR_STYLE_BODY, bodyStyle);
        }

        component.getAttributes().put(ATTR_STYLE, style + " "
            + styleAttribute + ": " + styleSpace + "px;");
//        if (log.isDebugEnabled()) {
//          log.debug("style = '" + style + "'");
//        }
        String innerStyle;
        if (width) {
          innerStyle = style;
        } else {
          innerStyle = (String) component.getAttributes().get(ATTR_STYLE_INNER);
        }
        {
          // todo: this is only used in BoxRenderer, move it
          Integer innerSpaceInteger
              = (Integer) component.getAttributes().get(innerAttribute);
          component.getAttributes().put(ATTR_STYLE_INNER,
              innerStyle + " " + styleAttribute + ": " + innerSpaceInteger +
              "px;");
        }
      }
      UIComponent layout = component.getFacet("layout");
      if (layout != null) {
        int layoutSpace2 = LayoutUtil.getInnerSpace(facesContext, component,
            width);
        if (layoutSpace2 > 0) {
          layout.getAttributes().put(layoutAttribute, new Integer(layoutSpace2));
        }
      }
    }
  }



   public static void createClassAttribute(UIComponent component, String name) {
    String rendererType = component.getRendererType();
    if (rendererType != null) {
      Object styleClassO = component.getAttributes().get(ATTR_STYLE_CLASS);
      if (styleClassO != null && LOG.isDebugEnabled()) {
        LOG.debug("styleClassO = '" + styleClassO.getClass().getName() + "'");
      }
      String styleClass
          = (String) component.getAttributes().get(ATTR_STYLE_CLASS);
      styleClass = LayoutUtil.updateClassAttribute(styleClass, name, component);
      component.getAttributes().put(ATTR_STYLE_CLASS, styleClass);
    }
  }
  public static String updateClassAttribute(
      String cssClass, String rendererName,
      UIComponent component) {
    if (cssClass != null) {
      // first remove old tobago-<rendererName>-<type> classes from class-attribute
      cssClass = cssClass.replaceAll(
          "tobago-" + rendererName
          + "-(default|disabled|readonly|inline|error)", "").trim();
      // remove old tobago-<rendererName>-markup-<type> classes from class-attribute
      cssClass = cssClass.replaceAll(
          "tobago-" + rendererName + "-markup-(strong|deleted)", "").trim();
    } else {
      cssClass = "";
    }
    String tobagoClass = "tobago-" + rendererName + "-default ";
    if (ComponentUtil.getBooleanAttribute(component, ATTR_DISABLED)) {
      tobagoClass += "tobago-" + rendererName + "-disabled ";
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_READONLY)) {
      tobagoClass += "tobago-" + rendererName + "-readonly ";
    }
    if (ComponentUtil.getBooleanAttribute(component, ATTR_INLINE)) {
      tobagoClass += "tobago-" + rendererName + "-inline ";
    }
    if (ComponentUtil.isError(component)) {
      tobagoClass += "tobago-" + rendererName + "-error ";
    }
    String markup = ComponentUtil.getStringAttribute(component, ATTR_MARKUP);
    if (StringUtils.isNotEmpty(markup)) {
      if (markup.equals("strong") || markup.equals("deleted")) {
        tobagoClass += "tobago-" + rendererName + "-markup-" + markup + " ";
      } else {
        LOG.warn("Unknown markup='" + markup + "'");
      }
    }

    return tobagoClass + cssClass;
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

}

