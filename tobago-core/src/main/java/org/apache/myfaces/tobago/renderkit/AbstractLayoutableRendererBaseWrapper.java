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


import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.Measure;

import javax.faces.context.FacesContext;

public abstract class AbstractLayoutableRendererBaseWrapper extends AbstractRendererBaseWrapper
    implements LayoutComponentRenderer {

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

  public Measure getOffsetLeft(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getOffsetLeft(facesContext, component);
  }

  public Measure getOffsetRight(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getOffsetRight(facesContext, component);
  }

  public Measure getOffsetTop(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getOffsetTop(facesContext, component);
  }

  public Measure getOffsetBottom(FacesContext facesContext, Configurable component) {
    return ((LayoutComponentRenderer) getRenderer(facesContext)).getOffsetBottom(facesContext, component);
  }
}
