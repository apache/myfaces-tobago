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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.internal.layout.LayoutUtils;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUIPanel extends javax.faces.component.UIPanel
    implements OnComponentPopulated, LayoutContainer, LayoutComponent {

  @Override
  public void encodeBegin(final FacesContext facesContext) throws IOException {

    super.encodeBegin(facesContext);

    final AbstractUILayoutBase layoutManager = (AbstractUILayoutBase) getLayoutManager();
    if (layoutManager != null) {
      layoutManager.encodeBegin(facesContext);
    }
  }

  @Override
  public void encodeChildren(final FacesContext facesContext) throws IOException {

    final AbstractUILayoutBase layoutManager = (AbstractUILayoutBase) getLayoutManager();
    if (layoutManager != null) {
      layoutManager.encodeChildren(facesContext);
    } else {
      super.encodeChildren(facesContext);
    }
  }

  @Override
  public void encodeEnd(final FacesContext facesContext) throws IOException {

    final AbstractUILayoutBase layoutManager = (AbstractUILayoutBase) getLayoutManager();
    if (layoutManager != null) {
      layoutManager.encodeEnd(facesContext);
    }

    super.encodeEnd(facesContext);
  }

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
  }

  public List<LayoutComponent> getComponents() {
    return LayoutUtils.findLayoutChildren(this);
  }

  public LayoutManager getLayoutManager() {

    final UIComponent base;

    final UIComponent compositeFacet = getFacet(COMPOSITE_FACET_NAME);
    if (compositeFacet != null) {
      base = compositeFacet.getChildren().get(0);
    } else {
      base = this;
    }

    final UIComponent layoutFacet = base.getFacet(Facets.LAYOUT);
    if (layoutFacet != null) {
      if (layoutFacet instanceof LayoutManager) {
        return (LayoutManager) layoutFacet;
      } else {
        return (LayoutManager) ComponentUtils.findChild(layoutFacet, AbstractUILayoutBase.class);
      }
    } else {
/*
      final LayoutManager layoutManager = CreateComponentUtils.createAndInitLayout(
          FacesContext.getCurrentInstance(), ComponentTypes.GRID_LAYOUT, RendererTypes.GRID_LAYOUT, base.getParent());
      base.getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
      return layoutManager;
*/
      return null;
    }
  }

  public void setLayoutManager(final LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (AbstractUILayoutBase) layoutManager);
  }

  public boolean isLayoutChildren() {
    return isRendered();
  }
}
