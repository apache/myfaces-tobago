package org.apache.myfaces.tobago.internal.layout;

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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.Form;
import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;

import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public final class LayoutUtils {

  private static final Logger LOG = LoggerFactory.getLogger(LayoutUtils.class);

  private static final Pattern TOKEN_PATTERN = Pattern.compile("^(\\d*px|\\d*\\*|\\d*%|fixed)$");

  private LayoutUtils() {
    // to prevent instantiation
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

    if (component instanceof Form) {
      return true;
    }
    
    return false;
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

  public static List<LayoutComponent> findLayoutChildren(LayoutContainer container) {
    List<LayoutComponent> result = new ArrayList<LayoutComponent>();
    addLayoutChildren((UIComponent) container, result);
    return result;
  }

  private static void addLayoutChildren(UIComponent component, List<LayoutComponent> result) {
    for (UIComponent child : (List<UIComponent>) component.getChildren()) {
      if (child instanceof LayoutComponent) {
        result.add((LayoutComponent) child);
      } else {
        // Child seems to be transparent for layout, like UIForm. 
        // So we try to add the inner components.
        addLayoutChildren(child, result);
      }
    }
  }

  public static Measure getOffsetEnd(Orientation orientation, LayoutContainer container) {
    return orientation == Orientation.HORIZONTAL ? container.getOffsetRight() : container.getOffsetBottom();
  }

  public static Measure getOffsetBegin(Orientation orientation, LayoutContainer container) {
    return orientation == Orientation.HORIZONTAL ? container.getOffsetLeft() : container.getOffsetTop();
  }

  public static Measure getCurrentSize(Orientation orientation, LayoutContainer container) {
    return orientation == Orientation.HORIZONTAL ? container.getCurrentWidth() : container.getCurrentHeight();
  }

  public static void setCurrentSize(Orientation orientation, LayoutBase component, Measure size) {
    if (orientation == Orientation.HORIZONTAL) {
      final Measure width = component.getWidth();
      if (width != null) { // to not override set sizes
        size = width;
      } else {
        final Measure maximumWidth = component.getMaximumWidth();
        if (size.greaterThan(maximumWidth)) {
          size = maximumWidth;
        }
        final Measure minimumWidth = component.getMinimumWidth();
        if (size.lessThan(minimumWidth)) {
          size = minimumWidth;
        }
      }
      component.setCurrentWidth(size);
    } else {
      final Measure height = component.getHeight();
      if (height != null) { // to not override set sizes
        size = height;
      } else {
        final Measure maximumHeight = component.getMaximumHeight();
        if (size.greaterThan(maximumHeight)) {
          size = maximumHeight;
        }
        final Measure minimumHeight = component.getMinimumHeight();
        if (size.lessThan(minimumHeight)) {
          size = minimumHeight;
        }
      }
      component.setCurrentHeight(size);
    }
  }
}
