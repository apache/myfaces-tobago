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

package org.apache.myfaces.tobago.renderkit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.tobago.component.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.util.Map;

public class InputRendererBase extends LayoutableRendererBase {

  private static final Log LOG = LogFactory.getLog(InputRendererBase.class);

  public void decode(FacesContext context, UIComponent component) {
    UIInput uiInput;
    if (component instanceof UIInput) {
      uiInput = (UIInput) component;
    } else {
      return; // no decoding required
    }

    if (ComponentUtil.isOutputOnly(component)) {
      return;
    }

    String clientId = component.getClientId(context);

    Map requestParameterMap = context.getExternalContext()
        .getRequestParameterMap();
    if (requestParameterMap.containsKey(clientId)) {
      if (LOG.isDebugEnabled()) {
        LOG.debug("clientId = '" + clientId + "'");
        LOG.debug("requestParameterMap.get(clientId) = '"
            + requestParameterMap.get(clientId) + "'");
        LOG.debug("requestParameterMap.get(clientId).getClass().getName() = '"
            + requestParameterMap.get(clientId).getClass().getName() + "'");
      }
      String newValue = (String) requestParameterMap.get(clientId);
      uiInput.setSubmittedValue(newValue);
    }
  }

  public int getLabelWidth(FacesContext facesContext, UIComponent component) {
    return getConfiguredValue(facesContext, component, "labelWidth");
  }

}

