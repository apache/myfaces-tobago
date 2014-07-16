/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.myfaces.tobago.internal.layout;

import org.apache.myfaces.tobago.layout.LayoutBase;
import org.apache.myfaces.tobago.layout.LayoutBox;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.layout.Orientation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public final class LayoutUtils {

  private static final Logger LOG = LoggerFactory.getLogger(LayoutUtils.class);

  private static final Pattern TOKEN_PATTERN = Pattern.compile("^(\\d*px|\\d*\\*|\\d*%|auto|fixed)$");

  private LayoutUtils() {
    // to prevent instantiation
  }

  public static boolean checkTokens(final String columns) {
    final StringTokenizer st = new StringTokenizer(columns, ";");
    while (st.hasMoreTokens()) {
      final String token = st.nextToken();
      if (!TOKEN_PATTERN.matcher(token).matches()) {
        return false;
      }
    }
    return true;
  }

  public static List<LayoutComponent> findLayoutChildren(final LayoutContainer container) {
    final List<LayoutComponent> result = new ArrayList<LayoutComponent>();
    addLayoutChildren((UIComponent) container, result);
    return result;
  }

  private static void addLayoutChildren(final UIComponent component, final List<LayoutComponent> result) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof LayoutComponent) {
        result.add((LayoutComponent) child);
      } else {
        // Child seems to be transparent for layout, like UIForm. 
        // So we try to add the inner components.
        addLayoutChildren(child, result);
      }
    }

    final UIComponent child = component.getFacet(UIComponent.COMPOSITE_FACET_NAME);
    if (child instanceof LayoutComponent) {
      result.add((LayoutComponent) child);
    } else if (child != null) {
      // Child seems to be transparent for layout, like UIForm.
      // So we try to add the inner components.
      addLayoutChildren(child, result);
    }
  }

  public static Measure getBorderEnd(final Orientation orientation, final LayoutBox container) {
    return orientation == Orientation.HORIZONTAL ? container.getBorderRight() : container.getBorderBottom();
  }

  public static Measure getBorderBegin(final Orientation orientation, final LayoutBox container) {
    return orientation == Orientation.HORIZONTAL ? container.getBorderLeft() : container.getBorderTop();
  }

  public static Measure getPaddingEnd(final Orientation orientation, final LayoutBox container) {
    return orientation == Orientation.HORIZONTAL ? container.getPaddingRight() : container.getPaddingBottom();
  }

  public static Measure getPaddingBegin(final Orientation orientation, final LayoutBox container) {
    return orientation == Orientation.HORIZONTAL ? container.getPaddingLeft() : container.getPaddingTop();
  }

  public static Measure getCurrentSize(final Orientation orientation, final LayoutBase component) {
    return orientation == Orientation.HORIZONTAL ? component.getCurrentWidth() : component.getCurrentHeight();
  }

  public static void setCurrentSize(final Orientation orientation, final LayoutBase component, Measure size) {
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
