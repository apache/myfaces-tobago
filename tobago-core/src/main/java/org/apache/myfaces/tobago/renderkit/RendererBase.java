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

package org.apache.myfaces.tobago.renderkit;

import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;

public class RendererBase extends Renderer {

  protected String getCurrentValue(final FacesContext facesContext, final UIComponent component) {

    if (component instanceof ValueHolder) {
      final ValueHolder valueHolder = (ValueHolder) component;
      if (valueHolder instanceof EditableValueHolder) {
        final EditableValueHolder editableValueHolder = (EditableValueHolder) component;
        final Object submittedValue = editableValueHolder.getSubmittedValue();
        if (submittedValue != null || !editableValueHolder.isValid()) {
          return (String) submittedValue;
        }
      }
      String currentValue = null;
      final Object result = ((ValueHolder) component).getValue();
      if (result != null) {
        currentValue = ComponentUtils.getFormattedValue(facesContext, component, result);
      }
      return currentValue;
    } else {
      return null;
    }
  }

  @Override
  public Object getConvertedValue(final FacesContext context, final UIComponent component, final Object submittedValue)
      throws ConverterException {
    if (!(submittedValue instanceof String)) {
      return submittedValue;
    }
    final Converter converter = ComponentUtils.getConverter(context, component, submittedValue);
    if (converter != null) {
      return converter.getAsObject(context, component, (String) submittedValue);
    } else {
      return submittedValue;
    }
  }

  public void onComponentCreated(
      final FacesContext facesContext, final UIComponent component, final UIComponent parent) {
  }
}
