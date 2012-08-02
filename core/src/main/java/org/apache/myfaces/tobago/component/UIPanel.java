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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.ajax.api.AjaxPhaseListener;
import static org.apache.myfaces.tobago.TobagoConstants.FACET_RELOAD;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

/**
 * User: idus
 * Date: 12.03.2007
 * Time: 22:32:13
 */
public class UIPanel extends UIPanelBase implements SupportsMarkup {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Panel";

  private String[] markup;

  public String[] getMarkup() {
    if (markup != null) {
      return markup;
    }
    return ComponentUtil.getMarkupBinding(getFacesContext(), this);
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    markup = (String[]) values[1];
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = markup;
    return values;
  }

  public void processDecodes(FacesContext context) {
    final String ajaxId = (String) context.getExternalContext()
        .getRequestParameterMap().get(AjaxPhaseListener.AJAX_COMPONENT_ID);
    if (ajaxId !=null && ajaxId.equals(getClientId(context))) {
      if (getFacet(FACET_RELOAD) != null && getFacet(FACET_RELOAD) instanceof UIReload
          && getFacet(FACET_RELOAD).isRendered()
          && ((UIReload) getFacet(FACET_RELOAD)).isImmediate()
          && ajaxId.equals(ComponentUtil.findPage(context, this).getActionId())) {
        UIReload reload = (UIReload) getFacet(FACET_RELOAD);
        if (!reload.getUpdate()) {
          if (context.getExternalContext().getResponse() instanceof HttpServletResponse) {
             ((HttpServletResponse) context.getExternalContext().getResponse())
                 .setStatus(HttpServletResponse.SC_NOT_MODIFIED);
          }
          context.responseComplete();
          return;
        }
      }
    }
    super.processDecodes(context);
  }

}
