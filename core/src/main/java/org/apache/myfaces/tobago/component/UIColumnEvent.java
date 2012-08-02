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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_EVENT;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.component.UIColumn;

/**
 * @since 1.0.13
 */
public class UIColumnEvent extends UIColumn {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.ColumnEvent";

  private String event;

  public Object saveState(FacesContext context) {
    Object[] values = new Object[2];
    values[0] = super.saveState(context);
    values[1] = event;
    return values;
  }

  public void restoreState(FacesContext context, Object savedState) {
    Object[] values = (Object[]) savedState;
    super.restoreState(context, values[0]);
    event = (String) values[1];
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getEvent() {
    if (event != null) {
      return event;
    }
    ValueBinding vb = getValueBinding(ATTR_EVENT);
    if (vb != null) {
      return (String) vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }
}
