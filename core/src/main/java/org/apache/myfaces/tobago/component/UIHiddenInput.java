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

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;

/*
 * Date: 10.02.2006
 * Time: 19:13:51
 */
public class UIHiddenInput extends javax.faces.component.UIInput {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.HiddenInput";

  private Boolean disabled;

  public boolean isDisabled() {
    if (disabled != null) {
      return disabled;
    }
    ValueBinding vb = getValueBinding(ATTR_DISABLED);
    if (vb != null) {
      return (Boolean.TRUE.equals(vb.getValue(getFacesContext())));
    } else {
      return false;
    }
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    disabled = (Boolean) values[1];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = disabled;
    return values;
  }
}
