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

import org.apache.myfaces.tobago.application.ProjectStage;
import org.apache.myfaces.tobago.config.TobagoConfig;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.Set;


public class DebugUtils {

  private DebugUtils() {
    // to prevent instantiation
  }

  public static String toString(UIComponent component, int offset) {
    return toString(component, offset, false);
  }

  public static String toString(UIComponent component, int offset, boolean asFacet) {
    StringBuilder result = new StringBuilder();
    if (component == null) {
      result.append("null");
    } else {
      result.append('\n');
      if (!asFacet) {
        result.append(spaces(offset));
        result.append(toString(component));
      }
      Map facets = component.getFacets();
      if (facets.size() > 0) {
        for (Map.Entry<String, UIComponent> entry : (Set<Map.Entry<String, UIComponent>>) facets.entrySet()) {
          UIComponent facet = entry.getValue();
          result.append('\n');
          result.append(spaces(offset + 1));
          result.append('\"');
          result.append(entry.getKey());
          result.append("\" = ");
          result.append(toString(facet));
          result.append(toString(facet, offset + 1, true));
        }
      }
      for (Object o : component.getChildren()) {
        result.append(toString((UIComponent) o, offset + 1, false));
      }
    }
    return result.toString();
  }

  public static String toString(UIComponent component) {
    StringBuilder buf = new StringBuilder(component.getClass().getName());
    buf.append('@');
    buf.append(Integer.toHexString(component.hashCode()));
    buf.append(" ");
    buf.append(component.getRendererType());
    buf.append(" ");
    if (component instanceof javax.faces.component.UIViewRoot) {
      buf.append(((javax.faces.component.UIViewRoot) component).getViewId());
    } else {
      buf.append(component.getId());
      buf.append(" ");
      buf.append(component.getClientId(FacesContext.getCurrentInstance()));
    }
    return buf.toString();
  }

  public static String spaces(int n) {
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < n; i++) {
      buffer.append("  ");
    }
    return buffer.toString();
  }

  public static void addDevelopmentMessage(FacesContext facesContext, String message) {
    if (TobagoConfig.getInstance(FacesContext.getCurrentInstance()).getProjectStage() == ProjectStage.Development) {
      facesContext.addMessage(null, new FacesMessage(message));
    }
  }

}
