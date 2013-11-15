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

package org.apache.myfaces.tobago.internal.lifecycle;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implements the lifecycle as described in Spec. 1.0 PFD Chapter 2
 * <p/>
 * render response phase (JSF Spec 2.2.6)
 *
 * Not longer needed.
 *
 * @deprecated since Tobago 2.0.0
 */
@Deprecated
class RenderResponseExecutor implements PhaseExecutor {

  public boolean execute(final FacesContext facesContext) {
    final Application application = facesContext.getApplication();
    final ViewHandler viewHandler = application.getViewHandler();

    try {
      final UIViewRoot viewRoot = facesContext.getViewRoot();
      if (viewRoot.getViewId() != null) {
        viewHandler.renderView(facesContext, viewRoot);
      } else {
        final Object respObj = facesContext.getExternalContext().getResponse();
        if (respObj instanceof HttpServletResponse) {
            final HttpServletResponse respHttp = (HttpServletResponse) respObj;
            respHttp.sendError(HttpServletResponse.SC_NOT_FOUND);
            facesContext.responseComplete();
        }
      }
    } catch (final IOException e) {
      throw new FacesException(e.getMessage(), e);
    }
    return false;
  }

  public PhaseId getPhase() {
    return PhaseId.RENDER_RESPONSE;
  }
}
