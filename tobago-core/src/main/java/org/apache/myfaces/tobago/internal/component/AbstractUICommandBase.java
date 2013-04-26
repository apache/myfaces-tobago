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

package org.apache.myfaces.tobago.internal.component;

import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.event.PopupFacetActionListener;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.Iterator;

public abstract class AbstractUICommandBase extends javax.faces.component.UICommand
    implements SupportsRenderedPartially, OnComponentPopulated {

  public void onComponentPopulated(FacesContext facesContext, UIComponent parent) {
    AbstractUIPopup popup = (AbstractUIPopup) getFacet(Facets.POPUP);
    if (popup != null) {
      if (!ComponentUtils.containsPopupActionListener(this)) {
        addActionListener(new PopupFacetActionListener());
      }
    }
  }

  public void processDecodes(FacesContext context) {
    if (context == null) {
      throw new NullPointerException();
    }

    // Skip processing if our rendered flag is false
    if (!isRendered()) {
      return;
    }

    // Process this component itself
    try {
      decode(context);
    } catch (RuntimeException e) {
      context.renderResponse();
      throw e;
    }

    Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(context);
    }
  }

  public void queueEvent(FacesEvent facesEvent) {
    // fix for TOBAGO-262
    super.queueEvent(facesEvent);
    if (this == facesEvent.getSource()) {
      if (isImmediate()) {
        facesEvent.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
      } else {
        facesEvent.setPhaseId(PhaseId.INVOKE_APPLICATION);
      }
    }
  }

  public abstract boolean isJsfResource();

  public abstract String getResource();

  public abstract String getLink();

  public abstract String getOnclick();

  public abstract String getTarget();

  public abstract boolean isTransition();

  public abstract String[] getRenderedPartially();

  public abstract boolean isOmit();
}
