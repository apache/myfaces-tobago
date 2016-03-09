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

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import java.io.IOException;

public class EncodeAjaxCallback implements TobagoCallback {

  @Override
  public void invokeContextCallback(final FacesContext facesContext, final UIComponent component) {
    try {
       final UIComponent reload = ComponentUtils.getFacet(component, Facets.reload);
       if (reload != null && reload.isRendered()) {
         final Boolean immediate = ComponentUtils.getBooleanAttribute(reload, Attributes.immediate);
         if (!immediate) {
           final Boolean update = ComponentUtils.getBooleanAttribute(reload, Attributes.update);
           if (!update) {
             return;
           }
         }
      }
      component.encodeAll(facesContext);
    } catch (final IOException e) {
      throw new FacesException(e);
    }
  }
  
  @Override
  public PhaseId getPhaseId() {
      return PhaseId.RENDER_RESPONSE;
  }
  
}
