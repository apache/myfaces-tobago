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

package org.apache.myfaces.tobago.internal.renderkit.renderer;

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.internal.util.StringUtils;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;
import java.util.Map;

public abstract class DecodingInputRendererBase<T extends UIComponent> extends RendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    if (!(component instanceof EditableValueHolder)) {
      return; // no decoding required
    }

    if (isOutputOnly(component)) {
      return; // no decoding required
    }

    final String clientId = component.getClientId(facesContext);

    final Map<String, String> requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
    if (requestParameterMap.containsKey(clientId)) {
      final String newValue = requestParameterMap.get(clientId);
      if (LOG.isDebugEnabled()) {
        final boolean password = ComponentUtils.getBooleanAttribute(component, Attributes.password);
        LOG.debug("clientId='{}'", clientId);
        LOG.debug("requestParameterMap.get(clientId)='{}'", StringUtils.toConfidentialString(newValue, password));
      }

      setSubmittedValue(facesContext, (EditableValueHolder) component, newValue);
    }

    decodeClientBehaviors(facesContext, component);
  }

  protected abstract boolean isOutputOnly(T component);

  protected void setSubmittedValue(
      final FacesContext facesContext, final EditableValueHolder component, final String newValue) {
    component.setSubmittedValue(newValue);
  }

}
