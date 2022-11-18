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

import org.apache.myfaces.tobago.internal.component.AbstractUISelectOneBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.faces.context.FacesContext;

import java.lang.invoke.MethodHandles;

public abstract class SelectOneRendererBase<T extends AbstractUISelectOneBase> extends MessageLayoutRendererBase<T> {

  private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  @Override
  protected boolean isOutputOnly(T component) {
    return component.isDisabled() || component.isReadonly();
  }

  @Override
  public void decodeInternal(final FacesContext facesContext, final T component) {
    if (isOutputOnly(component)) {
      return;
    }

    final String clientId = component.getClientId(facesContext);
    final Object newValue =
        facesContext.getExternalContext().getRequestParameterMap().get(clientId);
    LOG.debug("decode: key='{}' value='{}'", clientId, newValue);
    component.setSubmittedValue(newValue);

    decodeClientBehaviors(facesContext, component);
  }

  protected String getDecodingId(final FacesContext facesContext, final T component) {
    return component.getClientId(facesContext);
  }

}
