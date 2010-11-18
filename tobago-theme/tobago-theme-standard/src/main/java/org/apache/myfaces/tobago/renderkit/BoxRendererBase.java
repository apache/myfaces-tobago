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

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.UIBox;
import org.apache.myfaces.tobago.component.UIMenuBar;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.layout.Measure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public abstract class BoxRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(BoxRendererBase.class);

  public boolean getRendersChildren() {
    return true;
  }

  @Override
  public Measure getBorderTop(FacesContext facesContext, Configurable component) {

    Measure borderTop = super.getBorderTop(facesContext, component);
    if (getMenuBarFacet((UIComponent) component) != null) {
      borderTop = borderTop.add(19); // todo: get via theme config
    }
    return borderTop;
  }

  @Override
  public Measure getMinimumHeight(FacesContext facesContext, Configurable component) {
    if (component instanceof UIBox && ((UIBox) component).isCollapsed()) {
      return getBorderTop(facesContext, component);
    }
    return super.getMinimumHeight(facesContext, component);
  }

  @Override
  public Measure getMaximumHeight(FacesContext facesContext, Configurable component) {
    if (component instanceof UIBox && ((UIBox) component).isCollapsed()) {
      return getBorderTop(facesContext, component);
    }
    return super.getMaximumHeight(facesContext, component);
  }

  protected UIMenuBar getMenuBarFacet(UIComponent component) {
    return (UIMenuBar) component.getFacet(Facets.MENUBAR);
  }

  @Override
  public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
    if (component instanceof UIBox && ((UIBox) component).isCollapsed()) {
      return;
    }
    super.encodeChildren(facesContext, component);
  }
}
