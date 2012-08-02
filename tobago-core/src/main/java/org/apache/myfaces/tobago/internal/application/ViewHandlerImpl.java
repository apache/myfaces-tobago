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

package org.apache.myfaces.tobago.internal.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.portlet.PortletUtils;
import org.apache.myfaces.tobago.util.DebugUtils;
import org.apache.myfaces.tobago.util.RequestUtils;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Locale;

public class ViewHandlerImpl extends ViewHandler {

  private static final Logger LOG = LoggerFactory.getLogger(ViewHandlerImpl.class);

  private ViewHandler base;

  public ViewHandlerImpl(ViewHandler base) {
    if (LOG.isInfoEnabled()) {
      LOG.info("Hiding RI base implementation: " + base);
    }
    this.base = base;
  }

  public Locale calculateLocale(FacesContext facesContext) {
    return base.calculateLocale(facesContext);
  }

  public String calculateRenderKitId(FacesContext facesContext) {
    return base.calculateRenderKitId(facesContext);
  }

  public UIViewRoot createView(FacesContext facesContext, String viewId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("creating new view with viewId:        '{}'", viewId);
    }
    UIViewRoot viewRoot = base.createView(facesContext, viewId);
    // ensure tobago UIViewRoot RI don't create the component via application
    if (!(viewRoot instanceof org.apache.myfaces.tobago.component.UIViewRoot)) {
      UIViewRoot tobagoViewRoot = (UIViewRoot)
          facesContext.getApplication().createComponent(UIViewRoot.COMPONENT_TYPE);
      if (!(tobagoViewRoot instanceof org.apache.myfaces.tobago.component.UIViewRoot)) {
        LOG.warn("Application creating wrong UIViewRoot, forcing Tobago");
        tobagoViewRoot = new org.apache.myfaces.tobago.component.UIViewRoot();
      }
      tobagoViewRoot.setLocale(viewRoot.getLocale());
      tobagoViewRoot.setViewId(viewId);
      tobagoViewRoot.setRenderKitId(viewRoot.getRenderKitId());
      viewRoot = tobagoViewRoot;
    }
    return viewRoot;
  }

  public String getActionURL(FacesContext facesContext, String viewId) {

    if (PortletUtils.isRenderResponse(facesContext)) {
      return PortletUtils.setViewIdForUrl(facesContext, viewId);
    }

    return base.getActionURL(facesContext, viewId);
  }

  public String getResourceURL(FacesContext facesContext, String path) {
    return base.getResourceURL(facesContext, path);
  }

  public void renderView(FacesContext facesContext, UIViewRoot viewRoot)
      throws IOException, FacesException {
    // standard
    base.renderView(facesContext, viewRoot);

    if (LOG.isDebugEnabled()) {
      LOG.debug("VIEW");
      LOG.debug(DebugUtils.toString(facesContext.getViewRoot(), 0));
    }
  }

  public UIViewRoot restoreView(FacesContext facesContext, String viewId) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("restore view with viewId:             '{}'", viewId);
    }
    // this is only needed in the first request, the later will be handled by faces
    // TODO: maybe find a way to make this unneeded
    RequestUtils.ensureEncoding(facesContext);
    UIViewRoot viewRoot = base.restoreView(facesContext, viewId);
    return viewRoot;
  }

  public void writeState(FacesContext facesContext) throws IOException {
    base.writeState(facesContext);
  }

}

