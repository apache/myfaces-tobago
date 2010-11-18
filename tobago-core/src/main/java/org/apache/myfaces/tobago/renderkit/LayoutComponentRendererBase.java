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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.context.ClientProperties;
import org.apache.myfaces.tobago.layout.Measure;
import org.apache.myfaces.tobago.util.VariableResolverUtils;

import javax.faces.context.FacesContext;

public abstract class LayoutComponentRendererBase extends RendererBase implements LayoutComponentRenderer {

  public Measure getCustomMeasure(FacesContext facesContext, Configurable component, String name) {
    return getResourceManager().getThemeMeasure(facesContext, component, name);
  }

  public Measure getWidth(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.WIDTH);
  }

  public Measure getHeight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.HEIGHT);
  }

  public Measure getMinimumWidth(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MINIMUM_WIDTH);
  }

  public Measure getMinimumHeight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MINIMUM_HEIGHT);
  }

  public Measure getPreferredWidth(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PREFERRED_WIDTH);
  }

  public Measure getPreferredHeight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PREFERRED_HEIGHT);
  }

  public Measure getMaximumWidth(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MAXIMUM_WIDTH);
  }

  public Measure getMaximumHeight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MAXIMUM_HEIGHT);
  }

  public Measure getMarginLeft(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_LEFT);
  }

  public Measure getMarginRight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_RIGHT);
  }

  public Measure getMarginTop(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_TOP);
  }

  public Measure getMarginBottom(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.MARGIN_BOTTOM);
  }

  public Measure getBorderLeft(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_LEFT);
  }

  public Measure getBorderRight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_RIGHT);
  }

  public Measure getBorderTop(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_TOP);
  }

  public Measure getBorderBottom(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.BORDER_BOTTOM);
  }

  public Measure getPaddingLeft(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_LEFT);
  }

  public Measure getPaddingRight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_RIGHT);
  }

  public Measure getPaddingTop(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_TOP);
  }

  public Measure getPaddingBottom(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.PADDING_BOTTOM);
  }

  public Measure getVerticalScrollbarWeight(FacesContext facesContext, Configurable component) {
    final ClientProperties clientProperties = VariableResolverUtils.resolveClientProperties(facesContext);
    final Measure weight = clientProperties.getVerticalScrollbarWeight();
    if (weight != null) {
      return weight;
    } else { // default
      return getResourceManager().getThemeMeasure(facesContext, component, "verticalScrollbarWeight");
    }
  }
}
