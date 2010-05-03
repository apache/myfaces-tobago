package org.apache.myfaces.tobago.internal.component;

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

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.util.MessageFactory;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class AbstractUISelectBooleanCheckbox extends javax.faces.component.UISelectBoolean 
    implements LayoutComponent, SupportsMarkup {

  public boolean isSelected() {
    Object value = getSubmittedValue();
    if (value == null) {
      value = getValue();
    }
    if (value instanceof Boolean) {
      return ((Boolean) value);
    } else {
      return value != null && Boolean.valueOf(value.toString());
    }
  }

  protected void validateValue(FacesContext context, Object convertedValue) {
    if (isRequired()) {
      if (convertedValue instanceof Boolean && !((Boolean) convertedValue)
          // String: e. g. if there is no ValueExpression
          || convertedValue instanceof String && !Boolean.parseBoolean((String) convertedValue)) {
        FacesMessage facesMessage = MessageFactory.createFacesMessage(context,
            REQUIRED_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, new Object[]{getId()});
        context.addMessage(getClientId(context), facesMessage);
        setValid(false);
        return;
      }
    }
    super.validateValue(context, convertedValue);
  }
}
