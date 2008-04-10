package org.apache.myfaces.tobago.taglib.component;

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

import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.apt.annotation.TagGeneration;
import org.apache.myfaces.tobago.validator.SubmittedValueLengthValidator;

import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/*
 * Date: Oct 17, 2006
 * Time: 12:35:01 AM
 */

/**
 * Register an SubmittedValueLengthValidator instance on the UIComponent
 * associated with the closest parent UIComponent custom action.
 */
@Tag(name = "validateSubmittedValueLength")
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.SubmittedValueLengthValidatorTag")
public abstract class SubmittedValueLengthValidatorTag extends ValidatorTag {

  private static final long serialVersionUID = 6777040780038715924L;

  public abstract boolean isMinimumSet();

  @TagAttribute(name = "minimum")
  public abstract String getMinimumValue();

  public abstract boolean isMaximumSet();

  @TagAttribute(name = "maximum")
  public abstract String getMaximumValue();

  protected Validator createValidator() throws JspException {
    setValidatorId(SubmittedValueLengthValidator.VALIDATOR_ID);
    SubmittedValueLengthValidator validator = (SubmittedValueLengthValidator) super.createValidator();
    if (isMinimumSet()) {
      try {
        validator.setMinimum(Integer.parseInt(getMinimumValue()));
      } catch (NumberFormatException e) {
        // ignore
      }
    }
    if (isMaximumSet()) {
      try {
        validator.setMaximum(Integer.parseInt(getMaximumValue()));
      } catch (NumberFormatException e) {
        // ignore
      }
    }
    return validator;
  }


}
