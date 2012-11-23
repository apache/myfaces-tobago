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

package org.apache.myfaces.tobago.internal.taglib.component;

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.validator.SubmittedValueLengthValidator;

import javax.el.ValueExpression;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/**
 * Register an SubmittedValueLengthValidator instance on the UIComponent
 * associated with the closest parent UIComponent custom action.
 * The standard LengthValidator validate the length on the converted value.toString()
 * not on the submitted value. Sometime you need to check the length of the submitted value.
 */
@Tag(name = "validateSubmittedValueLength")
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.SubmittedValueLengthValidatorTag")
public abstract class SubmittedValueLengthValidatorTag extends ValidatorTag {

  private static final long serialVersionUID = 1L;

  @TagAttribute(name = "minimum", type = "java.lang.Integer")
  public abstract void setMinimum(ValueExpression minimum);

  public abstract Integer getMinimumValue();

  public abstract boolean isMinimumSet();

  @TagAttribute(name = "maximum", type = "java.lang.Integer")
  public abstract void setMaximum(ValueExpression maximum);

  public abstract Integer getMaximumValue();

  public abstract boolean isMaximumSet();

  protected Validator createValidator() throws JspException {
    setValidatorId(SubmittedValueLengthValidator.VALIDATOR_ID);
    SubmittedValueLengthValidator validator = (SubmittedValueLengthValidator) super.createValidator();
    if (isMinimumSet()) {
      try {
        validator.setMinimum(getMinimumValue());
      } catch (NumberFormatException e) {
        // ignore
      }
    }
    if (isMaximumSet()) {
      try {
        validator.setMaximum(getMaximumValue());
      } catch (NumberFormatException e) {
        // ignore
      }
    }
    return validator;
  }


}
