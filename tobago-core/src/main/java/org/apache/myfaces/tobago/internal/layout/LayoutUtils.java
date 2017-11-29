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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * @deprecated since 4.0.0
 */
@Deprecated
public final class LayoutUtils {

  private static final Pattern TOKEN_PATTERN = Pattern.compile("^(\\d*px|\\d*\\*|\\d*%|auto|fixed)$");

  private LayoutUtils() {
    // to prevent instantiation
  }

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
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

  /**
   * @deprecated since 4.0.0
   */
  @Deprecated
  public static List<UIComponent> findLayoutChildren(final UIComponent container) {
    return ComponentUtils.findLayoutChildren(container);
  }

  /**
   * This is needed, when the layout manager is set by a facet.
   * This should be only used by the classic &lt;tc:gridLayout&gt;
   * @since 3.0.0
   * @deprecated since 3.0.0
   */
  @Deprecated
  public static UIComponent getLayoutManager(final UIComponent component) {
    final UIComponent base;

    final UIComponent compositeFacet = component.getFacet(UIComponent.COMPOSITE_FACET_NAME);
    if (compositeFacet != null) {
      base = compositeFacet.getChildren().get(0);
    } else {
      base = component;
    }

    final UIComponent layoutFacet = ComponentUtils.getFacet(base, Facets.layout);
    if (layoutFacet != null) {
      return layoutFacet;
    } else {
      return null;
    }
  }
}
