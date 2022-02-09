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

import org.apache.myfaces.tobago.component.Attributes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.config.TobagoConfig;
import org.apache.myfaces.tobago.renderkit.RendererBase;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.render.Renderer;

/**
 * @deprecated Since 2.0.0. It's no longer needed, because this is resolved by JavaScript now. Set backward
 * compatibility mode via classic-date-time-picker.
 */
@Deprecated
public abstract class AbstractUIDatePicker extends AbstractUILink implements OnComponentCreated {

  public abstract String getFor();

  public abstract void setDisabled(boolean disabled);

  public UIComponent getForComponent() {
    ComponentUtils.evaluateAutoFor(this, AbstractUIDate.class);
    return ComponentUtils.findFor(this);
  }

  public void broadcast(final FacesEvent facesEvent) {

    if (!TobagoConfig.getInstance(FacesContext.getCurrentInstance()).isClassicDateTimePicker()) {
      return;
    }

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final UIComponent popup = (UIComponent) getFacets().get(Facets.POPUP);
    final String clientId = getForComponent().getClientId(facesContext);
    final UIComponent box = popup.findComponent("box");
    final UIComponent calendar = box.findComponent("calendar");
    calendar.getAttributes().put(Attributes.DATE_INPUT_ID, clientId);
    final UIComponent time = box.findComponent("time");
    if (time != null) {
      time.getAttributes().put(Attributes.DATE_INPUT_ID, clientId);
    }
    super.broadcast(facesEvent);
  }

  public void onComponentCreated(final FacesContext facesContext, final UIComponent parent) {

    if (!TobagoConfig.getInstance(facesContext).isClassicDateTimePicker()) {
      return;
    }

    final Renderer renderer = getRenderer(getFacesContext());
    if (renderer instanceof RendererBase) {
      ((RendererBase) renderer).onComponentCreated(facesContext, this, parent);
    }
  }

  public String getLabel() {
    return null;
  }

  public Character getAccessKey() {
    return null;
  }

  public String getTarget() {
    return null;
  }

  public boolean isTransition() {
    return true;
  }

  public boolean isOmit() {
    return false;
  }
}
