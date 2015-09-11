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

import org.apache.myfaces.tobago.internal.component.AbstractUIFlexLayout;
import org.apache.myfaces.tobago.internal.component.AbstractUIFlowLayout;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;

import javax.faces.component.UIComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public final class LayoutUtils {

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

  public static List<UIComponent> findLayoutChildren(final LayoutContainer container) {
    final List<UIComponent> result = new ArrayList<UIComponent>();
    addLayoutChildren((UIComponent) container, result);
    return result;
  }

  private static void addLayoutChildren(final UIComponent component, final List<UIComponent> result) {
    for (final UIComponent child : component.getChildren()) {
      if (child instanceof LayoutComponent
          || child instanceof AbstractUIFlowLayout || child instanceof AbstractUIFlexLayout) {
        result.add(child);
      } else {
        // Child seems to be transparent for layout, like UIForm. 
        // So we try to add the inner components.
        addLayoutChildren(child, result);
      }
    }

    final UIComponent child = component.getFacet(UIComponent.COMPOSITE_FACET_NAME);
    if (child instanceof LayoutComponent
        || child instanceof AbstractUIFlowLayout || child instanceof AbstractUIFlexLayout) {
      result.add(child);
    } else if (child != null) {
      // Child seems to be transparent for layout, like UIForm.
      // So we try to add the inner components.
      addLayoutChildren(child, result);
    }
  }
}
