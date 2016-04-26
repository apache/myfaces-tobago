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
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.component.UISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public final class CreateComponentUtils {

  private CreateComponentUtils() {
  }

  public static UIComponent createComponent(
      final String componentType, final RendererTypes rendererType, final String clientId) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType, clientId);
  }

  public static UIComponent createComponent(
      final FacesContext facesContext, final String componentType, final RendererTypes rendererType,
      final String clientId) {
    final UIComponent component  = facesContext.getApplication().createComponent(componentType);
    if (rendererType != null) {
      component.setRendererType(rendererType.name());
    }
    component.setId(clientId);
    return component;
  }

  public static UIMenuSelectOne createUIMenuSelectOneFacet(
      final FacesContext facesContext, final UICommand command, final String clientId) {

    final UIMenuSelectOne radio = (UIMenuSelectOne) createComponent(
        facesContext, UIMenuSelectOne.COMPONENT_TYPE, RendererTypes.SelectOneRadio, clientId);
    //noinspection unchecked
    ComponentUtils.setFacet(command, Facets.radio, radio);
    final ValueBinding valueBinding = command.getValueBinding(Attributes.value.getName());
    if (valueBinding != null) {
      radio.setValueBinding(Attributes.value.getName(), valueBinding);
    } else {
      radio.setValue(command.getValue());
    }
    return radio;
  }

  public static AbstractUISelectBooleanCheckbox createUISelectBooleanFacetWithId(
      final FacesContext facesContext, final UICommand command) {
    return createUISelectBooleanFacet(facesContext, command, facesContext.getViewRoot().createUniqueId());
  }

  public static AbstractUISelectBooleanCheckbox createUISelectBooleanFacet(
      final FacesContext facesContext, final UICommand command, final String clientId) {
    final AbstractUISelectBooleanCheckbox checkbox = (AbstractUISelectBooleanCheckbox) createComponent(
        facesContext, UISelectBooleanCheckbox.COMPONENT_TYPE, RendererTypes.SelectBooleanCheckbox, clientId);
    //noinspection unchecked
    ComponentUtils.setFacet(command, Facets.checkbox, checkbox);
    final ValueBinding valueBinding = command.getValueBinding(Attributes.value.getName());
    if (valueBinding != null) {
      checkbox.setValueBinding(Attributes.value.getName(), valueBinding);
    } else {
      //noinspection unchecked
      checkbox.setValue(command.getValue());
    }
    return checkbox;
  }
}
