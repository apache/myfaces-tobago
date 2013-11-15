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

package org.apache.myfaces.tobago.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.internal.component.AbstractUIPopup;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

public class PopupFacetActionListener extends AbstractPopupActionListener {

  private static final Logger LOG = LoggerFactory.getLogger(PopupActionListener.class);

  @Override
  protected AbstractUIPopup getPopup(final ActionEvent actionEvent) {
    final UIComponent component = actionEvent.getComponent().getFacet(Facets.POPUP);
    if (component instanceof AbstractUIPopup) {
      return (AbstractUIPopup) component;
    } else if (component != null) {
      LOG.error("Found wrong type='" + component.getClass().getName() + "' in popup facet of component "
          + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
    } else {
      LOG.error("Found no popup facet of component "
          + actionEvent.getComponent().getClientId(FacesContext.getCurrentInstance()));
    }
    return null;
  }
}
