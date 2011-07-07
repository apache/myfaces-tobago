package org.apache.myfaces.tobago.component;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.myfaces.tobago.internal.component.AbstractUIForm;
import org.apache.myfaces.tobago.renderkit.RendererBase;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;
import javax.faces.render.Renderer;
import java.util.List;

/*
 * Date: 30.05.2006
 * Time: 19:22:40
 */
public class UIDatePicker extends UILink implements OnComponentCreated {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.DatePicker";

  private String forComponent;

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = forComponent;
    return values;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    forComponent = (String) values[1];
  }

  private UIComponent getUIDateInput(UIComponent parent) {
    for (UIComponent child : (List<UIComponent>) parent.getChildren()) {
      if (child instanceof UIDate) {
        return child;
      }
    }
    return null;
  }

  public String getFor() {
    return forComponent;
  }

  // XXX May be better use ComponentUtils.evaluateAutoFor()
  public UIComponent getForComponent() {
    if ("@auto".equals(forComponent)) {
      UIComponent component = getUIDateInput(getParent());
      if (component == null && getParent() instanceof AbstractUIForm) {
        component = getUIDateInput(getParent().getParent());
      }
      return component;
    } else {
      return findComponent(forComponent);
    }
  }

  public void setFor(String forComponent) {
    this.forComponent = forComponent;
  }

  public void broadcast(FacesEvent facesEvent) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    UIComponent popup = (UIComponent) getFacets().get(Facets.PICKER_POPUP);
    String clientId = getForComponent().getClientId(facesContext);
    UIComponent box = popup.findComponent("box");
    UIComponent calendar = box.findComponent("calendar");
    calendar.getAttributes().put(Attributes.DATE_INPUT_ID, clientId);
    UIComponent time = box.findComponent("time");
    if (time != null) {
      time.getAttributes().put(Attributes.DATE_INPUT_ID, clientId);
    }
    super.broadcast(facesEvent);
  }

  public void onComponentCreated(FacesContext context, UIComponent parent) {
    Renderer renderer = getRenderer(getFacesContext());
    if (renderer instanceof RendererBase) {
      ((RendererBase) renderer).onComponentCreated(context, this, parent);
    }
  }
}
