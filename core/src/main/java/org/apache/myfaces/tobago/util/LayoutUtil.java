package org.apache.myfaces.tobago.util;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INLINE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_INNER_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_HEIGHT;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LAYOUT_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_MINIMUM_SIZE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_WIDTH;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.RENDERER_TYPE_OUT;
import org.apache.myfaces.tobago.component.ComponentUtil;
import org.apache.myfaces.tobago.component.UICell;
import org.apache.myfaces.tobago.component.UIForm;
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.awt.Dimension;
import java.util.ArrayList;
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
        space = spaceInteger;
      }

//      if (space < 0 && component.getParent() instanceof UIComponentBase) {
      if (space < 0 && component.getParent() != null) {
        space = getInnerSpace(facesContext, component.getParent(), width);
      }

      if (space != -1) {
        innerSpace = getInnerSpace(facesContext, component, space, width);
      } else {
        innerSpace = -1;
      }

      component.getAttributes().put(attribute, innerSpace);
    }

    return innerSpace;
  }

  public static int getInnerSpace(FacesContext facesContext, UIComponent component,
      int outerSpace, boolean width) {
    int margin = 0;
    if (component.getRendererType() != null) {
      try {

        LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);

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
    if (component != null) {
      UIComponent label = component.getFacet(FACET_LABEL);
      if (label != null) {
        String labelWidth = (String) label.getAttributes().get(ATTR_WIDTH);
        if (labelWidth != null) {
          try {
            return Integer.parseInt(labelWidth.replaceAll("\\D", ""));
          } catch (NumberFormatException e) {
            LOG.warn("Can't parse label width, using default value", e);
          }
        }
      }
    }
    return 0;
  }

  //TODO Change this to DimensionUtils.getWidth?
  public static Integer getLayoutWidth(UIComponent component) {
    return getLayoutSpace(component, ATTR_WIDTH, ATTR_LAYOUT_WIDTH);
  }
  //TODO Change this to DimensionUtils.getHeight?
  public static Integer getLayoutHeight(UIComponent component) {
    return getLayoutSpace(component, ATTR_HEIGHT, ATTR_LAYOUT_HEIGHT);
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
      return (Integer) value;
    }
    return null;
  }

  public static List<UIComponent> addChildren(List<UIComponent> children, UIComponent panel) {
    for (Object o : panel.getChildren()) {
      UIComponent child = (UIComponent) o;
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
    // TODO find a better way
    if ("facelets".equals(component.getFamily())) {
      return !"com.sun.facelets.tag.UIDebug".equals(component.getClass().getName());
    }
    /* TODO disable layouting of facelet stuff
    if (component.getClass().getPackage().getName().equals("com.sun.facelets.compiler")) {
      return true;
    } */
//  also Forms are transparent for layouting

    return component instanceof UIForm;
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
    } else if (ATTR_LAYOUT_HEIGHT.equals(attribute)) {
      cell.getAttributes().remove(ATTR_INNER_HEIGHT);
    }
    if (cell instanceof UICell) {
      List<UIComponent> children = addChildren(new ArrayList<UIComponent>(), cell);
      for (UIComponent component : children) {
        maybeSetLayoutAttribute(component, attribute, value);
      }
    }
  }

  public static int calculateFixedHeightForChildren(FacesContext facesContext, UIComponent component) {
    int height = 0;
    for (Object o : component.getChildren()) {
      UIComponent child = (UIComponent) o;
      LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, child);
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

  public static Dimension getMinimumSize(
      FacesContext facesContext, UIComponent component) {
    Dimension dimension
        = (Dimension) component.getAttributes().get(ATTR_MINIMUM_SIZE);
    if (dimension == null) {
      LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);
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

