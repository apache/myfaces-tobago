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

import org.apache.myfaces.tobago.component.SupportsMarkup;
import org.apache.myfaces.tobago.layout.LayoutComponent;
import org.apache.myfaces.tobago.util.MessageUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;

public abstract class AbstractUISelectOneBase extends javax.faces.component.UISelectOne
    implements SupportsMarkup, LayoutComponent {

  public static final String MESSAGE_VALUE_REQUIRED = "tobago.SelectOne.MESSAGE_VALUE_REQUIRED";

  public void validate(FacesContext facesContext) {
    if (isRequired()  && !isReadonly()) {
      Object submittedValue = getSubmittedValue();
      if (submittedValue == null || "".equals(submittedValue)) {
        if (getRequiredMessage() != null) {
          String requiredMessage = getRequiredMessage();
          facesContext.addMessage(getClientId(facesContext), new FacesMessage(FacesMessage.SEVERITY_ERROR,
              requiredMessage, requiredMessage));
        } else {
          MessageUtils.addMessage(facesContext, this, FacesMessage.SEVERITY_ERROR,
              UIInput.REQUIRED_MESSAGE_ID, new Object[]{MessageUtils.getLabel(facesContext, this)});
        }
        setValid(false);
      }
    }
    super.validate(facesContext);
  }

  public abstract boolean isReadonly();

  public abstract String getRequiredMessage();
}
