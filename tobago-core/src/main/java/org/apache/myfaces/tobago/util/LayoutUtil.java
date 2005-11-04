/*
 * Copyright 2002-2005 The Apache Software Foundation.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
/*
  * Created 01.07.2003 at 17:52:10.
  * $Id$
  */
package org.apache.myfaces.tobago.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.*;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.component.UIPanel;
import org.apache.myfaces.tobago.renderkit.InputRendererBase;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class LayoutUtil {

  private static final Log LOG = LogFactory.getLog(LayoutUtil.class);

  private LayoutUtil() {
    // to prevent instantiation
  }

  public static int getInnerSpace(FacesContext facesContext,
      UIComponent component, boolean width) {
    String attribute;
    if (width) {
      attribute = ATTR_INNER_WIDTH;
    } else {
      attribute = ATTR_INNER_HEIGHT;
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
            ATTR_WIDTH);
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
      InputRendererBase renderer = (InputRendererBase)
          ComponentUtil.getRenderer(facesContext, UIInput.COMPONENT_FAMILY,
              RENDERER_TYPE_IN);
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
    return getLayoutSpace(component, ATTR_WIDTH,
        ATTR_LAYOUT_WIDTH);
  }

  public static Integer getLayoutHeight(UIComponent component) {
    return getLayoutSpace(component, ATTR_HEIGHT,
        ATTR_LAYOUT_HEIGHT);
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
    if (RENDERER_TYPE_OUT.equals(cell.getRendererType())) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("set " + value + " to " + cell.getRendererType());
    }
    cell.getAttributes().put(attribute, value);
    if (ATTR_LAYOUT_WIDTH.equals(attribute)) {
      cell.getAttributes().remove(ATTR_INNER_WIDTH);
    } else if (ATTR_LAYOUT_HEIGHT.equals(attribute)){
      cell.getAttributes().remove(ATTR_INNER_HEIGHT);
    }
    if (cell instanceof UIPanel
        && ComponentUtil.getBooleanAttribute(cell,
            ATTR_LAYOUT_DIRECTIVE)) {
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

