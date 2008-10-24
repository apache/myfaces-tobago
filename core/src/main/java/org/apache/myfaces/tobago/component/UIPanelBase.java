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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_IMMEDIATE;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_UPDATE;
import org.apache.myfaces.tobago.ajax.api.AjaxComponent;
import org.apache.myfaces.tobago.ajax.api.AjaxUtils;
import org.apache.myfaces.tobago.compat.FacesUtils;
import org.apache.myfaces.tobago.compat.InvokeOnComponent;
import org.apache.myfaces.tobago.context.TobagoFacesContext;
import org.apache.myfaces.tobago.util.ComponentUtil;

import javax.faces.FacesException;
import javax.faces.component.ContextCallback;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * User: weber
 * Date: Feb 28, 2005
 * Time: 3:05:19 PM
 */
public class UIPanelBase extends javax.faces.component.UIPanel
    implements AjaxComponent, InvokeOnComponent {

  public void encodeChildren(FacesContext facesContext) throws IOException {
    if (isRendered()) {
      UILayout.getLayout(this).encodeChildrenOfComponent(facesContext, this);
    }
  }

  public void processDecodes(FacesContext context) {
    if (context instanceof TobagoFacesContext && ((TobagoFacesContext) context).isAjax()) {

      final String ajaxId = ((TobagoFacesContext) context).getAjaxComponentId();
      UIComponent reload = getFacet(Facets.RELOAD);
      if (ajaxId != null && ajaxId.equals(getClientId(context)) && reload != null && reload.isRendered()
          && ajaxId.equals(ComponentUtil.findPage(context, this).getActionId())) {
        Boolean immediate = (Boolean) reload.getAttributes().get(ATTR_IMMEDIATE);
        if (immediate != null && immediate) {
          Boolean update = (Boolean) reload.getAttributes().get(ATTR_UPDATE);
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
    super.processDecodes(context);
  }

  public void encodeAjax(FacesContext facesContext) throws IOException {
    UIComponent reload = getFacet(Facets.RELOAD);
    if (reload != null && reload.isRendered()) {
      Boolean immediate = (Boolean) reload.getAttributes().get(ATTR_IMMEDIATE);
      if (immediate != null && !immediate) {
        Boolean update = (Boolean) reload.getAttributes().get(ATTR_UPDATE);
        if (update != null && !update) {
          return;
        }
      }
    }
    AjaxUtils.encodeAjaxComponent(facesContext, this);
  }

  public boolean invokeOnComponent(FacesContext context, String clientId, ContextCallback callback)
     throws FacesException {
    return FacesUtils.invokeOnComponent(context, this, clientId, callback);
  }
}
