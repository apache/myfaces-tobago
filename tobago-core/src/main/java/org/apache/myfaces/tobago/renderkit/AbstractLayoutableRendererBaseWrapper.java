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


import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.context.FacesContext;

public abstract class AbstractLayoutableRendererBaseWrapper extends AbstractRendererBaseWrapper
    implements LayoutComponentRenderer {

  public Measure getCustomMeasure(final FacesContext facesContext, final Configurable component, final String name) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getCustomMeasure(facesContext, component, name);
  }

  public Measure getWidth(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getWidth(facesContext, component);
  }

  public Measure getHeight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getHeight(facesContext, component);
  }

  public Measure getMinimumWidth(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMinimumWidth(facesContext, component);
  }

  public Measure getMinimumHeight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMinimumHeight(facesContext, component);
  }

  public Measure getPreferredWidth(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPreferredWidth(facesContext, component);
  }

  public Measure getPreferredHeight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPreferredHeight(facesContext, component);
  }

  public Measure getMaximumWidth(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMaximumWidth(facesContext, component);
  }

  public Measure getMaximumHeight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMaximumHeight(facesContext, component);
  }

  public Measure getMarginLeft(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginLeft(facesContext, component);
  }

  public Measure getMarginRight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginRight(facesContext, component);
  }

  public Measure getMarginTop(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginTop(facesContext, component);
  }

  public Measure getMarginBottom(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginBottom(facesContext, component);
  }

  public Measure getBorderLeft(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderLeft(facesContext, component);
  }

  public Measure getBorderRight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderRight(facesContext, component);
  }

  public Measure getBorderTop(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderTop(facesContext, component);
  }

  public Measure getBorderBottom(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderBottom(facesContext, component);
  }

  public Measure getPaddingLeft(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingLeft(facesContext, component);
  }

  public Measure getPaddingRight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingRight(facesContext, component);
  }

  public Measure getPaddingTop(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingTop(facesContext, component);
  }

  public Measure getPaddingBottom(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingBottom(facesContext, component);
  }

  public Measure getVerticalScrollbarWeight(final FacesContext facesContext, final Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getVerticalScrollbarWeight(facesContext, component);
  }
}
