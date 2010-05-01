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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.context.TobagoFacesContext;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletResponse;

public class ApplyRequestValuesCallback implements TobagoCallback {

  @SuppressWarnings("UnusedDeclaration")
  private static final Logger LOG = LoggerFactory.getLogger(ApplyRequestValuesCallback.class);

  public void invokeContextCallback(FacesContext context, UIComponent component) {
    if (context instanceof TobagoFacesContext && ((TobagoFacesContext) context).isAjax()) {
      final String ajaxId = ((TobagoFacesContext) context).getAjaxComponentId();
      UIComponent reload = component.getFacet(Facets.RELOAD);
      if (ajaxId != null && ajaxId.equals(component.getClientId(context)) && reload != null && reload.isRendered()
          && ajaxId.equals(ComponentUtils.findPage(context, component).getActionId())) {
        Boolean immediate = (Boolean) reload.getAttributes().get(Attributes.IMMEDIATE);
        if (immediate != null && immediate) {
          Boolean update = (Boolean) reload.getAttributes().get(Attributes.UPDATE);
          if (update != null && !update) {
            if (context.getExternalContext().getResponse() instanceof HttpServletResponse) {
              ((HttpServletResponse) context.getExternalContext().getResponse())
                  .setStatus(HttpServletResponse.SC_NOT_MODIFIED);
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
