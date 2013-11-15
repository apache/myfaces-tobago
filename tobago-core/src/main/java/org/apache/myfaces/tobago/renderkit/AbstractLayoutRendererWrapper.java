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


public abstract class AbstractLayoutRendererWrapper
    extends AbstractLayoutableRendererBaseWrapper implements LayoutRenderer, SpacingValues, MarginValues {

  public Measure getColumnSpacing(final FacesContext facesContext, final Configurable component) {
    return ((SpacingValues) getRenderer(facesContext)).getColumnSpacing(facesContext, component);
  }

  public Measure getRowSpacing(final FacesContext facesContext, final Configurable component) {
    return ((SpacingValues) getRenderer(facesContext)).getRowSpacing(facesContext, component);
  }

  public Measure getMarginLeft(final FacesContext facesContext, final Configurable component) {
    return ((MarginValues) getRenderer(facesContext)).getMarginLeft(facesContext, component);
  }

  public Measure getMarginRight(final FacesContext facesContext, final Configurable component) {
    return ((MarginValues) getRenderer(facesContext)).getMarginRight(facesContext, component);
  }

  public Measure getMarginTop(final FacesContext facesContext, final Configurable component) {
    return ((MarginValues) getRenderer(facesContext)).getMarginTop(facesContext, component);
  }

  public Measure getMarginBottom(final FacesContext facesContext, final Configurable component) {
    return ((MarginValues) getRenderer(facesContext)).getMarginBottom(facesContext, component);
  }
}
