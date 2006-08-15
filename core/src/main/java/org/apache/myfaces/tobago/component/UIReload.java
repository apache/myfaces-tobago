package org.apache.myfaces.tobago.component;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_FREQUENCY;

import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

/**
 * Created by IntelliJ IDEA.
 * User: bommel
 * Date: 28.05.2006
 * Time: 21:57:46
 * To change this template use File | Settings | File Templates.
 */
public class UIReload extends UIComponentBase {
  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Reload";
  public static final String COMPONENT_FAMILY = "org.apache.myfaces.tobago.Reload";

  private Integer frequency;

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = frequency;
    return values;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    frequency = (Integer) values[1];
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

  public String getFamily() {
    return COMPONENT_FAMILY;
  }
}
