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

package org.apache.myfaces.tobago.internal.util;

import org.apache.myfaces.tobago.compat.FacesUtilsEL;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public final class ComponentAttributeUtils {

  private ComponentAttributeUtils() {
  }

  public static void setBooleanProperty(UIComponent component, String name, String value) {
    if (value != null) {
      if (isValueReference(value)) {
        component.setValueExpression(name, FacesUtilsEL.createValueExpression(value));
      } else {
        component.getAttributes().put(name, Boolean.valueOf(value));
      }
    }
  }

  public static void setStringProperty(UIComponent component, String name, String value) {
    if (value != null) {
      if (isValueReference(value)) {
        component.setValueExpression(name, FacesUtilsEL.createValueExpression(value));
      } else {
        component.getAttributes().put(name, value);
      }
    }
  }

  public static boolean isValueReference(String value) {

      int start = value.indexOf("#{");
      if (start < 0) {
        return false;
      }
      int end = value.lastIndexOf('}');
      return (end >=0 && start < end);
  }

  /**
   * @deprecated Since 2.0.0, please use {@link FacesUtilsEL#createValueExpression(String string)}
   */
  @Deprecated
  public static ValueBinding createValueBinding(String value) {
    return FacesContext.getCurrentInstance().getApplication().createValueBinding(value);
  }

}
