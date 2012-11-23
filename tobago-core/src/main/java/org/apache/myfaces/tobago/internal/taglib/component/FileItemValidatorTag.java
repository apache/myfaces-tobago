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
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.validator.FileItemValidator;

import javax.el.ValueExpression;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorTag;
import javax.servlet.jsp.JspException;

/*
 * Date: Oct 30, 2006
 * Time: 11:07:35 PM
 */

/**
 * Register an FileItemValidator instance on the UIComponent
 * associated with the closest parent UIComponent custom action.
 */
@Tag(name = "validateFileItem")
@TagGeneration(className = "org.apache.myfaces.tobago.internal.taglib.FileItemValidatorTag")
public abstract class FileItemValidatorTag extends ValidatorTag {

  private static final long serialVersionUID = 1L;

  @TagAttribute(name = "maxSize", type = "java.lang.Integer")
  public abstract void setMaxSize(ValueExpression maxSize);

  public abstract Integer getMaxSizeValue();

  public abstract boolean isMaxSizeSet();

  @TagAttribute(name = "contentType", type = "java.lang.String")
  public abstract void setContentType(ValueExpression contentType);

  public abstract String getContentTypeValue();

  public abstract boolean isContentTypeSet();

  protected Validator createValidator() throws JspException {
    setValidatorId(FileItemValidator.VALIDATOR_ID);
    FileItemValidator validator = (FileItemValidator) super.createValidator();

    if (isMaxSizeSet()) {
      try {
        validator.setMaxSize(getMaxSizeValue());
      } catch (NumberFormatException e) {
        // ignore
      }
    }
    if (isContentTypeSet()) {
      validator.setContentType(ComponentUtils.splitList(getContentTypeValue()));
    }
    return validator;
  }
}
