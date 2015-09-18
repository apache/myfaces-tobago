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
import org.apache.myfaces.tobago.component.SupportsAccessKey;
import org.apache.myfaces.tobago.component.SupportsRenderedPartially;
import org.apache.myfaces.tobago.component.SupportsStyle;
import org.apache.myfaces.tobago.config.Configurable;
import org.apache.myfaces.tobago.event.PopupFacetActionListener;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.event.PhaseId;
import java.util.Iterator;

public abstract class AbstractUICommand
    extends UICommand
    implements SupportsRenderedPartially, SupportsAccessKey, OnComponentPopulated, Configurable, SupportsStyle {

  // todo: transient
  private Boolean parentOfCommands;

  public void onComponentPopulated(final FacesContext facesContext, final UIComponent parent) {
    final AbstractUIPopup popup = (AbstractUIPopup) getFacet(Facets.POPUP);
    if (popup != null) {
      if (!ComponentUtils.containsPopupActionListener(this)) {
        addActionListener(new PopupFacetActionListener());
      }
    }
  }

  public void processDecodes(final FacesContext context) {
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
    } catch (final RuntimeException e) {
      context.renderResponse();
      throw e;
    }

    final Iterator kids = getFacetsAndChildren();
    while (kids.hasNext()) {
      final UIComponent kid = (UIComponent) kids.next();
      kid.processDecodes(context);
    }
  }

  public void queueEvent(final FacesEvent facesEvent) {
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

  public boolean isParentOfCommands() {
    if (parentOfCommands == null) {
      parentOfCommands = false;
      for (UIComponent child : getChildren()) {
        if (child instanceof UICommand) {
          parentOfCommands = true;
          break;
        }
      }
    }
    return parentOfCommands;
  }

  public abstract String getLabel();

  public abstract boolean isJsfResource();

  public abstract String getResource();

  public abstract String getLink();

  public abstract String getTarget();

  public abstract boolean isTransition();

  public abstract String[] getRenderedPartially();

  public abstract boolean isOmit();

  public abstract boolean isDisabled();

  public abstract String getTip();

//  public abstract Integer getTabIndex();
}
