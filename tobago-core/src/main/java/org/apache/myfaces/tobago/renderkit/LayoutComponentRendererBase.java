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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.context.FacesContext;

public abstract class LayoutComponentRendererBase extends RendererBase implements LayoutComponentRenderer {

  public Measure getCustomMeasure(final FacesContext facesContext, final Configurable component, final String name) {
    return getResourceManager().getThemeMeasure(facesContext, component, name);
  }

  public Measure getWidth(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.WIDTH);
  }

  public Measure getHeight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.HEIGHT);
  }

  public Measure getMinimumWidth(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MINIMUM_WIDTH);
  }

  public Measure getMinimumHeight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MINIMUM_HEIGHT);
  }

  public Measure getPreferredWidth(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PREFERRED_WIDTH);
  }

  public Measure getPreferredHeight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PREFERRED_HEIGHT);
  }

  public Measure getMaximumWidth(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MAXIMUM_WIDTH);
  }

  public Measure getMaximumHeight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MAXIMUM_HEIGHT);
  }

  public Measure getMarginLeft(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_LEFT);
  }

  public Measure getMarginRight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_RIGHT);
  }

  public Measure getMarginTop(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_TOP);
  }

  public Measure getMarginBottom(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_BOTTOM);
  }

  public Measure getBorderLeft(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_LEFT);
  }

  public Measure getBorderRight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_RIGHT);
  }

  public Measure getBorderTop(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_TOP);
  }

  public Measure getBorderBottom(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_BOTTOM);
  }

  public Measure getPaddingLeft(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_LEFT);
  }

  public Measure getPaddingRight(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_RIGHT);
  }

  public Measure getPaddingTop(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_TOP);
  }

  public Measure getPaddingBottom(final FacesContext facesContext, final Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_BOTTOM);
  }

  public Measure getVerticalScrollbarWeight(final FacesContext facesContext, final Configurable component) {
    final ClientProperties clientProperties = ClientProperties.getInstance(facesContext);
    final Measure weight = clientProperties.getVerticalScrollbarWeight();
    if (weight != null) {
      return weight;
    } else { // default
      return getResourceManager().getThemeMeasure(facesContext, component, "verticalScrollbarWeight");
    }
  }
}
