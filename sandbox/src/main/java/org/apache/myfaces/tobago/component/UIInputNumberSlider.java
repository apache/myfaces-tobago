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

package org.apache.myfaces.tobago.component;

import org.apache.myfaces.tobago.TobagoConstants;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.validator.LongRangeValidator;
import javax.faces.validator.ValidatorException;

public class UIInputNumberSlider extends javax.faces.component.UIInput {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.InputNumberSlider";

  private Boolean readonly;
  private Boolean disabled;
  private Integer min;
  private Integer max;

  public Boolean isReadonly() {
    if (readonly != null) {
      return readonly;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_READONLY);
    if (vb == null) {
      return false;
    } else {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    }
  }

  public void setReadonly(Boolean readonly) {
    this.readonly = readonly;
  }

  public Boolean isDisabled() {
    if (disabled != null) {
      return disabled;
    }
    ValueBinding vb = getValueBinding(TobagoConstants.ATTR_DISABLED);
    if (vb == null) {
      return false;
    } else {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    }
  }

  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  public Integer getMin() {
    if (min != null) {
      return min;
    }
    ValueBinding vb = getValueBinding("min");
    if (vb != null) {
      return (Integer) vb.getValue(getFacesContext());
    } else {
      return 0;
    }
  }

  public void setMin(Integer min) {
    this.min = min;
  }

  public Integer getMax() {
    if (max != null) {
      return max;
    }
    ValueBinding vb = getValueBinding("max");
    if (vb != null) {
      return (Integer) vb.getValue(getFacesContext());
    } else {
      return 100;
    }
  }

  public void setMax(Integer max) {
    this.max = max;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    readonly = (Boolean) values[1];
    min = (Integer) values[2];
    max = (Integer) values[3];
    disabled = (Boolean) values[4];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[5];
    values[0] = super.saveState(context);
    values[1] = readonly;
    values[2] = min;
    values[3] = max;
    values[4] = disabled;
    return values;
  }

  public void validate(FacesContext context) {
    super.validate(context);
    try {
      new LongRangeValidator(max, min).validate(context, this, getValue());
    } catch (ValidatorException e) {
      context.addMessage(getClientId(context), e.getFacesMessage());
    }
  }
}
