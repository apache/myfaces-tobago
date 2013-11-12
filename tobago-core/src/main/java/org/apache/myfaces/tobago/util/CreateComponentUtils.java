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
import org.apache.myfaces.tobago.component.ComponentTypes;
import org.apache.myfaces.tobago.component.Facets;
import org.apache.myfaces.tobago.component.OnComponentCreated;
import org.apache.myfaces.tobago.component.OnComponentPopulated;
import org.apache.myfaces.tobago.component.RendererTypes;
import org.apache.myfaces.tobago.component.UIMenuSelectOne;
import org.apache.myfaces.tobago.internal.component.AbstractUIColumn;
import org.apache.myfaces.tobago.internal.component.AbstractUIOut;
import org.apache.myfaces.tobago.internal.component.AbstractUISelectBooleanCheckbox;
import org.apache.myfaces.tobago.internal.util.ComponentAttributeUtils;
import org.apache.myfaces.tobago.layout.Display;
import org.apache.myfaces.tobago.layout.LayoutManager;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public final class CreateComponentUtils {

  private CreateComponentUtils() {
  }

  @Deprecated
  public static UIComponent createComponent(final String componentType, final String rendererType) {
    return createComponent(componentType, rendererType, null);
  }

  public static UIComponent createComponent(
      final String componentType, final String rendererType, final String clientId) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType, clientId);
  }

  @Deprecated
  public static UIComponent createComponent(
      final FacesContext facesContext, final String componentType, final String rendererType) {
    return createComponent(facesContext, componentType, rendererType, null);
  }

  public static UIComponent createComponent(
      final FacesContext facesContext, final String componentType, final String rendererType, final String clientId) {
    final UIComponent component  = facesContext.getApplication().createComponent(componentType);
    component.setRendererType(rendererType);
    component.setId(clientId);
    return component;
  }

  @Deprecated
  public static AbstractUIColumn createTextColumn(
      final String label, final String sortable, final String align, final String value) {
    return createTextColumn(label, sortable, align, value, null);
  }

  public static AbstractUIColumn createTextColumn(
      final String label, final String sortable, final String align, final String value, final String clientId) {
    final AbstractUIOut text = (AbstractUIOut) createComponent(ComponentTypes.OUT, RendererTypes.OUT, clientId + "_t");
    ComponentAttributeUtils.setStringProperty(text, Attributes.VALUE, value);
    ComponentAttributeUtils.setBooleanProperty(text, Attributes.CREATE_SPAN, "false");
    ComponentAttributeUtils.setBooleanProperty(text, Attributes.ESCAPE, "false");
    text.setDisplay(Display.INLINE);
    return createColumn(label, sortable, align, text, clientId);
  }

  @Deprecated
  public static AbstractUIColumn createColumn(
      final String label, final String sortable, final String align, final UIComponent child) {
    return createColumn(label, sortable, align, child, null);
  }

  public static AbstractUIColumn createColumn(
      final String label, final String sortable, final String align, final UIComponent child, final String clientId) {
    final AbstractUIColumn column = createColumn(label, sortable, align, clientId);
    //noinspection unchecked
    column.getChildren().add(child);
    return column;
  }

  @Deprecated
  public static AbstractUIColumn createColumn(final String label, final String sortable, final String align) {
    return createColumn(label, sortable, align, (String) null);
  }

  public static AbstractUIColumn createColumn(
      final String label, final String sortable, final String align, final String clientId) {
    final AbstractUIColumn column = (AbstractUIColumn) createComponent(ComponentTypes.COLUMN, null, clientId);
    ComponentAttributeUtils.setStringProperty(column, Attributes.LABEL, label);
    ComponentAttributeUtils.setBooleanProperty(column, Attributes.SORTABLE, sortable);
    ComponentAttributeUtils.setStringProperty(column, Attributes.ALIGN, align);
    return column;
  }

  @Deprecated
  public static UIMenuSelectOne createUIMenuSelectOneFacet(
      final FacesContext facesContext, final UICommand command) {
    return createUIMenuSelectOneFacet(facesContext, command, null);
  }

  public static UIMenuSelectOne createUIMenuSelectOneFacet(
      final FacesContext facesContext, final UICommand command, final String clientId) {

    final UIMenuSelectOne radio = (UIMenuSelectOne) createComponent(
        facesContext, UIMenuSelectOne.COMPONENT_TYPE, RendererTypes.SELECT_ONE_RADIO, clientId);
    //noinspection unchecked
    command.getFacets().put(Facets.RADIO, radio);
    final ValueBinding valueBinding = command.getValueBinding(Attributes.VALUE);
    if (valueBinding != null) {
      radio.setValueBinding(Attributes.VALUE, valueBinding);
    } else {
      radio.setValue(command.getValue());
    }
    return radio;
  }

  @Deprecated
  public static UIComponent createUISelectBooleanFacet(final FacesContext facesContext, final UICommand command) {
    return createUISelectBooleanFacet(facesContext, command, null);
  }

  public static AbstractUISelectBooleanCheckbox createUISelectBooleanFacetWithId(
      final FacesContext facesContext, final UICommand command) {
    return createUISelectBooleanFacet(facesContext, command, facesContext.getViewRoot().createUniqueId());
  }

  public static AbstractUISelectBooleanCheckbox createUISelectBooleanFacet(
      final FacesContext facesContext, final UICommand command, final String clientId) {
    final AbstractUISelectBooleanCheckbox checkbox = (AbstractUISelectBooleanCheckbox) createComponent(
        facesContext, ComponentTypes.SELECT_BOOLEAN_CHECKBOX, RendererTypes.SELECT_BOOLEAN_CHECKBOX, clientId);
    //noinspection unchecked
    command.getFacets().put(Facets.CHECKBOX, checkbox);
    final ValueBinding valueBinding = command.getValueBinding(Attributes.VALUE);
    if (valueBinding != null) {
      checkbox.setValueBinding(Attributes.VALUE, valueBinding);
    } else {
      //noinspection unchecked
      checkbox.setValue(command.getValue());
    }
    return checkbox;
  }

  public static LayoutManager createAndInitLayout(
      final FacesContext facesContext, final String componentType, final String rendererType,
      final UIComponent parent) {

    final LayoutManager layoutManager = (LayoutManager) CreateComponentUtils.createComponent(
        facesContext, componentType, rendererType, facesContext.getViewRoot().createUniqueId());
    if (layoutManager instanceof OnComponentCreated) {
      ((OnComponentCreated) layoutManager).onComponentCreated(facesContext, parent);
    }
    if (layoutManager instanceof OnComponentPopulated) {
      ((OnComponentPopulated) layoutManager).onComponentPopulated(facesContext, parent);
    }
    return layoutManager;
  }
}
