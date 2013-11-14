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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.util.Map;

public class InputRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(InputRendererBase.class);

  public void decode(FacesContext context, UIComponent component) {
    final UIInput uiInput;
    if (component instanceof UIInput) {
      uiInput = (UIInput) component;
    } else {
      return; // no decoding required
    }

    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }

    final String clientId = component.getClientId(context);

    final Map<String, String> requestParameterMap = context.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(clientId)) {
      final String newValue = requestParameterMap.get(clientId);
      if (LOG.isDebugEnabled()) {
        final boolean password = ComponentUtils.getBooleanAttribute(component, Attributes.PASSWORD);
        LOG.debug("clientId = '" + clientId + "'");
        LOG.debug("requestParameterMap.get(clientId) = '"
            + (password ? StringUtils.repeat("*", newValue.length()) : newValue) + "'");
      }
      uiInput.setSubmittedValue(newValue);
    }
  }
}
