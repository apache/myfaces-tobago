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


import org.apache.myfaces.tobago.internal.ajax.AjaxComponent;
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
      prepareRendererAll(facesContext, component);
      ((AjaxComponent) component).encodeAjax(facesContext);
    } catch (IOException e) {
      throw new FacesException(e);
    }
  }

  public PhaseId getPhaseId() {
    return PhaseId.RENDER_RESPONSE;
  }

  // TODO merge with RenderUtil.prepareRendererAll
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
