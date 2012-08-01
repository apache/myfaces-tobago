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

  public Measure getCustomMeasure(FacesContext facesContext, Configurable component, String name) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getCustomMeasure(facesContext, component, name);
  }

  public Measure getWidth(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getWidth(facesContext, component);
  }

  public Measure getHeight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getHeight(facesContext, component);
  }

  public Measure getMinimumWidth(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMinimumWidth(facesContext, component);
  }

  public Measure getMinimumHeight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMinimumHeight(facesContext, component);
  }

  public Measure getPreferredWidth(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPreferredWidth(facesContext, component);
  }

  public Measure getPreferredHeight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPreferredHeight(facesContext, component);
  }

  public Measure getMaximumWidth(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMaximumWidth(facesContext, component);
  }

  public Measure getMaximumHeight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMaximumHeight(facesContext, component);
  }

  public Measure getMarginLeft(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginLeft(facesContext, component);
  }

  public Measure getMarginRight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginRight(facesContext, component);
  }

  public Measure getMarginTop(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginTop(facesContext, component);
  }

  public Measure getMarginBottom(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getMarginBottom(facesContext, component);
  }

  public Measure getBorderLeft(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderLeft(facesContext, component);
  }

  public Measure getBorderRight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderRight(facesContext, component);
  }

  public Measure getBorderTop(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderTop(facesContext, component);
  }

  public Measure getBorderBottom(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getBorderBottom(facesContext, component);
  }

  public Measure getPaddingLeft(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingLeft(facesContext, component);
  }

  public Measure getPaddingRight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingRight(facesContext, component);
  }

  public Measure getPaddingTop(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingTop(facesContext, component);
  }

  public Measure getPaddingBottom(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getPaddingBottom(facesContext, component);
  }

  public Measure getVerticalScrollbarWeight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getVerticalScrollbarWeight(facesContext, component);
  }
}
