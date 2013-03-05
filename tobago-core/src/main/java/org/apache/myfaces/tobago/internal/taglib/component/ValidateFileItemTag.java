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
import org.apache.myfaces.tobago.util.ComponentUtils;
import org.apache.myfaces.tobago.validator.FileItemValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.webapp.ValidatorELTag;
import javax.servlet.jsp.JspException;

/**
 * Register an FileItemValidator instance on the UIComponent
 * associated with the closest parent UIComponent custom action.
 */
@Tag(name = "validateFileItem")
@ValidatorTag(
    validatorId = FileItemValidator.VALIDATOR_ID,
    faceletHandler = "org.apache.myfaces.tobago.facelets.TobagoValidateHandler")
public abstract class ValidateFileItemTag extends ValidatorELTag {

  private static final long serialVersionUID = 2L;

  private static final Logger LOG = LoggerFactory.getLogger(ValidateFileItemTag.class);

  private javax.el.ValueExpression contentType;
  private javax.el.ValueExpression maxSize;

  protected Validator createValidator() throws JspException {

    final FacesContext facesContext = FacesContext.getCurrentInstance();
    final Application application = facesContext.getApplication();
    final FileItemValidator validator = (FileItemValidator) application.createValidator(FileItemValidator.VALIDATOR_ID);
    final ELContext elContext = facesContext.getELContext();

    if (maxSize != null) {
      try {
        validator.setMaxSize((Integer) maxSize.getValue(elContext));
      } catch (NumberFormatException e) {
        LOG.warn(e.getMessage());
      }
    }
    if (contentType != null) {
      validator.setContentType(ComponentUtils.splitList((String) contentType.getValue(elContext)));
    }
    return validator;
  }

  @Override
  public void release() {
    super.release();
    contentType = null;
    maxSize = null;
  }

  @TagAttribute(name = "maxSize", type = "java.lang.Integer")
  public void setMaxSize(ValueExpression maxSize) {
    this.maxSize = maxSize;
  }

  @TagAttribute(name = "contentType", type = "java.lang.String")
  public void setContentType(ValueExpression contentType) {
    this.contentType = contentType;
  }
}
