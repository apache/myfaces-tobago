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

package org.apache.myfaces.tobago.mock.faces;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import java.io.IOException;
import java.util.Locale;

public class MockViewHandler extends ViewHandler {

  public Locale calculateLocale(FacesContext facesContext) {
    return null;
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    return RenderKitFactory.HTML_BASIC_RENDER_KIT;
  }

  public UIViewRoot createView(FacesContext facesContext, String viewId) {
    UIViewRoot result = new UIViewRoot();
    result.setViewId(viewId);
          result.setRenderKitId(calculateRenderKitId(facesContext));
    return result;
  }

  public String getActionURL(FacesContext facesContext, String reference) {
    return null;
  }

  public String getResourceURL(FacesContext facesContext, String reference) {
    return null;
  }

  public void renderView(FacesContext facesContext, UIViewRoot uiViewRoot)
      throws IOException, FacesException {
  }

  public UIViewRoot restoreView(FacesContext facesContext, String reference) {
    return null;
  }

  public void writeState(FacesContext facesContext) throws IOException {
  }
}
