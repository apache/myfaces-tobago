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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_LABEL;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_DISABLED;

import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/*
 * Date: Mar 22, 2007
 * Time: 10:51:16 PM
 */

public class UITab extends UIPanel {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Tab";

  private String label;
  private String tip;
  private Boolean disabled;

  public String getTip() {
    if (tip != null) {
      return tip;
    }
    ValueBinding vb = getValueBinding(ATTR_TIP);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  public void setTip(String tip) {
    this.tip = tip;
  }

  public String getLabel() {
    if (label != null) {
      return label;
    }
    ValueBinding vb = getValueBinding(ATTR_LABEL);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return label;
    }
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public boolean isDisabled() {
    if (disabled != null) {
      return disabled;
    }
    ValueBinding vb = getValueBinding(ATTR_DISABLED);
    if (vb != null) {
      return Boolean.TRUE.equals(vb.getValue(getFacesContext()));
    } else {
      return false;
    }
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
  }

  public Object saveState(FacesContext context) {
    Object[] state = new Object[4];
    state[0] = super.saveState(context);
    state[1] = tip;
    state[2] = label;
    state[3] = disabled;
    return state;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    tip = (String) values[1];
    label = (String) values[2];
    disabled = (Boolean) values[3];
  }
}
