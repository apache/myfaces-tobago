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

import org.apache.myfaces.tobago.internal.taglib.TagUtils;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class CreateComponentUtils {

  @Deprecated
  public static UIComponent createComponent(String componentType, String rendererType) {
    return createComponent(componentType, rendererType, null);
  }

  public static UIComponent createComponent(String componentType, String rendererType, String clientId) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType, clientId);
  }

  @Deprecated
  public static UIComponent createComponent(FacesContext facesContext, String componentType, String rendererType) {
    return createComponent(facesContext, componentType, rendererType, null);
  }

  public static UIComponent createComponent(
      FacesContext facesContext, String componentType, String rendererType, String clientId) {
    UIComponent component  = facesContext.getApplication().createComponent(componentType);
    component.setRendererType(rendererType);
    component.setId(clientId);
    return component;
  }

  @Deprecated
  public static UIColumn createTextColumn(String label, String sortable, String align, String value) {
    return createTextColumn(label, sortable, align, value, null);
  }

  public static UIColumn createTextColumn(String label, String sortable, String align, String value, String clientId) {
    UIComponent text = createComponent(UIOutput.COMPONENT_TYPE, RendererType.OUT, clientId + "_t");
    TagUtils.setStringProperty(text, Attributes.VALUE, value);
    TagUtils.setBooleanProperty(text, Attributes.CREATE_SPAN, "false");
    TagUtils.setBooleanProperty(text, Attributes.ESCAPE, "false");
    return createColumn(label, sortable, align, text, clientId);
  }

  @Deprecated
  public static UIColumn createColumn(String label, String sortable, String align, UIComponent child) {
    return createColumn(label, sortable, align, child, null);
  }
  public static UIColumn createColumn(String label, String sortable, String align, UIComponent child, String clientId) {
    UIColumn column = createColumn(label, sortable, align, clientId);
    //noinspection unchecked
    column.getChildren().add(child);
    return column;
  }

  @Deprecated
  public static UIColumn createColumn(String label, String sortable, String align) {
    return createColumn(label, sortable, align, (String) null);
  }

  public static UIColumn createColumn(String label, String sortable, String align, String clientId) {
    UIColumn column = (UIColumn) createComponent(UIColumn.COMPONENT_TYPE, null, clientId);
    TagUtils.setStringProperty(column, Attributes.LABEL, label);
    TagUtils.setBooleanProperty(column, Attributes.SORTABLE, sortable);
    TagUtils.setStringProperty(column, Attributes.ALIGN, align);
    return column;
  }

  @Deprecated
  public static UIMenuSelectOne createUIMenuSelectOneFacet(FacesContext facesContext,
      javax.faces.component.UICommand command) {
    return createUIMenuSelectOneFacet(facesContext, command, null);
  }

  public static UIMenuSelectOne createUIMenuSelectOneFacet(FacesContext facesContext,
      javax.faces.component.UICommand command, String clientId) {
    UIMenuSelectOne radio = null;
    final ValueBinding valueBinding = command.getValueBinding(Attributes.VALUE);
    if (valueBinding != null) {
      radio = (UIMenuSelectOne) createComponent(facesContext, UIMenuSelectOne.COMPONENT_TYPE,
          RendererType.SELECT_ONE_RADIO, clientId);
      //noinspection unchecked
      command.getFacets().put(Facets.ITEMS, radio);
      radio.setValueBinding(Attributes.VALUE, valueBinding);
    }
    return radio;
  }

  @Deprecated
  public static UIComponent createUISelectBooleanFacet(FacesContext facesContext, UICommand command) {
    return createUISelectBooleanFacet(facesContext, command, null);
  }

  public static UIComponent createUISelectBooleanFacet(FacesContext facesContext, UICommand command, String clientId) {
    UIComponent checkbox = createComponent(facesContext, UISelectBoolean.COMPONENT_TYPE,
        RendererType.SELECT_BOOLEAN_CHECKBOX, clientId);
    //noinspection unchecked
    command.getFacets().put(Facets.ITEMS, checkbox);
    ValueBinding valueBinding = command.getValueBinding(Attributes.VALUE);
    if (valueBinding != null) {
      checkbox.setValueBinding(Attributes.VALUE, valueBinding);
    } else {
      //noinspection unchecked
      checkbox.getAttributes().put(Attributes.VALUE, command.getAttributes().get(Attributes.VALUE));
    }
    return checkbox;
  }
}
