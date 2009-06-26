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
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Cell;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.Form;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.renderkit.LayoutInformationProvider;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public final class LayoutUtils {

  private static final Log LOG = LogFactory.getLog(LayoutUtils.class);

  private static final Pattern TOKEN_PATTERN
      = Pattern.compile("^(\\d*px|\\d*\\*|\\d*%|fixed)$");

  private LayoutUtils() {
    // to prevent instantiation
  }

  public static int getInnerSpace(FacesContext facesContext,
      UIComponent component, boolean width) {
    String attribute;
    if (width) {
      attribute = Attributes.INNER_WIDTH;
    } else {
      attribute = Attributes.INNER_HEIGHT;
    }
    Integer innerSpace = (Integer) component.getAttributes().get(attribute);

    if (innerSpace == null) {
      int space = -1;

      Integer spaceInteger;
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
      UIComponent label = component.getFacet(Facets.LABEL);
      if (label != null) {
        String labelWidth = (String) label.getAttributes().get(Attributes.WIDTH);
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
    return getLayoutSpace(component, Attributes.WIDTH, Attributes.LAYOUT_WIDTH);
  }

  //TODO Change this to DimensionUtils.getHeight?
  public static Integer getLayoutHeight(UIComponent component) {
    return getLayoutSpace(component, Attributes.HEIGHT, Attributes.LAYOUT_HEIGHT);
  }

  public static Integer getLayoutSpace(UIComponent component,
      String sizeAttribute, String layoutAttribute) {
    Object value = ComponentUtil.getAttribute(component, sizeAttribute);
    if (value != null) {
      if (value instanceof String) {
        return new Integer(((String) value).replaceAll("\\D", ""));
      } else if (value instanceof Measure) {
        return ((Measure) value).getPixel();
      } else {
        return (Integer) value;
      }
    } else if (!ComponentUtil.getBooleanAttribute(component, Attributes.INLINE)) {

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

    return component instanceof Form;
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
    if (RendererTypes.OUT.equals(cell.getRendererType())) {
      return;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("set " + value + " to " + cell.getRendererType());
    }
    cell.getAttributes().put(attribute, value);
    if (Attributes.LAYOUT_WIDTH.equals(attribute)) {
      cell.getAttributes().remove(Attributes.INNER_WIDTH);
    } else if (Attributes.LAYOUT_HEIGHT.equals(attribute)) {
      cell.getAttributes().remove(Attributes.INNER_HEIGHT);
    }
    if (cell instanceof Cell) {
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

  public static int getMinimumWidth(FacesContext facesContext, UIComponent component) {
    LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null) {
       return renderer.getMinimumWidth(facesContext, component);
    }
    return -1;
  }

  public static int getMinimumHeight(FacesContext facesContext, UIComponent component) {
    LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null) {
       return renderer.getMinimumHeight(facesContext, component);
    }
    return -1;
  }

  public static Dimension getMinimumSize(
      FacesContext facesContext, UIComponent component) {
    Dimension dimension = null;
    LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, component);
    if (renderer != null) {
      dimension = renderer.getMinimumSize(facesContext, component);
    }
    if (dimension == null) {
      dimension = new Dimension(-1, -1);
    }
    return dimension;
  }

  public static boolean checkTokens(String columns) {
    StringTokenizer st = new StringTokenizer(columns, ";");
    while (st.hasMoreTokens()) {
      String token = st.nextToken();
      if (!TOKEN_PATTERN.matcher(token).matches()) {
        return false;
      }
    }
    return true;
  }
}

