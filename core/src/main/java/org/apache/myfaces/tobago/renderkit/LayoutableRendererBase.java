package org.apache.myfaces.tobago.renderkit;

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
import org.apache.myfaces.tobago.component.RendererType;
import org.apache.myfaces.tobago.renderkit.html.HtmlAttributes;
import org.apache.myfaces.tobago.renderkit.html.HtmlStyleMap;
import org.apache.myfaces.tobago.renderkit.html.StyleClasses;
import org.apache.myfaces.tobago.util.ComponentUtil;
import org.apache.myfaces.tobago.util.LayoutUtil;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class LayoutableRendererBase
    extends RendererBase implements LayoutableRenderer {

  private static final Log LOG = LogFactory.getLog(LayoutableRendererBase.class);

  public int getHeaderHeight(
      FacesContext facesContext, UIComponent component) {
    int height = getConfiguredValue(facesContext, component, "headerHeight");
    final UIComponent menubar = component.getFacet(Facets.MENUBAR);
    if (menubar != null) {
      height += getConfiguredValue(facesContext, menubar, "headerHeight");
    }
    return height;
  }

  public int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "paddingWidth");
  }

  public int getPaddingHeight(
      FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "paddingHeight");
  }

  public int getComponentExtraWidth(
      FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "componentExtraWidth");
  }

  public int getComponentExtraHeight(
      FacesContext facesContext,
      UIComponent component) {
    return getConfiguredValue(facesContext, component, "componentExtraHeight");
  }

  public Dimension getMinimumSize(
      FacesContext facesContext, UIComponent component) {
    int width = getMinimumWidth(facesContext, component);
    int height = getMinimumHeight(facesContext, component);
    return new Dimension(width, height);
  }

  public int getMinimumHeight(FacesContext facesContext, UIComponent component) {
    int height = getConfiguredValue(facesContext, component, "minimumHeight");
    if (height == -1) {
      height = getConfiguredValue(facesContext, component, "fixedHeight");
    }
    return height;
  }

  public int getMinimumWidth(FacesContext facesContext, UIComponent component) {
    int width = getConfiguredValue(facesContext, component, "minimumWidth");
    if (width == -1) {
      width = getConfiguredValue(facesContext, component, "fixedWidth");
    }
    return width;
  }

  public int getFixedWidth(FacesContext facesContext, UIComponent component) {
    return getFixedSpace(facesContext, component, true);
  }

  public int getFixedHeight(FacesContext facesContext, UIComponent component) {
    return getFixedSpace(facesContext, component, false);
  }

  public int getFixedSpace(FacesContext facesContext, UIComponent component, boolean width) {

    int fixedSpace = 0;

    if (component instanceof Cell) {
      List children = LayoutUtil.addChildren(new ArrayList(), component);
      for (Object aChildren : children) {
        UIComponent child = (UIComponent) aChildren;

        LayoutInformationProvider renderer = ComponentUtil.getRenderer(facesContext, child);
        if (renderer != null) {
          if (width) {
            fixedSpace = Math.max(fixedSpace, renderer.getFixedWidth(facesContext, child));
          } else {
            fixedSpace = Math.max(fixedSpace, renderer.getFixedHeight(facesContext, child));
          }
        }
      }
    } else {
      if (width) {
        fixedSpace = getFixedSpace(facesContext, component, Attributes.WIDTH, "fixedWidth");
      } else {
        fixedSpace = getFixedSpace(facesContext, component, Attributes.HEIGHT, "fixedHeight");
      }
    }
    return fixedSpace;
  }

  private int getFixedSpace(FacesContext facesContext, UIComponent component,
                            String attr, String attrFixed) {
    int intSpace = -1;
    Object space = null;
    if (component != null) {
      space = ComponentUtil.getObjectAttribute(component, attr);
    }
    if (space != null && space instanceof String) {
      try {
        intSpace = Integer.parseInt(((String) space).replaceAll("\\D", ""));
      } catch (NumberFormatException e) {
        LOG.error("Caught: " + e.getMessage(), e);
      }
    }
    if (space != null && space instanceof Integer) {
      intSpace = (Integer) space;
    }
    if (intSpace == -1) {
      return getConfiguredValue(facesContext, component, attrFixed);
    } else {
      return intSpace;
    }
  }

  public void layoutBegin(FacesContext context, UIComponent component) throws IOException {
  }

  public void layoutEnd(FacesContext context, UIComponent component) throws IOException {
  }

  public void prepareRender(FacesContext facesContext, UIComponent component) throws IOException {
    
    prepareDimension(facesContext, component);
    final String rendererType = component.getRendererType();
    if (rendererType != null) {
      final String rendererName = getRendererName(rendererType);
      StyleClasses classes = StyleClasses.ensureStyleClasses(component);
      classes.updateClassAttributeAndMarkup(component, rendererName);
    } else {
      LOG.error("No renderType for " + component); 
    }
    layoutSpace(facesContext, component, true);
    layoutSpace(facesContext, component, false);
  }

   void prepareDimension(FacesContext facesContext, UIComponent component) {
//    LOG.info("prepareDimension for " + component.getClientId(facesContext) + " is " + component.getRendererType());
    setInnerWidth(facesContext, component);
    setInnerHeight(facesContext, component);
  }

  void setInnerWidth(FacesContext facesContext, UIComponent component) {
    Integer layoutWidth = LayoutUtil.getLayoutWidth(component);
    if (layoutWidth != null) {
      int space = layoutWidth.intValue();
      int innerSpace = LayoutUtil.getInnerSpace(facesContext, component, space, true);
      component.getAttributes().put(Attributes.INNER_WIDTH, Integer.valueOf(innerSpace));
    }
  }

  void setInnerHeight(FacesContext facesContext, UIComponent component) {
    Integer layoutHeight = LayoutUtil.getLayoutHeight(component);
    if (layoutHeight != null) {
      int space = layoutHeight.intValue();
      int innerSpace = LayoutUtil.getInnerSpace(facesContext, component, space, false);
      component.getAttributes().put(Attributes.INNER_HEIGHT, Integer.valueOf(innerSpace));
    }
  }

  private void layoutSpace(FacesContext facesContext, UIComponent component, boolean width) {

    // prepare html 'style' attribute
    Integer layoutSpace;
    String layoutAttribute;
    String styleAttribute;
    if (width) {
      layoutSpace = LayoutUtil.getLayoutWidth(component);
      layoutAttribute = Attributes.LAYOUT_WIDTH;
      styleAttribute = HtmlAttributes.WIDTH;
    } else {
      layoutSpace = LayoutUtil.getLayoutHeight(component);
      layoutAttribute = Attributes.LAYOUT_HEIGHT;
      styleAttribute = HtmlAttributes.HEIGHT;
    }
    int space = -1;
    if (layoutSpace != null) {
      space = layoutSpace.intValue();
    }
    if (space == -1
        && (!RendererType.OUT.equals(component.getRendererType()))) {
      UIComponent parent = component.getParent();
      space = LayoutUtil.getInnerSpace(facesContext, parent, width);
      if (space > 0 && !ComponentUtil.isFacetOf(component, parent)) {
        component.getAttributes().put(layoutAttribute, Integer.valueOf(space));
        if (width) {
          component.getAttributes().remove(Attributes.INNER_WIDTH);
        } else {
          component.getAttributes().remove(Attributes.INNER_HEIGHT);
        }
      }
    }
    if (space > 0) {
      if (layoutSpace != null
          || !ComponentUtil.getBooleanAttribute(component, Attributes.INLINE)) {
        int styleSpace = space;
          if (width) {
            styleSpace -= getComponentExtraWidth(facesContext, component);
          } else {
            styleSpace -= getComponentExtraHeight(facesContext, component);
          }

        replaceStyleAttribute(component, styleAttribute, styleSpace);

      }
      UIComponent layout = component.getFacet(Facets.LAYOUT);
      if (layout != null) {
        int layoutSpace2 = LayoutUtil.getInnerSpace(facesContext, component,
            width);
        if (layoutSpace2 > 0) {
          layout.getAttributes().put(layoutAttribute, Integer.valueOf(layoutSpace2));
        }
      }
    }
  }

  private void replaceStyleAttribute(UIComponent component, String styleAttribute, int value) {
      HtmlStyleMap style = ensureStyleAttributeMap(component);
      style.put(styleAttribute, value);
    }

  private HtmlStyleMap ensureStyleAttributeMap(UIComponent component) {
    return ensureStyleAttributeMap(component, Attributes.STYLE);
  }

  private HtmlStyleMap ensureStyleAttributeMap(UIComponent component, String attribute) {
    final Map attributes = component.getAttributes();
    HtmlStyleMap style = (HtmlStyleMap) attributes.get(attribute);
    if (style == null) {
      style = new HtmlStyleMap();
      attributes.put(attribute, style);
    }
    return style;
  }
}
