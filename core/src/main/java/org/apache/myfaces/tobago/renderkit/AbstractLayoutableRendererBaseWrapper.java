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


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.awt.Dimension;
import java.io.IOException;

public abstract class AbstractLayoutableRendererBaseWrapper extends AbstractRendererBaseWrapper
    implements LayoutableRenderer {

  public final int getHeaderHeight(FacesContext facesContext, UIComponent component) {
    return getLayoutableRenderer(facesContext).getHeaderHeight(facesContext, component);
  }

  public final int getPaddingWidth(FacesContext facesContext, UIComponent component) {
    return getLayoutableRenderer(facesContext).getPaddingWidth(facesContext, component);
  }

  public final int getPaddingHeight(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getPaddingHeight(facesContext, component);
  }

  public final int getComponentExtraWidth(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getComponentExtraWidth(facesContext, component);
  }

  public final int getComponentExtraHeight(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getComponentExtraHeight(facesContext, component);
  }

  public final int getMinimumWidth(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getMinimumWidth(facesContext, component);
  }

  public final int getMinimumHeight(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getMinimumHeight(facesContext, component);
  }

  public final Dimension getMinimumSize(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getMinimumSize(facesContext, component);
  }

  public final int getFixedWidth(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getFixedWidth(facesContext, component);
  }

  public final int getFixedHeight(FacesContext facesContext, UIComponent component) {
     return getLayoutableRenderer(facesContext).getFixedHeight(facesContext, component);
  }

  public final void layoutBegin(FacesContext facesContext, UIComponent component) throws IOException {
    getLayoutableRenderer(facesContext).layoutBegin(facesContext, component);
  }

  public final void layoutEnd(FacesContext facesContext, UIComponent component) throws IOException {
    getLayoutableRenderer(facesContext).layoutEnd(facesContext, component);
  }

  protected final LayoutableRenderer getLayoutableRenderer(FacesContext facesContext) {
    return (LayoutableRenderer) getRenderer(facesContext);

  }

}
