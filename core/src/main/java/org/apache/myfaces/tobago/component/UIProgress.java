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

import static org.apache.myfaces.tobago.TobagoConstants.ATTR_TIP;

import javax.faces.el.ValueBinding;
import javax.faces.context.FacesContext;

/*
 * User: bommel
 * Date: Apr 24, 2007
 * Time: 8:07:24 PM
 */
public class UIProgress extends javax.faces.component.UIOutput implements SupportsMarkup {
  private String[] markup;
  private String tip;

  @Override
  public void restoreState(FacesContext context, Object state) {
    Object[] values = (Object[]) state;
    super.restoreState(context, values[0]);
    markup = (String[]) values[1];
    tip = (String) values[2];
  }

  @Override
  public Object saveState(FacesContext context) {
    Object[] values = new Object[3];
    values[0] = super.saveState(context);
    values[1] = markup;
    values[2] = tip;
    return values;
  }

  public String[] getMarkup() {
    if (markup != null) {
      return markup;
    }
    return ComponentUtil.getMarkupBinding(getFacesContext(), this);
  }

  public void setMarkup(String[] markup) {
    this.markup = markup;
  }

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

}
