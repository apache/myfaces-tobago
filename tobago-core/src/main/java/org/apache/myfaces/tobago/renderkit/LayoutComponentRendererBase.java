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

  public Measure getOffsetLeft(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.OFFSET_LEFT);
  }

  public Measure getOffsetRight(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.OFFSET_RIGHT);
  }

  public Measure getOffsetTop(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.OFFSET_TOP);
  }

  public Measure getOffsetBottom(FacesContext facesContext, Configurable component) {
    return getResourceManager().getThemeMeasure(facesContext, component, Attributes.OFFSET_BOTTOM);
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
