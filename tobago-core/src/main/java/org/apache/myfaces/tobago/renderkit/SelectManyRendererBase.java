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

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.myfaces.tobago.util.ComponentUtils;

import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectMany;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.el.ValueBinding;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectManyRendererBase extends LayoutComponentRendererBase {

  private static final Logger LOG = LoggerFactory.getLogger(SelectManyRendererBase.class);

  public void decode(FacesContext facesContext, UIComponent component) {
    if (ComponentUtils.isOutputOnly(component)) {
      return;
    }
    if (component instanceof UISelectMany) {
      UISelectMany uiSelectMany = (UISelectMany) component;

      String[] newValues = (String[])
          facesContext.getExternalContext().getRequestParameterValuesMap().get(uiSelectMany.getClientId(facesContext));
      if (LOG.isDebugEnabled()) {
        LOG.debug("decode: key='" + component.getClientId(facesContext)
            + "' value='" + Arrays.toString(newValues) + "'");
        LOG.debug("size ... '" + (newValues != null ? newValues.length : -1) + "'");
        if (newValues != null) {
          for (String newValue : newValues) {
            LOG.debug("newValues[i] = '" + newValue + "'");
          }
        }
      }

      if (newValues == null) {
        newValues = ArrayUtils.EMPTY_STRING_ARRAY; // because no selection will not submitted by browsers
      }
      uiSelectMany.setSubmittedValue(newValues);
    }
  }

  // the following is copied from myfaces shared RendererUtils
  public Object getConvertedValue(FacesContext facesContext, UIComponent component, Object submittedValue)
      throws ConverterException {

    if (submittedValue == null) {
      return null;
    } else {
      if (!(submittedValue instanceof String[])) {
        throw new ConverterException("Submitted value of type String[] for component : "
            + component.getClientId(facesContext) + "expected");
      }
    }
    return getConvertedUISelectManyValue(facesContext, (UISelectMany) component, (String[]) submittedValue);
  }

  private Object getConvertedUISelectManyValue(FacesContext facesContext,
      UISelectMany component,
      String[] submittedValue)
      throws ConverterException {
    // Attention!
    // This code is duplicated in jsfapi component package.
    // If you change something here please do the same in the other class!

    if (submittedValue == null) {
      throw new NullPointerException("submittedValue");
    }

    ValueBinding vb = component.getValueBinding("value");
    Class valueType = null;
    Class arrayComponentType = null;
    if (vb != null) {
      valueType = vb.getType(facesContext);
      if (valueType != null && valueType.isArray()) {
        arrayComponentType = valueType.getComponentType();
      }
    }

    Converter converter = component.getConverter();
    if (converter == null) {
      if (valueType == null) {
        // No converter, and no idea of expected type
        // --> return the submitted String array
        return submittedValue;
      }

      if (List.class.isAssignableFrom(valueType)) {
        // expected type is a List
        // --> according to javadoc of UISelectMany we assume that the element type
        //     is java.lang.String, and copy the String array to a new List
        return Arrays.asList(submittedValue);
      }

      if (arrayComponentType == null) {
        throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
      }

      if (String.class.equals(arrayComponentType)) {
        return submittedValue; //No conversion needed for String type
      }
      if (Object.class.equals(arrayComponentType)) {
        return submittedValue; //No conversion for Object class
      }

      try {
        converter = facesContext.getApplication().createConverter(arrayComponentType);
      } catch (FacesException e) {
        LOG.error("No Converter for type " + arrayComponentType.getName() + " found", e);
        return submittedValue;
      }
    }

    // Now, we have a converter...
    // We determine the type of the component array after converting one of it's elements
    if (vb != null && arrayComponentType == null
        && valueType != null && valueType.isArray()) {
      if (submittedValue.length > 0) {
        arrayComponentType = converter.getAsObject(facesContext, component, submittedValue[0]).getClass();
      }
    }

    if (valueType == null) {
      // ...but have no idea of expected type
      // --> so let's convert it to an Object array
      int len = submittedValue.length;
      Object[] convertedValues = (Object[]) Array.newInstance(
          arrayComponentType == null ? Object.class : arrayComponentType, len);
      for (int i = 0; i < len; i++) {
        convertedValues[i]
            = converter.getAsObject(facesContext, component, submittedValue[i]);
      }
      return convertedValues;
    }

    if (List.class.isAssignableFrom(valueType)) {
      // Curious case: According to specs we should assume, that the element type
      // of this List is java.lang.String. But there is a Converter set for this
      // component. Because the user must know what he is doing, we will convert the values.
      int length = submittedValue.length;
      List<Object> list = new ArrayList<Object>(length);
      for (int i = 0; i < length; i++) {
        list.add(converter.getAsObject(facesContext, component, submittedValue[i]));
      }
      return list;
    }

    if (arrayComponentType == null) {
      throw new IllegalArgumentException("ValueBinding for UISelectMany must be of type List or Array");
    }

    if (arrayComponentType.isPrimitive()) {
      // primitive array
      int len = submittedValue.length;
      Object convertedValues = Array.newInstance(arrayComponentType, len);
      for (int i = 0; i < len; i++) {
        Array.set(convertedValues, i,
            converter.getAsObject(facesContext, component, submittedValue[i]));
      }
      return convertedValues;
    } else {
      // Object array
      int length = submittedValue.length;
      List<Object> convertedValues = new ArrayList<Object>(length);
      for (int i = 0; i < length; i++) {
        convertedValues.add(i, converter.getAsObject(facesContext, component, submittedValue[i]));
      }
      return convertedValues.toArray((Object[]) Array.newInstance(arrayComponentType, length));
    }
  }

}
