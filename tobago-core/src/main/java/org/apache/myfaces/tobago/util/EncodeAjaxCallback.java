package org.apache.myfaces.tobago.util;

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
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.layout.LayoutContext;
import org.apache.myfaces.tobago.layout.LayoutContainer;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.IOException;
import java.util.Iterator;

public class EncodeAjaxCallback implements TobagoCallback {

  public void invokeContextCallback(FacesContext facesContext, UIComponent component) {
    try {
       UIComponent reload = component.getFacet(Facets.RELOAD);
       if (reload != null && reload.isRendered()) {
         Boolean immediate = (Boolean) reload.getAttributes().get(Attributes.IMMEDIATE);
         if (immediate != null && !immediate) {
           Boolean update = (Boolean) reload.getAttributes().get(Attributes.UPDATE);
           if (update != null && !update) {
             return;
           }
         }
      }
      if (component instanceof LayoutContainer) {
        new LayoutContext((LayoutContainer) component).layout();
      }
      prepareRendererAll(facesContext, component);
      encodeAll(facesContext, component);
    } catch (IOException e) {
      throw new FacesException(e);
    }
  }
  
  public PhaseId getPhaseId() {
      return PhaseId.RENDER_RESPONSE;
  }
  

  // TODO replace with component.encodeAll after removing jsf 1.1 support
  public static void encodeAll(FacesContext facesContext, UIComponent component) throws IOException {
     if (component.isRendered()) {
      component.encodeBegin(facesContext);
      if (component.getRendersChildren()) {
        component.encodeChildren(facesContext);
      } else {
        for (Object o : component.getChildren()) {
          UIComponent kid = (UIComponent) o;
          encodeAll(facesContext, kid);
        }
      }
      component.encodeEnd(facesContext);
    }
  }

  // TODO merge with RenderUtils.prepareRendererAll
  public static void prepareRendererAll(FacesContext facesContext, UIComponent component) throws IOException {
    RendererBase renderer = ComponentUtils.getRenderer(facesContext,  component);
    boolean prepareRendersChildren = false;
    if (renderer != null) {
      renderer.prepareRender(facesContext, component);
      prepareRendersChildren = renderer.getPrepareRendersChildren();
    }
    if (prepareRendersChildren) {
      renderer.prepareRendersChildren(facesContext, component);
    } else {
      Iterator it = component.getFacetsAndChildren();
      while (it.hasNext()) {
        UIComponent child = (UIComponent) it.next();
        prepareRendererAll(facesContext, child);
      }
    }
  }
}
