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

package org.apache.myfaces.tobago.util;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class DebugUtils {

  private DebugUtils() {
    // to prevent instantiation
  }

  public static String toString(final UIComponent component, final int offset) {
    return toString(component, offset, false, null);
  }

  public static String toString(
      final UIComponent component, final int offset, final boolean asFacet, final Integer childIndex) {
    final StringBuilder result = new StringBuilder();
    if (component == null) {
      result.append("null");
    } else {
      if (!asFacet) {
        result.append(spaces(offset));
        if (childIndex != null) {
          result.append('[');
          result.append(childIndex);
          result.append("] ");
        }
        result.append(toString(component));
      }
      final Map facets = component.getFacets();
      if (facets.size() > 0) {
        for (final Map.Entry<String, UIComponent> entry : (Set<Map.Entry<String, UIComponent>>) facets.entrySet()) {
          final UIComponent facet = entry.getValue();
          result.append(spaces(offset + 1));
          result.append('[');
          result.append(entry.getKey());
          result.append("] ");
          result.append(toString(facet));
          result.append(toString(facet, offset + 1, true, null));
        }
      }
      final List<UIComponent> children = component.getChildren();
      for (int i = 0; i < children.size(); i++) {
        result.append(toString(children.get(i), offset + 1, false, i));
      }
    }
    return result.toString();
  }

  public static String toString(final UIComponent component) {
    final StringBuilder buf = new StringBuilder(component.getClass().getName());
//    buf.append('@');
//    buf.append(Integer.toHexString(component.hashCode()));
    buf.append(" ");
    buf.append(component.getRendererType());
    buf.append(" ");
//      buf.append(component.getId());
//      buf.append(" ");
    buf.append(component.getClientId(FacesContext.getCurrentInstance()));
    if (component instanceof jakarta.faces.component.UIViewRoot) {
      buf.append(" viewId=");
      buf.append(((jakarta.faces.component.UIViewRoot) component).getViewId());
    }
    buf.append(" rendered=");
    buf.append(component.isRendered());
    buf.append('\n');
    return buf.toString();
  }

  public static String spaces(final int n) {
    final StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < n; i++) {
      buffer.append("  ");
    }
    return buffer.toString();
  }

  public static void addDevelopmentMessage(final FacesContext facesContext, final String message) {
    if (facesContext.isProjectStage(ProjectStage.Development)) {
      facesContext.addMessage(null, new FacesMessage(message));
    }
  }

}
