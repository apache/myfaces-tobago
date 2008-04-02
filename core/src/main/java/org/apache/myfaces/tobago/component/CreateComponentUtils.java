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
import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.component.*;
import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class CreateComponentUtils {
  public static UIComponent createComponent(String componentType, String rendererType) {
    final FacesContext facesContext = FacesContext.getCurrentInstance();
    return createComponent(facesContext, componentType, rendererType);
  }

  public static UIComponent createComponent(FacesContext facesContext, String componentType, String rendererType) {
    UIComponent component  = facesContext.getApplication().createComponent(componentType);
    component.setRendererType(rendererType);
    return component;
  }

  public static UIColumn createTextColumn(String label, String sortable, String align, String value) {
    UIComponent text = createComponent(UIOutput.COMPONENT_TYPE, TobagoConstants.RENDERER_TYPE_OUT);
    TagUtils.setStringProperty(text, TobagoConstants.ATTR_VALUE, value);
    TagUtils.setBooleanProperty(text, TobagoConstants.ATTR_CREATE_SPAN, "false");
    TagUtils.setBooleanProperty(text, TobagoConstants.ATTR_ESCAPE, "false");
    return createColumn(label, sortable, align, text);
  }

  public static UIColumn createColumn(String label, String sortable, String align, UIComponent child) {
    UIColumn column = createColumn(label, sortable, align);
    column.getChildren().add(child);
    return column;
  }

  public static UIColumn createColumn(String label, String sortable, String align) {
    UIColumn column = (UIColumn) createComponent(UIColumn.COMPONENT_TYPE, null);
    TagUtils.setStringProperty(column, TobagoConstants.ATTR_LABEL, label);
    TagUtils.setBooleanProperty(column, TobagoConstants.ATTR_SORTABLE, sortable);
    TagUtils.setBooleanProperty(column, TobagoConstants.ATTR_ALIGN, align);
    return column;
  }

  public static UIMenuSelectOne createUIMenuSelectOneFacet(FacesContext facesContext,
      javax.faces.component.UICommand command) {
    UIMenuSelectOne radio = null;
    final ValueBinding valueBinding = command.getValueBinding(TobagoConstants.ATTR_VALUE);
    if (valueBinding != null) {
      radio = (UIMenuSelectOne) createComponent(facesContext, UIMenuSelectOne.COMPONENT_TYPE,
          TobagoConstants.RENDERER_TYPE_SELECT_ONE_RADIO);
      command.getFacets().put(org.apache.myfaces.tobago.TobagoConstants.FACET_ITEMS, radio);
      radio.setValueBinding(org.apache.myfaces.tobago.TobagoConstants.ATTR_VALUE, valueBinding);
    }
    return radio;
  }

  public static UIComponent createUISelectBooleanFacet(FacesContext facesContext, UICommand command) {
    UIComponent checkbox = createComponent(facesContext, AbstractUISelectBoolean.COMPONENT_TYPE,
        TobagoConstants.RENDERER_TYPE_SELECT_BOOLEAN_CHECKBOX);
    command.getFacets().put(TobagoConstants.FACET_ITEMS, checkbox);
    ValueBinding valueBinding = command.getValueBinding(TobagoConstants.ATTR_VALUE);
    if (valueBinding != null) {
      checkbox.setValueBinding(TobagoConstants.ATTR_VALUE, valueBinding);
    } else {
      checkbox.getAttributes().put(TobagoConstants.ATTR_VALUE, command.getAttributes().get(TobagoConstants.ATTR_VALUE));
    }
    return checkbox;
  }
}
