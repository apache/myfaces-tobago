package org.apache.myfaces.tobago.internal.lifecycle;

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

import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.internal.ajax.AjaxInternalUtils;
import org.apache.myfaces.tobago.internal.ajax.AjaxResponseRenderer;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.IOException;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 * <p/>
 * render response phase (JSF Spec 2.2.6)
 */
class RenderResponseExecutor implements PhaseExecutor {

  private AjaxResponseRenderer ajaxResponseRenderer;


  public RenderResponseExecutor() {
    this.ajaxResponseRenderer = new AjaxResponseRenderer();
  }

  public boolean execute(FacesContext facesContext) {
    if (AjaxInternalUtils.getAjaxComponents(facesContext) != null) {
      try {
        if (facesContext instanceof TobagoFacesContext) {
          ((TobagoFacesContext) facesContext).setAjax(true);
        }
        ajaxResponseRenderer.renderResponse(facesContext);
      } catch (IOException e) {
        throw new FacesException(e.getMessage(), e);
      }
    } else {
      Application application = facesContext.getApplication();
      ViewHandler viewHandler = application.getViewHandler();

      try {
        viewHandler.renderView(facesContext, facesContext.getViewRoot());
      } catch (IOException e) {
        throw new FacesException(e.getMessage(), e);
      }
    }
    return false;
  }

  public PhaseId getPhase() {
    return PhaseId.RENDER_RESPONSE;
  }
}
