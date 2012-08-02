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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FREQUENCY;
import static org.apache.myfaces.tobago.TobagoConstants.ATTR_UPDATE;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/*
 * Date: 28.05.2006
 * Time: 21:57:46
 */
public class UIReload extends UIComponentBase {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Reload";
  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Reload";

  private Integer frequency;

  private Boolean update;

  private Boolean immediate;

  public Object saveState(FacesContext context) {
    Object[] values = new Object[4];
    values[0] = super.saveState(context);
    values[1] = frequency;
    values[2] = update;
    values[3] = immediate;
    return values;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    frequency = (Integer) values[1];
    update = (Boolean) values[2];
    immediate = (Boolean) values[3];
  }

  public int getFrequency() {
    if (frequency != null) {
      return frequency;
    }
    ValueBinding vb = getValueBinding(ATTR_FREQUENCY);
    Integer value = null;
    if (vb != null) {
      value = (Integer) vb.getValue(getFacesContext());
    }
    if (value != null) {
      return value;
    } else {
      return 5000;
    }
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  public void setUpdate(boolean update) {
    this.update = update;
  }

  public boolean getUpdate() {
    if (update != null) {
      return update;
    }
    ValueBinding vb = getValueBinding(ATTR_UPDATE);
    Boolean value = null;
    if (vb != null) {
      value = (Boolean) vb.getValue(getFacesContext());
    }
    if (value != null) {
      return value;
    } else {
      return true;
    }
  }

  public void setImmediate(boolean immediate) {
    this.immediate = immediate;
  }

  public boolean isImmediate() {
    if (immediate != null) {
      return immediate;
    }
    ValueBinding vb = getValueBinding("immediate");
    Boolean value = null;
    if (vb != null) {
      value = (Boolean) vb.getValue(getFacesContext());
    }
    if (value != null) {
      return value;
    } else {
      return false;
    }
  }

  public String getFamily() {
    return COMPONENT_FAMILY;
  }
}
