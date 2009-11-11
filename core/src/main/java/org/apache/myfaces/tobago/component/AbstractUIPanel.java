package org.apache.myfaces.tobago.component;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.layout.LayoutManager;
import org.apache.myfaces.tobago.layout.LayoutUtils;

import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public abstract class AbstractUIPanel extends UIPanelBase
    implements OnComponentPopulated, LayoutContainer, LayoutComponent {

  private static final Log LOG = LogFactory.getLog(AbstractUIPanel.class);

  @Override
  public void encodeBegin(FacesContext facesContext) throws IOException {

    super.encodeBegin(facesContext);
    ((UILayout) getLayoutManager()).encodeBegin(facesContext);
  }

  @Override
  public void encodeChildren(FacesContext facesContext) throws IOException {

    ((UILayout) getLayoutManager()).encodeChildren(facesContext);
  }

  @Override
  public void encodeEnd(FacesContext facesContext) throws IOException {

    ((UILayout) getLayoutManager()).encodeEnd(facesContext);
    super.encodeEnd(facesContext);
  }

  public void onComponentPopulated(FacesContext facesContext) {
    if (getLayoutManager() == null) {
      setLayoutManager(CreateComponentUtils.createAndInitLayout(
          facesContext, ComponentTypes.GRID_LAYOUT, RendererTypes.GRID_LAYOUT));
    }
  }

  public List<LayoutComponent> getComponents() {
    return LayoutUtils.findLayoutChildren(this);
  }

  public LayoutManager getLayoutManager() {
    return (LayoutManager) getFacet(Facets.LAYOUT);
  }

  public void setLayoutManager(LayoutManager layoutManager) {
    getFacets().put(Facets.LAYOUT, (UILayout) layoutManager);
  }
}
