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

package org.apache.myfaces.tobago.component;

import jakarta.faces.context.FacesContext;
import org.apache.myfaces.tobago.renderkit.html.HtmlElements;

import java.util.Map;

public interface SupportsAutoSpacing {
  /**
   * use {@link #getAutoSpacing(FacesContext)} to get the correct default value.
   * return null if autoSpacing is not set manually
   */
  Boolean getAutoSpacing();

  default boolean getAutoSpacing(final FacesContext facesContext) {
    Boolean autoSpacing = getAutoSpacing();
    if (autoSpacing != null) {
      return autoSpacing;
    } else {
      final Map<Object, Object> attributes = facesContext.getAttributes();
      return attributes.get(HtmlElements.TOBAGO_HEADER) == null
          && attributes.get(HtmlElements.TOBAGO_FOOTER) == null
          && attributes.get(HtmlElements.TOBAGO_BAR) == null
          && attributes.get(HtmlElements.TOBAGO_SHEET) == null
          && attributes.get(HtmlElements.TOBAGO_TREE) == null
          && attributes.get(HtmlElements.TOBAGO_LINKS) == null
          && attributes.get(HtmlElements.TOBAGO_BUTTONS) == null
          && attributes.get(HtmlElements.TOBAGO_POPOVER) == null
          && attributes.get(Facets.before) == null
          && attributes.get(Facets.after) == null
          && attributes.get(Facets.label) == null
          && attributes.get(Facets.bar) == null
          && attributes.get(Facets.footer) == null;
    }
  }
}
