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

package org.apache.myfaces.tobago.taglib.component;

import org.apache.commons.lang.StringUtils;
import org.apache.myfaces.tobago.apt.annotation.Tag;
import org.apache.myfaces.tobago.apt.annotation.TagAttribute;
import org.apache.myfaces.tobago.validator.FileItemValidator;

import javax.faces.webapp.ValidatorTag;
import javax.faces.validator.Validator;
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
public class FileItemValidatorTag extends ValidatorTag {

  private static final long serialVersionUID = -1461244883146997440L;

  private String maxSize;
  private String contentType;

  public String getMaxSize() {
    return maxSize;
  }

  @TagAttribute()
  public void setMaxSize(String maxSize) {
    this.maxSize = maxSize;
  }

  public String getContentType() {
    return contentType;
  }

  @TagAttribute()
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  protected Validator createValidator() throws JspException {
    setValidatorId(FileItemValidator.VALIDATOR_ID);
    FileItemValidator validator = (FileItemValidator) super.createValidator();

    if (maxSize != null) {
      try {
        validator.setMaxSize(Integer.parseInt(maxSize));
      } catch (NumberFormatException e) {
        // ignore
      }
    }
    if (contentType != null) {
      validator.setContentType(StringUtils.split(contentType, ", "));
    }
    return validator;
  }


  public void release() {
    super.release();
    maxSize = null;
    contentType = null;
  }

}
