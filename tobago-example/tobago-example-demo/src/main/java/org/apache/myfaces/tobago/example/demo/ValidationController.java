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

import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.ValidatorException;
import jakarta.inject.Named;

import java.io.Serializable;

@RequestScoped
@Named
public class ValidationController implements Serializable {
  private String letter;

  public String getLetter() {
    return letter;
  }

  public void setLetter(final String letter) {
    this.letter = letter;
  }

  public void customValidator(final FacesContext facesContext, final UIComponent component, final Object value)
      throws ValidatorException {
    if (value == null) {
      return;
    }
    if (!"tobago".equalsIgnoreCase(value.toString())) {
      throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please type in 'Tobago'",
          "Please type in 'Tobago'"));
    }
  }

  public void passwordValidator(final FacesContext facesContext, final UIComponent component, final Object value)
      throws ValidatorException {
    final String password = value.toString();

    final UIInput confirmationField = (UIInput) component.getAttributes().get("confirmationField");
    final String confirmationFieldValue = confirmationField.getSubmittedValue().toString();

    if (password.isEmpty() || confirmationFieldValue.isEmpty()) {
      return;
    }

    if (!password.equals(confirmationFieldValue)) {
      confirmationField.setValid(false);
      throw new ValidatorException(new FacesMessage("Passwords must match."));
    }
  }
}
