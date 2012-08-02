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


public class UICell extends UIPanelBase {

  public static final String COMPONENT_TYPE = "org.apache.myfaces.tobago.Cell";

  private Integer spanX;
  private Integer spanY;
  private String scrollbars;

  public Integer getSpanX() {
    if (spanX != null) {
      return spanX;
    }
    javax.faces.el.ValueBinding vb = getValueBinding(TobagoConstants.ATTR_SPAN_X);
    if (vb != null) {
      Number number = (Number) vb.getValue(getFacesContext());
      if (number != null) {
        return number.intValue();
      }
    }
    return 1;
  }

  public void setSpanX(Integer spanX) {
    this.spanX = spanX;
  }

  public Integer getSpanY() {
    if (spanY != null) {
      return spanY;
    }
    javax.faces.el.ValueBinding vb = getValueBinding(TobagoConstants.ATTR_SPAN_Y);
    if (vb != null) {
      Number number = (Number) vb.getValue(getFacesContext());
      if (number != null) {
        return number.intValue();
      }
    }
    return 1;
  }

  public void setSpanY(Integer spanY) {
    this.spanY = spanY;
  }

  public String getScrollbars() {
    if (scrollbars != null) {
      return scrollbars;
    }
    javax.faces.el.ValueBinding vb = getValueBinding(TobagoConstants.ATTR_SCROLLBARS);
    if (vb != null) {
      java.lang.String scrollbars = (java.lang.String) vb.getValue(getFacesContext());
      if (scrollbars != null) {
        return scrollbars;
      }
    }
    return "false";
  }

  public void setScrollbars(String scrollbars) {
    this.scrollbars = scrollbars;
  }

  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    spanX = (Integer) values[1];
    spanY = (Integer) values[2];
    scrollbars = (String) values[3];
  }

  public Object saveState(FacesContext context) {
    Object[] values = new Object[4];
    values[0] = super.saveState(context);
    values[1] = spanX;
    values[2] = spanY;
    values[3] = scrollbars;
    return values;
  }
}
