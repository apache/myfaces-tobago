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

import org.apache.myfaces.tobago.apt.annotation.Validator;
import org.apache.myfaces.tobago.util.MessageUtils;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.LengthValidator;
import javax.faces.validator.ValidatorException;

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

  public SubmittedValueLengthValidator(final int maximum) {
    setMaximum(maximum);
  }

  public SubmittedValueLengthValidator(final int maximum, final int minimum) {
    setMaximum(maximum);
    setMinimum(minimum);
  }

  @Override
  public int getMinimum() {
    return minimum != null ? minimum : 0;
  }

  @Override
  public void setMinimum(final int minimum) {
    if (minimum > 0) {
      this.minimum = minimum;
    }
  }

  @Override
  public int getMaximum() {
    return maximum != null ? maximum : 0;
  }

  @Override
  public void setMaximum(final int maximum) {
    if (maximum > 0) {
      this.maximum = maximum;
    }
  }

  @Override
  public void validate(final FacesContext facesContext, final UIComponent uiComponent, final Object value)
      throws ValidatorException {
    if (value != null && uiComponent instanceof EditableValueHolder) {
      final String submittedValue = ((EditableValueHolder) uiComponent).getSubmittedValue().toString();
      if (maximum != null && submittedValue.length() > maximum) {
        final Object[] args = {maximum, uiComponent.getId()};
        final FacesMessage facesMessage = MessageUtils.getMessage(facesContext,
            facesContext.getViewRoot().getLocale(), FacesMessage.SEVERITY_ERROR, MAXIMUM_MESSAGE_ID, args);
        throw new ValidatorException(facesMessage);
      }
      if (minimum != null && submittedValue.length() < minimum) {
        final Object[] args = {minimum, uiComponent.getId()};
        final FacesMessage facesMessage = MessageUtils.getMessage(facesContext,
            facesContext.getViewRoot().getLocale(), FacesMessage.SEVERITY_ERROR, MINIMUM_MESSAGE_ID, args);
        throw new ValidatorException(facesMessage);
      }
    }
  }

  @Override
  public Object saveState(final FacesContext context) {
    final Object[] values = new Object[2];
    values[0] = maximum;
    values[1] = minimum;
    return values;
  }

  @Override
  public void restoreState(final FacesContext context, final Object state) {
    final Object[] values = (Object[]) state;
    maximum = (Integer) values[0];
    minimum = (Integer) values[1];
  }

  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    final SubmittedValueLengthValidator validator = (SubmittedValueLengthValidator) o;

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
    result = minimum != null ? minimum.hashCode() : 0;
    result = 31 * result + (maximum != null ? maximum.hashCode() : 0);
    return result;
  }
}
