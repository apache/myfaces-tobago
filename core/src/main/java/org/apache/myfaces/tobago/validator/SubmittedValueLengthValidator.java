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

package org.apache.myfaces.tobago.validator;

import org.apache.myfaces.tobago.util.MessageFactory;
import org.apache.myfaces.tobago.apt.annotation.Validator;

import javax.faces.validator.LengthValidator;
import javax.faces.validator.ValidatorException;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.EditableValueHolder;
import javax.faces.application.FacesMessage;

/*
 * Date: Oct 16, 2006
 * Time: 11:58:47 PM
 */
/**
 * <p><strong>SubmittedLengthValidator</strong> is a {@link Validator} that checks
 * the number of characters in the submitted value of the
 * associated component.
 */

@Validator(id = SubmittedValueLengthValidator.VALIDATOR_ID)
public class SubmittedValueLengthValidator extends LengthValidator {
  public static final String VALIDATOR_ID = "org.apache.myfaces.tobago.SubmittedValueLength";
  private Integer minimum;
  private Integer maximum;

  public SubmittedValueLengthValidator() {
  }

  public SubmittedValueLengthValidator(int maximum) {
    setMaximum(maximum);
  }

  public SubmittedValueLengthValidator(int maximum, int minimum) {
    setMaximum(maximum);
    setMinimum(minimum);
  }

  public int getMinimum() {
    return minimum != null ? minimum : 0;
  }

  public void setMinimum(int minimum) {
    if (minimum > 0) {
      this.minimum = minimum;
    }
  }

  public int getMaximum() {
    return maximum != null ? maximum : 0;
  }

  public void setMaximum(int maximum) {
    if (maximum > 0) {
      this.maximum = maximum;
    }
  }

  public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {
    if (value != null && uiComponent instanceof EditableValueHolder) {
      String submittedValue = ((EditableValueHolder) uiComponent).getSubmittedValue().toString();
      if (maximum != null && submittedValue.length() > maximum) {
        Object[] args = {maximum, uiComponent.getId()};
        FacesMessage facesMessage =
            MessageFactory.createFacesMessage(facesContext, MAXIMUM_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, args);
        throw new ValidatorException(facesMessage);
      }
      if (minimum != null && submittedValue.length() < minimum) {
        Object[] args = {minimum, uiComponent.getId()};
        FacesMessage facesMessage =
            MessageFactory.createFacesMessage(facesContext, MINIMUM_MESSAGE_ID, FacesMessage.SEVERITY_ERROR, args);
        throw new ValidatorException(facesMessage);
      }
    }
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = maximum;
    values[1] = minimum;
    return values;

  }


  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    maximum = (Integer) values[0];
    minimum = (Integer) values[1];
  }


  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    SubmittedValueLengthValidator validator = (SubmittedValueLengthValidator) o;

    if (maximum != null ? !maximum.equals(validator.maximum) : validator.maximum != null) {
      return false;
    }
    if (minimum != null ? !minimum.equals(validator.minimum) : validator.minimum != null) {
      return false;
    }

    return true;
  }

  public int hashCode() {
    int result;
    result = (minimum != null ? minimum.hashCode() : 0);
    result = 31 * result + (maximum != null ? maximum.hashCode() : 0);
    return result;
  }
}
