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

package org.apache.myfaces.tobago.util;


import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.util.FacesContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletResponse;

public class ApplyRequestValuesCallback implements TobagoCallback {

  @SuppressWarnings("UnusedDeclaration")
  private static final Logger LOG = LoggerFactory.getLogger(ApplyRequestValuesCallback.class);

  public void invokeContextCallback(final FacesContext context, final UIComponent component) {
    if (FacesContextUtils.isAjax(context)) {
      final String ajaxId = FacesContextUtils.getAjaxComponentId(context);
      final UIComponent reload = component.getFacet(Facets.RELOAD);
      if (ajaxId != null && ajaxId.equals(component.getClientId(context)) && reload != null && reload.isRendered()
          && ajaxId.equals(FacesContextUtils.getActionId(context))) {
        final boolean immediate = ComponentUtils.getBooleanAttribute(reload, Attributes.immediate);
        if (immediate) {
          final boolean update = ComponentUtils.getBooleanAttribute(reload, Attributes.update);
          if (!update) {
            final Object response = context.getExternalContext().getResponse();
            if (response instanceof HttpServletResponse) {
              ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else if (response instanceof PortletResponse) {
              // TBD: Portlet: what is to do here?
            }
            context.responseComplete();
            return;
          }
        }
      }
    }
    component.processDecodes(context);
  }

  public PhaseId getPhaseId() {
    return PhaseId.APPLY_REQUEST_VALUES;
  }
}
