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
import org.apache.myfaces.tobago.apt.annotation.ValidatorTag;
import org.apache.myfaces.tobago.validator.SubmittedValueLengthValidator;

import javax.el.ValueExpression;

/**
 * Register an {@link SubmittedValueLengthValidator} instance on the UIComponent
 * associated with the closest parent UIComponent custom action.
 * The standard LengthValidator validate the length on the converted value.toString()
 * not on the submitted value. Sometime you need to check the length of the submitted value.
 */
@Tag(name = "validateSubmittedValueLength")
@ValidatorTag(validatorId = SubmittedValueLengthValidator.VALIDATOR_ID)
public interface ValidateSubmittedValueLengthTagDeclaration {

  @TagAttribute(name = "minimum", type = "java.lang.Integer")
  void setMinimum(final ValueExpression minimum);

  @TagAttribute(name = "maximum", type = "java.lang.Integer")
  void setMaximum(final ValueExpression maximum);

}
