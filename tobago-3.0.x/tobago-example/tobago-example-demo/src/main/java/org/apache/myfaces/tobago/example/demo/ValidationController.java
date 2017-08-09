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

package org.apache.myfaces.tobago.example.demo;

import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import java.io.Serializable;

@RequestScoped
@Named
public class ValidationController implements Serializable {

  public void customValidator(final FacesContext context, final UIComponent component, final Object value)
          throws ValidatorException {
    if (value == null) {
      return;
    }
    if (!"tobago".equalsIgnoreCase(value.toString())) {
      throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please type in 'Tobago'",
              "Please type in 'Tobago'"));
    }
  }

  public void passwordValidator(FacesContext context, UIComponent component, Object value) throws ValidatorException {
    String password = value.toString();

    UIInput confirmationField = (UIInput) component.getAttributes().get("confirmationField");
    String confirmationFieldValue = confirmationField.getSubmittedValue().toString();

    if (password.isEmpty() || confirmationFieldValue.isEmpty()) {
      return;
    }

    if (!password.equals(confirmationFieldValue)) {
      confirmationField.setValid(false);
      throw new ValidatorException(new FacesMessage("Passwords must match."));
    }
  }
}
